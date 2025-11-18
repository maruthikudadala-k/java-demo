
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
    public void shouldReturnMapWithFleetIdsAndTimestampsWhenViewIsCalled() {
        List<Fleet> fleets = Arrays.asList(
                createFleet("1", 100L),
                createFleet("2", 200L)
        );

        when(request.getUserPrincipal()).thenReturn(Mockito.mock(OAuth2Authentication.class));
        when(fleetService.getByOrganizationId(any())).thenReturn(fleets);
        when(request.getUserPrincipal()).thenReturn(Mockito.mock(OAuth2Authentication.class));
        
        Map<String, Long> expected = new HashMap<>();
        expected.put("1", 100L);
        expected.put("2", 200L);

        Map<String, Long> result = syncController.view(request);

        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalledWithValidSyncRequest() {
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Collections.singletonList("1")));
        syncRequest.setUpdate(new ArrayList<>(Collections.singletonList(createFleet("2", 200L))));
        syncRequest.setGet(new HashSet<>(Collections.singletonList("3")));

        when(request.getUserPrincipal()).thenReturn(Mockito.mock(OAuth2Authentication.class));
        when(fleetService.deleteFleet("1")).thenReturn(null);
        when(fleetService.getFleet("2")).thenReturn(Optional.of(createFleet("2", 200L)));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(createFleet("2", 200L));
        when(fleetService.getFleet("3")).thenReturn(Optional.of(createFleet("3", 300L)));

        SyncResponse response = syncController.sync(request, syncRequest);

        assertEquals(1, response.getRemoved().size());
        assertEquals(1, response.getUpdated().size());
        assertEquals(1, response.getGet().size());
    }

    private Fleet createFleet(String id, Long ts) {
        Fleet fleet = new Fleet();
        fleet.setId(id);
        fleet.setTs(ts);
        fleet.setOrganizationId("org1");
        return fleet;
    }
}
