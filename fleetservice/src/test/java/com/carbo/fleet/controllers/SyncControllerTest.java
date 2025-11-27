
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
    public void shouldReturnFleetTimestampsWhenViewIsCalled() {
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(1000L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(2000L);
        List<Fleet> fleetList = Arrays.asList(fleet1, fleet2);

        when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleetList);
        when(com.carbo.fleet.utils.ControllerUtil.getOrganizationId(request)).thenReturn(organizationId);

        Map<String, Long> expectedResult = new HashMap<>();
        expectedResult.put("fleet1", 1000L);
        expectedResult.put("fleet2", 2000L);

        Map<String, Long> result = syncController.view(request);

        assertEquals(expectedResult, result);
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldSyncFleetsWhenSyncIsCalled() {
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setOrganizationId(organizationId);
        fleetToUpdate.setTs(1500L);
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));

        Fleet existingFleet = new Fleet();
        existingFleet.setId("fleet1");
        existingFleet.setTs(1000L);
        
        when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(existingFleet));
        when(com.carbo.fleet.utils.ControllerUtil.getOrganizationId(request)).thenReturn(organizationId);
        
        SyncResponse response = syncController.sync(request, syncRequest);

        assertEquals(1, response.getUpdated().size());
        assertEquals(1500L, response.getUpdated().get("fleet1").longValue());
        verify(fleetService).updateFleet(fleetToUpdate);
    }

    @Test
    public void shouldRemoveFleetsWhenSyncIsCalledWithRemoveRequest() {
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1", "fleet2")));

        when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        when(com.carbo.fleet.utils.ControllerUtil.getOrganizationId(request)).thenReturn(organizationId);

        SyncResponse response = syncController.sync(request, syncRequest);

        verify(fleetService, times(1)).deleteFleet("fleet1");
        verify(fleetService, times(1)).deleteFleet("fleet2");
        assertEquals(2, response.getRemoved().size());
    }

    @Test
    public void shouldGetFleetsWhenSyncIsCalledWithGetRequest() {
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setGet(new HashSet<>(Arrays.asList("fleet1", "fleet2")));

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        List<Fleet> fleetList = Arrays.asList(fleet1, fleet2);

        when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet1));
        when(fleetService.getFleet("fleet2")).thenReturn(Optional.of(fleet2));
        when(com.carbo.fleet.utils.ControllerUtil.getOrganizationId(request)).thenReturn(organizationId);

        SyncResponse response = syncController.sync(request, syncRequest);

        assertEquals(2, response.getGet().size());
        assertEquals("fleet1", response.getGet().get(0).getId());
        assertEquals("fleet2", response.getGet().get(1).getId());
    }
}
