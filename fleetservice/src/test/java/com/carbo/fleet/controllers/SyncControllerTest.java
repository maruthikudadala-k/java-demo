
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
import org.mockito.junit.MockitoJUnitExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoJUnitExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTsMapWhenViewCalled() {
        // Given
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(123L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(456L);
        List<Fleet> fleetList = Arrays.asList(fleet1, fleet2);

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleetList);
        when(getOrganizationId(request)).thenReturn(organizationId);

        // When
        Map<String, Long> result = syncController.view(request);

        // Then
        assertEquals(2, result.size());
        assertEquals(123L, result.get("fleet1"));
        assertEquals(456L, result.get("fleet2"));
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncCalledWithUpdate() {
        // Given
        String organizationId = "org123";
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setTs(123L);
        fleetToUpdate.setOrganizationId(organizationId);
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleetToUpdate));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleetToUpdate);
        when(getOrganizationId(request)).thenReturn(organizationId);

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertEquals(1, response.getUpdated().size());
        assertEquals(123L, response.getUpdated().get("fleet1"));
        verify(fleetService).updateFleet(fleetToUpdate);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncCalledWithRemove() {
        // Given
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Collections.singletonList("fleet1")));

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(getOrganizationId(request)).thenReturn(organizationId);

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertEquals(1, response.getRemoved().size());
        assertTrue(response.getRemoved().contains("fleet1"));
        verify(fleetService).deleteFleet("fleet1");
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncCalledWithGet() {
        // Given
        String organizationId = "org123";
        Fleet fleetToGet = new Fleet();
        fleetToGet.setId("fleet1");
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setGet(new HashSet<>(Collections.singletonList("fleet1")));

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleetToGet));
        when(getOrganizationId(request)).thenReturn(organizationId);

        // When
        SyncResponse response = syncController.sync(request, syncRequest);

        // Then
        assertEquals(1, response.getGet().size());
        assertEquals("fleet1", response.getGet().get(0).getId());
        verify(fleetService).getFleet("fleet1");
    }
}
