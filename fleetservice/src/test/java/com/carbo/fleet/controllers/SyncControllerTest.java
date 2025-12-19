
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
    public void shouldReturnFleetTsMapWhenViewIsCalled() {
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(100L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);
        
        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);
        Map<String, Long> expectedResult = new HashMap<>();
        expectedResult.put(fleet1.getId(), fleet1.getTs());
        expectedResult.put(fleet2.getId(), fleet2.getTs());

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        Map<String, Long> result = syncController.view(request);

        assertEquals(expectedResult, result);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        SyncRequest syncRequest = new SyncRequest();
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setTs(100L);
        fleet.setOrganizationId("org123");
        syncRequest.setUpdate(Collections.singletonList(fleet));
        syncRequest.setRemove(Collections.singleton("fleet1"));
        syncRequest.setGet(Collections.singleton("fleet1"));

        when(request.getUserPrincipal()).thenReturn(() -> "org123");
        when(fleetService.getFleet(fleet.getId())).thenReturn(Optional.of(fleet));
        when(fleetService.getByOrganizationId("org123")).thenReturn(Collections.singletonList(fleet));
        when(fleetService.deleteFleet(fleet.getId())).thenReturn(null);
        when(fleetService.saveFleet(Mockito.any(Fleet.class))).thenReturn(fleet);
        when(fleetService.updateFleet(Mockito.any(Fleet.class))).thenReturn(null);

        SyncResponse response = syncController.sync(request, syncRequest);

        assertEquals(1, response.getUpdated().size());
        assertEquals(1, response.getRemoved().size());
        assertEquals(1, response.getGet().size());
    }
}
