
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTimestampsWhenViewIsCalled() {
        String organizationId = "org123";
        List<Fleet> fleets = Arrays.asList(new Fleet(), new Fleet());
        fleets.get(0).setId("fleet1");
        fleets.get(0).setTs(100L);
        fleets.get(1).setId("fleet2");
        fleets.get(1).setTs(200L);

        when(request.getUserPrincipal()).thenReturn((Principal) () -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        Map<String, Long> result = syncController.view(request);

        Map<String, Long> expected = new HashMap<>();
        expected.put("fleet1", 100L);
        expected.put("fleet2", 200L);
        assertEquals(expected, result);
    }

    @Test
    public void shouldSyncFleetsWhenSyncIsCalled() {
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setTs(100L);
        fleetToUpdate.setOrganizationId(organizationId);
        
        Fleet existingFleet = new Fleet();
        existingFleet.setId("fleet1");
        existingFleet.setTs(50L);
        existingFleet.setOrganizationId(organizationId);
        
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));

        when(request.getUserPrincipal()).thenReturn((Principal) () -> organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(existingFleet));
        when(fleetService.saveFleet(Mockito.any(Fleet.class))).thenReturn(fleetToUpdate);
        when(fleetService.updateFleet(Mockito.any(Fleet.class))).thenReturn(null);

        SyncResponse result = syncController.sync(request, syncRequest);

        assertEquals(1, result.getUpdated().size());
        assertEquals(100L, result.getUpdated().get("fleet1"));
        assertEquals(organizationId, fleetToUpdate.getOrganizationId());
    }
}
