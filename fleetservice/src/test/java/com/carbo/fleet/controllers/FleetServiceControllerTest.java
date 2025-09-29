
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FleetServiceControllerTest {

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Mock
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnFleetsWhenGetFleetsCalled() {
        String organizationId = "test-org-id";
        String organizationType = "OPERATOR";

        when(request.getHeader("organizationId")).thenReturn(organizationId);
        when(request.getHeader("organizationType")).thenReturn(organizationType);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fleetService, times(1)).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetCalled() {
        String fleetId = "test-fleet-id";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        var result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertEquals(fleetId, result.getId());
        verify(fleetService, times(1)).getFleet(fleetId);
    }

    @Test
    public void shouldSaveFleetWhenPostFleetCalled() {
        Fleet fleet = new Fleet();
        
        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldUpdateFleetWhenPutFleetCalled() {
        String fleetId = "test-fleet-id";
        Fleet fleet = new Fleet();
        
        // Act
        fleetServiceController.updateFleet(fleetId, fleet);

        // Assert
        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetCalled() {
        String fleetId = "test-fleet-id";
        
        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService, times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetWhenFindDistinctCalled() {
        String fleetName = "test-fleet";
        when(fleetService.findDistinctByOrganizationIdAndName(any(), eq(fleetName))).thenReturn(Optional.empty());

        // Act
        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, fleetName);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
        verify(fleetService, times(1)).findDistinctByOrganizationIdAndName(any(), eq(fleetName));
    }
    
    @Test
    public void shouldReturnFleetDataWhenGetFleetDataCalled() {
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(ResponseEntity.ok().build(), result);
        verify(fleetService, times(1)).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenGetFleetsForCalendarCalled() {
        String organizationId = "test-org-id";
        String organizationType = "OPERATOR";

        when(request.getHeader("organizationId")).thenReturn(organizationId);
        when(request.getHeader("organizationType")).thenReturn(organizationType);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Act
        var result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fleetService, times(1)).getByOrganizationId(organizationId);
    }
}
