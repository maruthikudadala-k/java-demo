
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTimestampMapWhenViewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(123L);
        
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(456L);

        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));
        request.addHeader("Authorization", "Bearer token"); // Simulating authorization header

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        Map<String, Long> expected = new HashMap<>();
        expected.put("fleet1", 123L);
        expected.put("fleet2", 456L);
        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        
        Set<String> removeSet = new HashSet<>(Arrays.asList("fleet1", "fleet2"));
        syncRequest.setRemove(removeSet);

        when(fleetService.deleteFleet("fleet1")).thenReturn(null);
        when(fleetService.deleteFleet("fleet2")).thenReturn(null);
        when(fleetService.saveFleet(any(Fleet.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(removeSet, response.getRemoved());
    }
}
