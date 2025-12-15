
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTimestampsWhenViewIsCalled() {
        // Arrange
        String organizationId = "org123";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(1L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(2L);

        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        Mockito.when(fleetService.getByOrganizationId(anyString())).thenReturn(fleets);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        Map<String, Long> expected = new HashMap<>();
        expected.put("fleet1", 1L);
        expected.put("fleet2", 2L);
        assertEquals(expected, result);
    }

    @Test
    public void shouldSyncFleetsWhenSyncIsCalled() {
        // Arrange
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        Set<String> removeSet = new HashSet<>(Collections.singletonList("fleet1"));
        syncRequest.setRemove(removeSet);

        Fleet fleet = new Fleet();
        fleet.setId("fleet2");
        fleet.setOrganizationId(organizationId);
        fleet.setTs(1L);
        syncRequest.setUpdate(Collections.singletonList(fleet));

        Mockito.when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        Mockito.when(fleetService.getFleet(anyString())).thenReturn(Optional.of(fleet));
        Mockito.when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleet);
        Mockito.when(fleetService.deleteFleet(anyString())).thenReturn(null);

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(1, response.getUpdated().size());
        assertEquals(1, response.getRemoved().size());
        assertEquals("fleet1", response.getRemoved().iterator().next());
    }
}
