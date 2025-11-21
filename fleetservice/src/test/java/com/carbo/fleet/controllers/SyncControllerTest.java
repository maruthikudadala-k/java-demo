
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
    public void shouldReturnMapOfFleetIdAndTsWhenViewIsCalled() {
        String organizationId = "org123";
        List<Fleet> fleets = Arrays.asList(
                new Fleet() {{ setId("fleet1"); setTs(100L); }},
                new Fleet() {{ setId("fleet2"); setTs(200L); }}
        );

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        Map<String, Long> result = syncController.view(request);

        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet1"));
        assertEquals(200L, result.get("fleet2"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Collections.singletonList("fleet1")));
        syncRequest.setUpdate(new ArrayList<>());
        syncRequest.setGet(new HashSet<>(Collections.singletonList("fleet2")));

        String organizationId = "org123";
        Fleet fleetToGet = new Fleet() {{ setId("fleet2"); setTs(200L); }};

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet("fleet2")).thenReturn(Optional.of(fleetToGet));
        when(fleetService.deleteFleet("fleet1")).thenReturn(null);

        SyncResponse response = syncController.sync(request, syncRequest);

        assertEquals(1, response.getRemoved().size());
        assertEquals("fleet1", response.getRemoved().iterator().next());
        assertEquals(1, response.getGet().size());
        assertEquals("fleet2", response.getGet().get(0).getId());
    }
}
