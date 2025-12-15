
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
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @InjectMocks
    private SyncController syncController;

    @Mock
    private FleetService fleetService;

    @Test
    public void shouldReturnMapWhenViewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.addHeader("Authorization", "Bearer token");
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(new Fleet()));

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        assertNotNull(result);
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.addHeader("Authorization", "Bearer token");
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleetId1", "fleetId2")));
        syncRequest.setUpdate(new ArrayList<>(Arrays.asList(new Fleet())));
        syncRequest.setGet(new HashSet<>(Arrays.asList("fleetId3")));
        
        when(fleetService.getFleet(anyString())).thenReturn(Optional.of(new Fleet()));
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertNotNull(response);
        verify(fleetService, times(2)).deleteFleet(anyString());
        verify(fleetService, times(1)).saveFleet(any(Fleet.class));
        verify(fleetService).getByOrganizationId(organizationId);
    }
}
