
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTsMapWhenViewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        List<Fleet> fleets = Arrays.asList(new Fleet(), new Fleet());
        fleets.get(0).setId("fleet1");
        fleets.get(0).setTs(123L);
        fleets.get(1).setId("fleet2");
        fleets.get(1).setTs(456L);

        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        assertEquals(2, result.size());
        assertEquals(123L, result.get("fleet1"));
        assertEquals(456L, result.get("fleet2"));
    }

    @Test
    public void shouldSyncFleetsAndReturnResponse() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1")));
        syncRequest.setUpdate(new ArrayList<>(Arrays.asList(new Fleet())));
        syncRequest.getUpdate().get(0).setId("fleet2");
        syncRequest.getUpdate().get(0).setOrganizationId(organizationId);
        syncRequest.getUpdate().get(0).setTs(100L);
        
        when(fleetService.getFleet("fleet2")).thenReturn(Optional.of(new Fleet()));
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(new Fleet()));

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.getRemoved().contains("fleet1"));
        assertEquals(1, response.getUpdated().size());
    }

    @Test
    public void shouldReturnResponseWithGetFleets() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setGet(new HashSet<>(Arrays.asList("fleet1", "fleet2")));

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setOrganizationId(organizationId);
        fleet1.setTs(123L);

        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setOrganizationId(organizationId);
        fleet2.setTs(456L);

        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet1));
        when(fleetService.getFleet("fleet2")).thenReturn(Optional.of(fleet2));

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertNotNull(response.getGet());
        assertEquals(2, response.getGet().size());
        assertEquals("fleet1", response.getGet().get(0).getId());
        assertEquals("fleet2", response.getGet().get(1).getId());
    }
}
