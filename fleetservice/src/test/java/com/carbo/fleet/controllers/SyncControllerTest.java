
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
import org.mockito.junit.jupiter.MockitoExtension;

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
    public void shouldReturnFleetIdsWithTimestampsWhenViewIsCalled() {
        String organizationId = "org123";
        List<Fleet> fleets = Arrays.asList(new Fleet(), new Fleet());

        for (int i = 0; i < fleets.size(); i++) {
            fleets.get(i).setId("fleet" + i);
            fleets.get(i).setTs((long) (i + 1));
        }

        when(request.getUserPrincipal()).thenReturn(new MockPrincipal(organizationId));
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        Map<String, Long> result = syncController.view(request);

        Map<String, Long> expected = new HashMap<>();
        for (Fleet fleet : fleets) {
            expected.put(fleet.getId(), fleet.getTs());
        }

        assertEquals(expected, result);
    }

    @Test
    public void shouldUpdateAndReturnSyncResponseWhenSyncIsCalled() {
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUpdate(Arrays.asList(new Fleet(), new Fleet()));
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1", "fleet2")));
        syncRequest.setGet(new HashSet<>(Arrays.asList("fleet3")));

        String organizationId = "org123";

        when(request.getUserPrincipal()).thenReturn(new MockPrincipal(organizationId));
        when(fleetService.getFleet(Mockito.anyString())).thenReturn(Optional.of(new Fleet()));
        when(fleetService.deleteFleet(Mockito.anyString())).thenReturn(null);
        when(fleetService.saveFleet(Mockito.any(Fleet.class))).thenReturn(new Fleet());

        SyncResponse response = syncController.sync(request, syncRequest);

        assertEquals(2, response.getUpdated().size());
        assertEquals(2, response.getRemoved().size());
        assertEquals(0, response.getGet().size()); // No fleets retrieved in this case
    }

    private static class MockPrincipal implements java.security.Principal {
        private final String organizationId;

        public MockPrincipal(String organizationId) {
            this.organizationId = organizationId;
        }

        @Override
        public String getName() {
            return organizationId;
        }
    }
}
