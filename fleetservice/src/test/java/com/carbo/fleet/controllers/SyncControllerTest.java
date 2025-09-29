
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.SyncRequest;
import com.carbo.fleet.model.SyncResponse;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

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
    public void shouldReturnMapOfFleetIdAndTsWhenViewIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "test-user");
        String organizationId = "org-1";
        request.addHeader("Authorization", "Bearer token");

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet-1");
        fleet1.setTs(100L);

        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet-2");
        fleet2.setTs(200L);

        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));

        // When
        Map<String, Long> result = syncController.view(request);

        // Then
        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet-1"));
        assertEquals(200L, result.get("fleet-2"));
        
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldSyncFleetsAndReturnSyncResponseWhenSyncIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "test-user");
        String organizationId = "org-1";
        request.addHeader("Authorization", "Bearer token");

        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet-1");
        fleetToUpdate.setTs(150L);
        fleetToUpdate.setOrganizationId(organizationId);

        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));
        syncRequest.setRemove(Collections.emptySet());
        syncRequest.setGet(Collections.emptySet());

        Fleet existingFleet = new Fleet();
        existingFleet.setId("fleet-1");
        existingFleet.setTs(100L);

        when(fleetService.getFleet("fleet-1")).thenReturn(Optional.of(existingFleet));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleetToUpdate);

        // When
        SyncResponse result = syncController.sync(request, syncRequest);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getUpdated().size());
        assertTrue(result.getUpdated().containsKey("fleet-1"));
        assertEquals(150L, result.getUpdated().get("fleet-1"));

        verify(fleetService).getFleet("fleet-1");
        verify(fleetService).updateFleet(fleetToUpdate);
    }
}
