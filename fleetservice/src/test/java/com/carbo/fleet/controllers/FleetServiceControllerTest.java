
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void shouldReturnFleetsWhenOrganizationTypeIsOperator() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setName("Fleet A");
        
        when(fleetService.getByOrganizationId(anyString())).thenReturn(Collections.singletonList(fleet));

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fleet A", result.get(0).getName());
        verify(fleetService).getByOrganizationId(anyString());
    }

    @Test
    public void shouldReturnFleetWhenFleetIdIsProvided() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet A");
        
        when(fleetService.getFleet("1")).thenReturn(Optional.of(fleet));

        // Act
        var result = fleetServiceController.getFleet("1");

        // Assert
        assertEquals("Fleet A", result.getName());
        verify(fleetService).getFleet("1");
    }

    @Test
    public void shouldUpdateFleetWhenFleetIdIsProvided() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet A");

        // Act
        fleetServiceController.updateFleet("1", fleet);

        // Assert
        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setName("Fleet A");

        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenFleetIdIsProvided() {
        // Act
        fleetServiceController.deleteFleet("1");

        // Assert
        verify(fleetService).deleteFleet("1");
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet A");

        when(fleetService.findDistinctByOrganizationIdAndName(any(), any())).thenReturn(Optional.of(fleet));

        // Act
        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, "Fleet A");

        // Assert
        assertEquals("Fleet A", result.get().getName());
        verify(fleetService).findDistinctByOrganizationIdAndName(any(), any());
    }

    @Test
    public void shouldReturnFleetData() {
        // Arrange
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(responseEntity, result);
        verify(fleetService).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOrganizationTypeIsOperator() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setName("Fleet A");

        when(fleetService.getByOrganizationId(anyString())).thenReturn(Collections.singletonList(fleet));

        // Act
        var result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fleet A", result.get(0).getName());
        verify(fleetService).getByOrganizationId(anyString());
    }
}
