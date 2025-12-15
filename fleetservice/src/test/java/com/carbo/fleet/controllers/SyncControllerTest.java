
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

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnMapWithFleetIdAndTsWhenViewCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(Mockito.mock(OAuth2Authentication.class));
        
        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(100L);
        
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(200L);
        
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));
        when(getOrganizationId(request)).thenReturn(organizationId);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        assertEquals(2, result.size());
        assertEquals(100L, result.get("fleet1"));
        assertEquals(200L, result.get("fleet2"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncCalledWithUpdate() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(Mockito.mock(OAuth2Authentication.class));
        
        SyncRequest syncRequest = new SyncRequest();
        
        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet1");
        fleetToUpdate.setOrganizationId(organizationId);
        fleetToUpdate.setTs(100L);
        
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));
        
        Fleet existingFleet = new Fleet();
        existingFleet.setId("fleet1");
        existingFleet.setTs(90L);
        
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(existingFleet));
        when(getOrganizationId(request)).thenReturn(organizationId);

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertNotNull(response.getUpdated());
        assertTrue(response.getUpdated().containsKey("fleet1"));
        assertEquals(100L, response.getUpdated().get("fleet1"));
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncCalledWithGet() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(Mockito.mock(OAuth2Authentication.class));
        
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setGet(Collections.singleton("fleet1"));
        
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        
        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet));
        when(getOrganizationId(request)).thenReturn(organizationId);

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertNotNull(response.getGet());
        assertEquals(1, response.getGet().size());
        assertEquals(fleet, response.getGet().get(0));
    }

    @Test
    public void shouldReturnSyncResponseWithRemovedWhenSyncCalledWithRemove() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(Mockito.mock(OAuth2Authentication.class));
        
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setRemove(new HashSet<>(Arrays.asList("fleet1", "fleet2")));
        
        when(getOrganizationId(request)).thenReturn(organizationId);

        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertNotNull(response.getRemoved());
        assertEquals(2, response.getRemoved().size());
        verify(fleetService, times(1)).deleteFleet("fleet1");
        verify(fleetService, times(1)).deleteFleet("fleet2");
    }
}
