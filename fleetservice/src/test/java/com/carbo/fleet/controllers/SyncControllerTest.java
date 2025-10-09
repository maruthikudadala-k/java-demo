
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
    public void shouldReturnMapOfFleetIdsAndTimestampsWhenViewIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org1";
        request.setUserPrincipal(() -> organizationId);
        
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(100L);

        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);

        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));

        // When
        Map<String, Long> result = syncController.view(request);

        // Then
        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet1"));
        assertEquals(200L, result.get("fleet2"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org1";
        request.setUserPrincipal(() -> organizationId);
        
        SyncRequest syncRequest = new SyncRequest();
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setTs(100L);
        syncRequest.setUpdate(Collections.singletonList(fleet));

        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleet);
        when(fleetService.updateFleet(any(Fleet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.getUpdated().containsKey("fleet1"));
        assertEquals(100L, response.getUpdated().get("fleet1"));
    }

    @Test
    public void shouldRemoveFleetsWhenSyncIsCalledWithRemoveIds() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org1";
        request.setUserPrincipal(() -> organizationId);
        
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Collections.singletonList("fleet1")));

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        verify(fleetService, times(1)).deleteFleet("fleet1");
        assertNotNull(response);
        assertTrue(response.getRemoved().contains("fleet1"));
    }

    @Test
    public void shouldGetFleetWhenSyncIsCalledWithGetIds() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org1";
        request.setUserPrincipal(() -> organizationId);
        
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setGet(new HashSet<>(Collections.singletonList("fleet1")));

        Fleet fleet = new Fleet();
        fleet.setId("fleet1");

        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet));

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getGet().size());
        assertEquals("fleet1", response.getGet().get(0).getId());
    }
}
