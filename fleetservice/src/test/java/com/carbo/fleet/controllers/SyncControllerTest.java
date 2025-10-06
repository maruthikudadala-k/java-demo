
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.SyncRequest;
import com.carbo.fleet.model.SyncResponse;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    public void shouldReturnMapWithFleetIdAndTsWhenViewIsCalled() {
        // Given
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(1L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(2L);
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));

        // When
        Map<String, Long> result = syncController.view(request);

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get("fleet1"));
        assertEquals(2L, result.get("fleet2"));
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Given
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleetToUpdate");
        fleetToUpdate.setTs(3L);
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));
        Map<String, Long> updated = new HashMap<>();
        updated.put(fleetToUpdate.getId(), System.currentTimeMillis());

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet(fleetToUpdate.getId())).thenReturn(Optional.of(fleetToUpdate));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleetToUpdate);
        when(fleetService.updateFleet(any(Fleet.class))).thenReturn(null);

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertEquals(updated.size(), response.getUpdated().size());
        assertTrue(response.getUpdated().containsKey("fleetToUpdate"));
        verify(fleetService).updateFleet(fleetToUpdate);
        verify(fleetService).getFleet(fleetToUpdate.getId());
    }
}
