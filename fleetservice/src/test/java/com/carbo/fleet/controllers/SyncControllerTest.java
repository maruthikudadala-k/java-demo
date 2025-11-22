
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
    public void shouldReturnMapOfFleetIdsAndTimestampsWhenViewIsCalled() {
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(100L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);
        List<Fleet> fleetList = Arrays.asList(fleet1, fleet2);

        when(request.getUserPrincipal()).thenReturn(mock(Principal.class));
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleetList);
        when(request.getUserPrincipal()).thenReturn(mock(Principal.class));

        Map<String, Long> result = syncController.view(request);

        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet1"));
        assertEquals(200L, result.get("fleet2"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1", "fleet2")));
        syncRequest.setUpdate(Arrays.asList(new Fleet()));
        syncRequest.setGet(new HashSet<>(Arrays.asList("fleet3")));

        String organizationId = "org123";
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleetToUpdate");
        fleetToUpdate.setOrganizationId(organizationId);
        fleetToUpdate.setTs(150L);
        when(request.getUserPrincipal()).thenReturn(mock(Principal.class));
        when(fleetService.getFleet("fleetToUpdate")).thenReturn(Optional.of(fleetToUpdate));
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        SyncResponse response = syncController.sync(request, syncRequest);

        verify(fleetService, times(1)).deleteFleet("fleet1");
        verify(fleetService, times(1)).deleteFleet("fleet2");
        assertNotNull(response);
    }

    @Test
    public void shouldUpdateFleetWhenSyncIsCalledWithValidData() {
        SyncRequest syncRequest = new SyncRequest();
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleetToUpdate");
        fleetToUpdate.setTs(100L);
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));

        String organizationId = "org123";
        fleetToUpdate.setOrganizationId(organizationId);

        when(request.getUserPrincipal()).thenReturn(mock(Principal.class));
        when(fleetService.getFleet(fleetToUpdate.getId())).thenReturn(Optional.of(fleetToUpdate));
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        SyncResponse response = syncController.sync(request, syncRequest);

        verify(fleetService, times(1)).updateFleet(fleetToUpdate);
        assertNotNull(response);
    }
}
