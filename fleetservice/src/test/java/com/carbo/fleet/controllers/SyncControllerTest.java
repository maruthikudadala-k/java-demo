
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
        String organizationId = "org123";
        List<Fleet> fleets = Collections.singletonList(createFleet("fleet1", 123L));
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);
        when(getOrganizationId(request)).thenReturn(organizationId);

        // When
        Map<String, Long> result = syncController.view(request);

        // Then
        assertEquals(1, result.size());
        assertEquals(123L, result.get("fleet1"));
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldSyncFleetsWhenSyncIsCalled() {
        // Given
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1")));
        SyncResponse expectedResponse = new SyncResponse();

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(getOrganizationId(request)).thenReturn(organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(createFleet("fleet1", 123L)));

        // When
        SyncResponse result = syncController.sync(request, syncRequest);

        // Then
        assertTrue(result.getRemoved().contains("fleet1"));
        verify(fleetService).deleteFleet("fleet1");
    }

    @Test
    public void shouldReturnUpdatedFleetsWhenSyncIsCalled() {
        // Given
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUpdate(Collections.singletonList(createFleet("fleet1", 123L)));
        SyncResponse expectedResponse = new SyncResponse();

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(getOrganizationId(request)).thenReturn(organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(createFleet("fleet1", 122L)));

        // When
        SyncResponse result = syncController.sync(request, syncRequest);

        // Then
        assertTrue(result.getUpdated().containsKey("fleet1"));
        verify(fleetService).updateFleet(any(Fleet.class));
    }

    private Fleet createFleet(String id, long ts) {
        Fleet fleet = new Fleet();
        fleet.setId(id);
        fleet.setTs(ts);
        return fleet;
    }
}
