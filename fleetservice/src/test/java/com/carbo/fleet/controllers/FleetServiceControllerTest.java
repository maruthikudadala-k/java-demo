
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

    @Mock
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Test
    public void shouldReturnAllFleetsWhenOperator() {
        // Arrange
        when(request.getAttribute("organizationType")).thenReturn("OPERATOR");
        when(fleetService.getByOrganizationId(any())).thenReturn(Collections.emptyList());

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fleetService).getByOrganizationId(any());
    }

    @Test
    public void shouldReturnFleetById() {
        // Arrange
        String fleetId = "fleetId123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        var result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertNotNull(result);
        assertEquals(fleetId, result.getId());
        verify(fleetService).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleet() {
        // Arrange
        String fleetId = "fleetId123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        
        // Act
        fleetServiceController.updateFleet(fleetId, fleet);

        // Assert
        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Arrange
        Fleet fleet = new Fleet();

        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        // Arrange
        String fleetId = "fleetId123";

        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        // Arrange
        String name = "FleetName";
        Fleet fleet = new Fleet();
        fleet.setName(name);
        when(fleetService.findDistinctByOrganizationIdAndName(any(), any())).thenReturn(Optional.of(fleet));

        // Act
        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
        verify(fleetService).findDistinctByOrganizationIdAndName(any(), eq(name));
    }

    @Test
    public void shouldGetFleetData() {
        // Arrange
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok(Collections.emptyMap()));

        // Act
        ResponseEntity<?> result = fleetServiceController.getFleetData(request);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(fleetService).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOperator() {
        // Arrange
        when(request.getAttribute("organizationType")).thenReturn("OPERATOR");
        when(fleetService.getByOrganizationId(any())).thenReturn(Collections.emptyList());

        // Act
        var result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fleetService).getByOrganizationId(any());
    }
}
