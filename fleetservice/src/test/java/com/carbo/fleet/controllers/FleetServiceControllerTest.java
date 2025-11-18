
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class FleetServiceControllerTest {
    
    @Mock
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Test
    public void shouldReturnAllFleetsWhenOrganizationTypeIsOperator() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet 1");
        Mockito.when(fleetService.getByOrganizationId(any())).thenReturn(Collections.singletonList(fleet));

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fleet 1", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetById() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet 1");
        Mockito.when(fleetService.getFleet(eq("1"))).thenReturn(Optional.of(fleet));

        // Act
        var result = fleetServiceController.getFleet("1");

        // Assert
        assertEquals("Fleet 1", result.getName());
    }

    @Test
    public void shouldUpdateFleet() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Updated Fleet");

        // Act
        fleetServiceController.updateFleet("1", fleet);

        // Assert
        Mockito.verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("New Fleet");

        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        Mockito.verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        // Act
        fleetServiceController.deleteFleet("1");

        // Assert
        Mockito.verify(fleetService).deleteFleet("1");
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet 1");
        Mockito.when(fleetService.findDistinctByOrganizationIdAndName(any(), any())).thenReturn(Optional.of(fleet));

        // Act
        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, "Fleet 1");

        // Assert
        assertEquals("Fleet 1", result.get().getName());
    }

    @Test
    public void shouldReturnFleetData() {
        // Arrange
        ResponseEntity responseEntity = ResponseEntity.ok(Collections.emptyMap());
        Mockito.when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(responseEntity, result);
    }
}
