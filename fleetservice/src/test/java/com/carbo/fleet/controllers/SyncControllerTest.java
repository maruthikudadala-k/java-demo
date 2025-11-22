
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
    public void shouldReturnFleetIdsAndTimestampsWhenViewIsCalled() {
        String organizationId = "org123";
        List<Fleet> fleets = Arrays.asList(new Fleet(), new Fleet());
        fleets.get(0).setId("fleet1");
        fleets.get(0).setTs(123L);
        fleets.get(1).setId("fleet2");
        fleets.get(1).setTs(456L);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        Map<String, Long> result = syncController.view(request);

        Map<String, Long> expected = new HashMap<>();
        expected.put("fleet1", 123L);
        expected.put("fleet2", 456L);
        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setOrganizationId(organizationId);
        fleet.setTs(1000L);
        
        syncRequest.setUpdate(Collections.singletonList(fleet));
        syncRequest.setRemove(Collections.singleton("fleet2"));
        syncRequest.setGet(Collections.singleton("fleet3"));

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet));
        when(fleetService.getFleet("fleet2")).thenReturn(Optional.empty());
        when(fleetService.getFleet("fleet3")).thenReturn(Optional.of(fleet));

        SyncResponse response = syncController.sync(request, syncRequest);

        assertEquals(1, response.getUpdated().size());
        assertEquals(1, response.getRemoved().size());
        assertEquals(1, response.getGet().size());
    }
}
