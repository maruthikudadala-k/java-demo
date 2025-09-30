
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
    public void shouldReturnFleetTimestampMapWhenViewIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "test-principal");
        String organizationId = "org123";
        request.setAttribute("organizationId", organizationId);

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(1L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(2L);
        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);
        
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        // When
        Map<String, Long> result = syncController.view(request);

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get("fleet1"));
        assertEquals(2L, result.get("fleet2"));
    }

    @Test
    public void shouldSyncFleetsWhenSyncIsCalledWithValidRequest() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "test-principal");
        String organizationId = "org123";
        request.setAttribute("organizationId", organizationId);

        SyncRequest syncRequest = new SyncRequest();
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setTs(1L);
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));

        Fleet existingFleet = new Fleet();
        existingFleet.setId("fleet1");
        existingFleet.setTs(0L);
        existingFleet.setOrganizationId(organizationId);

        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(existingFleet));
        when(fleetService.updateFleet(fleetToUpdate)).thenReturn(null);

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.getUpdated().containsKey("fleet1"));
        assertEquals(1L, response.getUpdated().get("fleet1"));
    }

    @Test
    public void shouldRemoveFleetsWhenSyncIsCalledWithRemoveRequest() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "test-principal");
        String organizationId = "org123";
        request.setAttribute("organizationId", organizationId);

        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1", "fleet2")));

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        verify(fleetService, times(1)).deleteFleet("fleet1");
        verify(fleetService, times(1)).deleteFleet("fleet2");
        assertNotNull(response);
        assertEquals(2, response.getRemoved().size());
    }

    @Test
    public void shouldReturnSyncResponseWithGetWhenSyncIsCalledWithGetRequest() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "test-principal");
        String organizationId = "org123";
        request.setAttribute("organizationId", organizationId);

        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setGet(new HashSet<>(Arrays.asList("fleet1")));

        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setOrganizationId(organizationId);
        
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet));

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getGet().size());
        assertEquals("fleet1", response.getGet().get(0).getId());
    }
}
