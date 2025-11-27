
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
class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    @Test
    void shouldReturnFleetTimestampMapWhenViewIsCalled() {
        // Arrange
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(100L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);

        MockHttpServletRequest request = new MockHttpServletRequest();
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));
        request.setAttribute("organizationId", organizationId);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet1"));
        assertEquals(200L, result.get("fleet2"));
    }

    @Test
    void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Arrange
        SyncRequest syncRequest = new SyncRequest();
        Set<String> remove = new HashSet<>(Arrays.asList("fleet1", "fleet2"));
        syncRequest.setRemove(remove);

        Fleet fleet = new Fleet();
        fleet.setId("fleet3");
        fleet.setOrganizationId("org123");
        fleet.setTs(50L);
        
        when(fleetService.getFleet("fleet3")).thenReturn(Optional.of(fleet));
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(fleetService.getByOrganizationId(anyString())).thenReturn(Collections.singletonList(fleet));
        request.setAttribute("organizationId", "org123");

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getRemoved().size());
        assertTrue(response.getRemoved().contains("fleet1"));
        assertTrue(response.getRemoved().contains("fleet2"));
    }
}
