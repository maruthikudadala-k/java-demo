
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTimestampMapWhenViewIsCalled() {
        // Arrange
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(100L);
        
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);

        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);
        
        Map<String, Long> expectedResult = new HashMap<>();
        expectedResult.put("fleet1", 100L);
        expectedResult.put("fleet2", 200L);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        assertEquals(expectedResult, result);
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Arrange
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setTs(100L);
        fleet.setOrganizationId(organizationId);
        syncRequest.setUpdate(Collections.singletonList(fleet));

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleet);
        
        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(1, response.getUpdated().size());
        assertEquals(fleet.getId(), response.getUpdated().keySet().iterator().next());
        assertEquals(fleet.getTs(), response.getUpdated().get(fleet.getId()));
        verify(fleetService).deleteFleet(anyString());
        verify(fleetService).updateFleet(any(Fleet.class));
        verify(fleetService).saveFleet(any(Fleet.class));
    }
}
