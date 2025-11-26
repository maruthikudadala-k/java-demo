
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class FleetServiceControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnAllFleetsWhenOperator() {
        // Arrange
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        Mockito.when(request.getAttribute("organizationType")).thenReturn("OPERATOR");
        Mockito.when(fleetService.getByOrganizationId(anyString())).thenReturn(fleets);

        // Act
        List<Fleet> result = fleetServiceController.getFleets(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void shouldReturnFleetById() {
        // Arrange
        Fleet fleet = new Fleet();
        Mockito.when(fleetService.getFleet(anyString())).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet("fleetId");

        // Assert
        assertNotNull(result);
    }

    @Test
    public void shouldUpdateFleet() {
        // Arrange
        Fleet fleet = new Fleet();
        fleetServiceController.updateFleet("fleetId", fleet);

        // Act & Assert
        Mockito.verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Arrange
        Fleet fleet = new Fleet();
        fleetServiceController.saveFleet(fleet);

        // Act & Assert
        Mockito.verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        // Arrange
        fleetServiceController.deleteFleet("fleetId");

        // Act & Assert
        Mockito.verify(fleetService).deleteFleet("fleetId");
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        // Arrange
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(fleetService.findDistinctByOrganizationIdAndName(any(HttpServletRequest.class), anyString()))
                .thenReturn(Optional.of(new Fleet()));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, "fleetName");

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    public void shouldGetFleetData() {
        // Arrange
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        Mockito.when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(responseEntity, result);
    }

    @Test
    public void shouldGetFleetsForCalendar() {
        // Arrange
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        Mockito.when(request.getAttribute("organizationType")).thenReturn("OPERATOR");
        Mockito.when(fleetService.getByOrganizationId(anyString())).thenReturn(fleets);

        // Act
        List<Fleet> result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
