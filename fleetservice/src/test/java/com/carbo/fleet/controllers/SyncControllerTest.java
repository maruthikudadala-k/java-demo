
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
    public void shouldReturnFleetTimestampMapWhenViewIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> "user");
        
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(1000L);

        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(2000L);

        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));

        // When
        Map<String, Long> result = syncController.view(request);

        // Then
        assertEquals(2, result.size());
        assertEquals(1000L, result.get("fleet1"));
        assertEquals(2000L, result.get("fleet2"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> "user");

        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUpdate(Collections.singletonList(new Fleet()));
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1", "fleet2")));
        syncRequest.setGet(new HashSet<>(Arrays.asList("fleet3", "fleet4")));

        Fleet fleet = new Fleet();
        fleet.setId("fleet3");
        fleet.setTs(3000L);

        when(fleetService.getFleet("fleet3")).thenReturn(Optional.of(fleet));
        when(fleetService.getFleet("fleet4")).thenReturn(Optional.empty());
        when(fleetService.deleteFleet("fleet1")).thenReturn(null);
        when(fleetService.deleteFleet("fleet2")).thenReturn(null);
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleet);

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertNotNull(response);
        assertEquals(2, response.getUpdated().size());
        assertTrue(response.getRemoved().contains("fleet1"));
        assertTrue(response.getRemoved().contains("fleet2"));
        assertEquals(1, response.getGet().size());
        assertEquals("fleet3", response.getGet().get(0).getId());
    }
}
