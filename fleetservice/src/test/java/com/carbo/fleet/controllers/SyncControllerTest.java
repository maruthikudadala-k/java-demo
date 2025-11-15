
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    public void shouldReturnFleetTsMapWhenViewIsCalled() {
        String organizationId = "orgId";
        Fleet fleet1 = new Fleet();
        fleet1.setId("1");
        fleet1.setTs(100L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("2");
        fleet2.setTs(200L);
        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(anyString())).thenReturn(fleets);

        Map<String, Long> result = syncController.view(request);

        assertEquals(2, result.size());
        assertEquals(100L, result.get("1"));
        assertEquals(200L, result.get("2"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("1", "2")));
        syncRequest.setUpdate(new ArrayList<>());
        syncRequest.setGet(new HashSet<>(Arrays.asList("3")));

        String organizationId = "orgId";
        Fleet fleet = new Fleet();
        fleet.setId("3");
        fleet.setOrganizationId(organizationId);
        fleet.setTs(100L);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet(anyString())).thenReturn(Optional.of(fleet));
        when(fleetService.deleteFleet(anyString())).thenReturn(null);
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleet);
        when(fleetService.updateFleet(any(Fleet.class))).thenReturn(null);

        SyncResponse response = syncController.sync(request, syncRequest);

        assertEquals(2, response.getRemoved().size());
        assertEquals(1, response.getGet().size());
        assertEquals("3", response.getGet().get(0).getId());
    }
}
