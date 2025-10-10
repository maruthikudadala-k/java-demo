
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.SyncRequest;
import com.carbo.fleet.model.SyncResponse;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    public void shouldReturnFleetTimestampMapWhenViewIsCalled() {
        // Arrange
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(123L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(456L);
        when(request.getUserPrincipal()).thenReturn(mock(Principal.class));
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));
        when(getOrganizationId(request)).thenReturn(organizationId);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        assertEquals(2, result.size());
        assertEquals(123L, result.get("fleet1"));
        assertEquals(456L, result.get("fleet2"));
    }

    @Test
    public void shouldSyncFleetsWhenSyncIsCalled() {
        // Arrange
        String organizationId = "org123";
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setTs(100L);
        fleetToUpdate.setOrganizationId(organizationId);
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));
        Map<String, Long> updatedMap = new HashMap<>();
        updatedMap.put(fleetToUpdate.getId(), fleetToUpdate.getTs());
        when(request.getUserPrincipal()).thenReturn(mock(Principal.class));
        when(fleetService.getFleet(fleetToUpdate.getId())).thenReturn(Optional.of(fleetToUpdate));
        when(getOrganizationId(request)).thenReturn(organizationId);

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(1, response.getUpdated().size());
        assertEquals(updatedMap, response.getUpdated());
    }

    @Test
    public void shouldRemoveFleetsWhenSyncIsCalledWithRemoveSet() {
        // Arrange
        String organizationId = "org123";
        String fleetIdToRemove = "fleet1";
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(Collections.singleton(fleetIdToRemove));
        when(request.getUserPrincipal()).thenReturn(mock(Principal.class));
        when(getOrganizationId(request)).thenReturn(organizationId);

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        verify(fleetService, times(1)).deleteFleet(fleetIdToRemove);
        assertEquals(Collections.singleton(fleetIdToRemove), response.getRemoved());
    }
}
