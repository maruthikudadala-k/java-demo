
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
    public void shouldReturnFleetTsMappingWhenViewIsCalled() {
        // Given
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(100L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);

        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);
        
        HttpServletRequest request = new MockHttpServletRequest();
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);
        
        // Mocking the utility method
        when(getOrganizationId(request)).thenReturn(organizationId);

        // When
        Map<String, Long> result = syncController.view(request);

        // Then
        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet1"));
        assertEquals(200L, result.get("fleet2"));
    }

    @Test
    public void shouldSyncFleetDataWhenSyncIsCalled() {
        // Given
        String organizationId = "org123";
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setOrganizationId(organizationId);
        fleetToUpdate.setTs(150L);
        
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(getOrganizationId(request)).thenReturn(organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleetToUpdate));

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.getUpdated().containsKey("fleet1"));
        assertEquals(fleetToUpdate.getTs(), response.getUpdated().get("fleet1"));
    }

    @Test
    public void shouldReturnEmptyUpdatedWhenNoFleetToUpdate() {
        // Given
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUpdate(Collections.emptyList());

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(getOrganizationId(request)).thenReturn(organizationId);

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.getUpdated().isEmpty());
    }

    @Test
    public void shouldRemoveFleetsWhenSyncIsCalledWithRemoveIds() {
        // Given
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1", "fleet2")));

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(getOrganizationId(request)).thenReturn(organizationId);

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        verify(fleetService, times(2)).deleteFleet(Mockito.anyString());
        assertNotNull(response);
        assertNotNull(response.getRemoved());
        assertEquals(2, response.getRemoved().size());
    }

    @Test
    public void shouldReturnSyncResponseWithGetFleets() {
        // Given
        String organizationId = "org123";
        Fleet fleetToGet = new Fleet();
        fleetToGet.setId("fleet1");
        fleetToGet.setOrganizationId(organizationId);

        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setGet(Collections.singleton("fleet1"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(getOrganizationId(request)).thenReturn(organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleetToGet));

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertNotNull(response);
        assertNotNull(response.getGet());
        assertEquals(1, response.getGet().size());
        assertEquals("fleet1", response.getGet().get(0).getId());
    }
}
