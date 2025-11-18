
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
import static org.mockito.Mockito.when;

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
        fleet1.setTs(100L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);
        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);
        
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);
        request.setAttribute("organizationId", organizationId);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        Map<String, Long> expected = new HashMap<>();
        expected.put("fleet1", 100L);
        expected.put("fleet2", 200L);
        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setOrganizationId(organizationId);
        fleet.setTs(100L);
        syncRequest.setUpdate(Collections.singletonList(fleet));
        SyncResponse expectedResponse = new SyncResponse();
        expectedResponse.setUpdated(Collections.singletonMap("fleet1", System.currentTimeMillis()));
        
        when(fleetService.saveFleet(Mockito.any(Fleet.class))).thenReturn(fleet);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));
        request.setAttribute("organizationId", organizationId);

        // Act
        SyncResponse result = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(expectedResponse.getUpdated(), result.getUpdated());
        assertEquals(expectedResponse.getGet(), result.getGet());
    }
}
