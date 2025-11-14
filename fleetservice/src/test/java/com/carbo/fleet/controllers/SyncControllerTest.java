
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
    public void shouldReturnFleetTsMapWhenViewIsCalled() {
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(100L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));

        Map<String, Long> result = syncController.view(request);

        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet1"));
        assertEquals(200L, result.get("fleet2"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1", "fleet2")));
        syncRequest.setUpdate(new ArrayList<>());
        syncRequest.setGet(new HashSet<>(Arrays.asList("fleet3")));

        Fleet fleet3 = new Fleet();
        fleet3.setId("fleet3");
        fleet3.setTs(300L);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet("fleet3")).thenReturn(Optional.of(fleet3));
        doNothing().when(fleetService).deleteFleet("fleet1");
        doNothing().when(fleetService).deleteFleet("fleet2");

        SyncResponse response = syncController.sync(request, syncRequest);

        verify(fleetService, times(2)).deleteFleet(anyString());
        verify(fleetService, times(1)).getFleet("fleet3");
        assertEquals(1, response.getGet().size());
        assertEquals("fleet3", response.getGet().get(0).getId());
    }
}
