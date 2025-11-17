
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void shouldReturnMapOfFleetIdsAndTimestampsWhenViewIsCalled() {
        // Arrange
        String organizationId = "org-123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet-1");
        fleet1.setTs(100L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet-2");
        fleet2.setTs(200L);
        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);

        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet-1"));
        assertEquals(200L, result.get("fleet-2"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Arrange
        SyncRequest syncRequest = new SyncRequest();
        Set<String> removeSet = new HashSet<>(Collections.singletonList("fleet-1"));
        syncRequest.setRemove(removeSet);

        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet-2");
        fleetToUpdate.setTs(150L);
        syncRequest.setUpdate(Arrays.asList(fleetToUpdate));

        Fleet existingFleet = new Fleet();
        existingFleet.setId("fleet-2");
        existingFleet.setTs(100L);
        when(fleetService.getFleet("fleet-2")).thenReturn(Optional.of(existingFleet));

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(1, response.getRemoved().size());
        assertEquals(1, response.getUpdated().size());
        assertEquals("fleet-2", response.getUpdated().keySet().iterator().next());
        assertEquals(existingFleet.getCreated(), response.getUpdated().get("fleet-2"));
        
        verify(fleetService).deleteFleet("fleet-1");
        verify(fleetService).updateFleet(fleetToUpdate);
    }

    @Test
    public void shouldReturnSyncResponseWithGetWhenSyncIsCalled() {
        // Arrange
        SyncRequest syncRequest = new SyncRequest();
        Set<String> getSet = new HashSet<>(Collections.singletonList("fleet-2"));
        syncRequest.setGet(getSet);

        Fleet existingFleet = new Fleet();
        existingFleet.setId("fleet-2");
        existingFleet.setTs(200L);
        when(fleetService.getFleet("fleet-2")).thenReturn(Optional.of(existingFleet));

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(1, response.getGet().size());
        assertEquals("fleet-2", response.getGet().get(0).getId());

        verify(fleetService).getFleet("fleet-2");
    }
}
