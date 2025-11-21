
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTsMapWhenViewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(123L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(456L);
        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);

        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);
        request.setAttribute("organizationId", organizationId);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        assertEquals(2, result.size());
        assertEquals(123L, result.get("fleet1"));
        assertEquals(456L, result.get("fleet2"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.addHeader("Authorization", "Bearer token");
        SyncRequest syncRequest = new SyncRequest();
        List<Fleet> updateList = new ArrayList<>();
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setTs(1000L);
        updateList.add(fleetToUpdate);
        syncRequest.setUpdate(updateList);
        Map<String, Long> updatedMap = new HashMap<>();
        updatedMap.put("fleet1", 1000L);

        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleetToUpdate));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleetToUpdate);
        when(fleetService.deleteFleet(anyString())).thenReturn(null);

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(updatedMap, response.getUpdated());
        assertEquals(Collections.emptySet(), response.getRemoved());
        assertEquals(Collections.emptyList(), response.getGet());
    }
}
