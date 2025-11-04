
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.SyncRequest;
import com.carbo.fleet.model.SyncResponse;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTimestampsWhenViewIsCalled() {
        // Arrange
        String organizationId = "org1";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> organizationId);

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(100L);
        
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);

        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet1"));
        assertEquals(200L, result.get("fleet2"));
    }

    @Test
    public void shouldUpdateAndReturnSyncResponseWhenSyncIsCalled() {
        // Arrange
        String organizationId = "org1";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> organizationId);

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setOrganizationId(organizationId);
        fleet1.setTs(100L);

        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUpdate(Collections.singletonList(fleet1));

        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet1));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleet1);
        when(fleetService.deleteFleet(anyString())).thenReturn(null);

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(1, response.getUpdated().size());
        assertEquals(fleet1.getId(), response.getUpdated().keySet().iterator().next());
    }

    @Test
    public void shouldReturnGetFleetResponseWhenGetIsCalled() {
        // Arrange
        String organizationId = "org1";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> organizationId);

        Set<String> getIds = new HashSet<>(Collections.singletonList("fleet1"));
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setGet(getIds);

        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");

        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet));

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(1, response.getGet().size());
        assertEquals("Fleet One", response.getGet().get(0).getName());
    }
}
