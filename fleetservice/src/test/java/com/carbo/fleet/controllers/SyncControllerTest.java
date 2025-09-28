
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
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTsMapWhenViewIsCalled() {
        // Arrange
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(1L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(2L);
        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get("fleet1"));
        assertEquals(2L, result.get("fleet2"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalledWithUpdates() {
        // Arrange
        SyncRequest syncRequest = new SyncRequest();
        List<Fleet> updates = new ArrayList<>();
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setTs(100L);
        updates.add(fleetToUpdate);
        syncRequest.setUpdate(updates);
        String organizationId = "org123";
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleetToUpdate));
        
        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.getUpdated().containsKey("fleet1"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalledWithRemovals() {
        // Arrange
        SyncRequest syncRequest = new SyncRequest();
        Set<String> removals = new HashSet<>(Collections.singletonList("fleet1"));
        syncRequest.setRemove(removals);
        String organizationId = "org123";

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        
        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.getRemoved().contains("fleet1"));
        verify(fleetService).deleteFleet("fleet1");
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalledWithGets() {
        // Arrange
        SyncRequest syncRequest = new SyncRequest();
        Set<String> gets = new HashSet<>(Collections.singletonList("fleet1"));
        syncRequest.setGet(gets);
        String organizationId = "org123";
        
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setTs(1L);
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet));
        
        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getGet().size());
        assertEquals("fleet1", response.getGet().get(0).getId());
    }
}
