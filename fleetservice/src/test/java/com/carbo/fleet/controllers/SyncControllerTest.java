
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
        fleet1.setTs(1L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(2L);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));

        Map<String, Long> result = syncController.view(request);

        Map<String, Long> expected = new HashMap<>();
        expected.put("fleet1", 1L);
        expected.put("fleet2", 2L);
        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Collections.singletonList("fleet1")));
        syncRequest.setUpdate(new ArrayList<>(Collections.singletonList(new Fleet())));
        syncRequest.setGet(new HashSet<>(Collections.singletonList("fleet2")));
        
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setOrganizationId(organizationId);
        fleetToUpdate.setTs(1L);
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleetToUpdate));
        
        SyncResponse response = syncController.sync(request, syncRequest);

        assertEquals(Collections.singleton("fleet1"), response.getRemoved());
        // Further assertions can be added here to check the updated and get fields in response
    }
}
