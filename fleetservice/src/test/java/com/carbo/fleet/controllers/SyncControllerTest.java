
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.SyncRequest;
import com.carbo.fleet.model.SyncResponse;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    private MockHttpServletRequest request;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testtoken");
    }

    @Test
    public void shouldReturnFleetDataWhenViewCalled() {
        String organizationId = "org123";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setTs(123456789L);

        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);

        Map<String, Long> result = syncController.view(request);

        assertEquals(1, result.size());
        assertEquals(123456789L, result.get("fleet1"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncCalled() {
        String organizationId = "org123";
        SyncRequest syncRequest = new SyncRequest();
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setTs(123456789L);
        fleet.setOrganizationId(organizationId);
        syncRequest.setUpdate(Collections.singletonList(fleet));

        when(fleetService.getFleet(anyString())).thenReturn(Optional.of(fleet));
        when(fleetService.saveFleet(any(Fleet.class))).thenReturn(fleet);
        when(fleetService.updateFleet(any(Fleet.class))).thenReturn(null);
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);

        SyncResponse response = syncController.sync(request, syncRequest);

        assertEquals(1, response.getUpdated().size());
        assertEquals(123456789L, response.getUpdated().get("fleet1"));
    }
}
