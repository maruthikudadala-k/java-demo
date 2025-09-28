
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
        String organizationId = "org1";
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(100L);
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);
        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

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
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1", "fleet2")));
        syncRequest.setUpdate(new ArrayList<>());
        syncRequest.setGet(new HashSet<>(Arrays.asList("fleet3")));

        String organizationId = "org1";
        Fleet fleet3 = new Fleet();
        fleet3.setId("fleet3");
        fleet3.setOrganizationId(organizationId);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getFleet("fleet3")).thenReturn(Optional.of(fleet3));

        // Act
        SyncResponse result = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(1, result.getRemoved().size());
        assertEquals("fleet1", result.getRemoved().iterator().next());
        assertEquals(1, result.getGet().size());
        assertEquals("fleet3", result.getGet().get(0).getId());
    }
}
