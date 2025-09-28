
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
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTimestampsWhenViewIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org-123";
        request.setUserPrincipal(() -> "user");
        
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet-1");
        fleet1.setTs(100L);
        
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet-2");
        fleet2.setTs(200L);
        
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));
        when(fleetService.getOrganizationId(request)).thenReturn(organizationId);

        // When
        Map<String, Long> result = syncController.view(request);

        // Then
        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet-1"));
        assertEquals(200L, result.get("fleet-2"));
    }

    @Test
    public void shouldSyncFleetsWhenSyncIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org-123";
        request.setUserPrincipal(() -> "user");

        SyncRequest syncRequest = new SyncRequest();
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet-1");
        fleetToUpdate.setTs(150L);
        fleetToUpdate.setOrganizationId(organizationId);
        
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));
        Map<String, Long> updatedMap = new HashMap<>();
        updatedMap.put(fleetToUpdate.getId(), fleetToUpdate.getTs());

        when(fleetService.getOrganizationId(request)).thenReturn(organizationId);
        when(fleetService.getFleet(fleetToUpdate.getId())).thenReturn(Optional.of(fleetToUpdate));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleetToUpdate);
        when(fleetService.updateFleet(any(Fleet.class))).thenReturn(null);
        
        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertNotNull(response);
        assertFalse(response.getUpdated().isEmpty());
        assertTrue(response.getUpdated().containsKey(fleetToUpdate.getId()));
    }
}
