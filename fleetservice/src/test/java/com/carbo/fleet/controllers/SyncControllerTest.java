
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTsWhenViewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setAttribute("organizationId", organizationId);

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(1000L);
        
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(2000L);
        
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        Map<String, Long> expected = new HashMap<>();
        expected.put("fleet1", 1000L);
        expected.put("fleet2", 2000L);
        assertEquals(expected, result);
    }

    @Test
    public void shouldSyncFleetsWhenSyncIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setAttribute("organizationId", organizationId);

        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUpdate(Collections.singletonList(createFleet("fleet1", organizationId, 1000L)));
        
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(createFleet("fleet1", organizationId, 500L)));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(createFleet("fleet1", organizationId, 1000L));

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(1, response.getUpdated().size());
        assertEquals(1000L, response.getUpdated().get("fleet1"));
    }

    @Test
    public void shouldRemoveFleetsWhenSyncIsCalledWithRemove() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setAttribute("organizationId", organizationId);

        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(Collections.singleton("fleet1"));

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        verify(fleetService).deleteFleet("fleet1");
        assertEquals(1, response.getRemoved().size());
        assertEquals("fleet1", response.getRemoved().iterator().next());
    }

    private Fleet createFleet(String id, String organizationId, long ts) {
        Fleet fleet = new Fleet();
        fleet.setId(id);
        fleet.setOrganizationId(organizationId);
        fleet.setTs(ts);
        return fleet;
    }
}
