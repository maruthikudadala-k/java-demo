
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
import org.springframework.http.ResponseEntity;

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
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(1000L);
        
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(2000L);
        
        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);
        
        Map<String, Long> result = syncController.view(request);
        
        Map<String, Long> expected = new HashMap<>();
        expected.put("fleet1", 1000L);
        expected.put("fleet2", 2000L);
        
        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setTs(1000L);
        
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));
        
        Fleet existingFleet = new Fleet();
        existingFleet.setId("fleet1");
        existingFleet.setTs(500L);
        existingFleet.setOrganizationId(organizationId);
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(existingFleet));
        
        SyncResponse response = syncController.sync(request, syncRequest);
        
        assertEquals(1, response.getUpdated().size());
        assertEquals(1000L, response.getUpdated().get("fleet1"));
    }
}
