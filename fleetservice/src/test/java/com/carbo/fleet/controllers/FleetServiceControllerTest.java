
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void shouldReturnAllFleetsWhenOperator() {
        // Arrange
        List<Fleet> expectedFleets = new ArrayList<>();
        expectedFleets.add(new Fleet());
        
        Mockito.when(fleetService.getByOrganizationId(any())).thenReturn(expectedFleets);
        
        // Act
        List<Fleet> actualFleets = fleetServiceController.getFleets(request);
        
        // Assert
        assertThat(actualFleets).isEqualTo(expectedFleets);
    }

    @Test
    public void shouldReturnFleetById() {
        // Arrange
        String fleetId = "fleetId";
        Fleet expectedFleet = new Fleet();
        Mockito.when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(expectedFleet));
        
        // Act
        Fleet actualFleet = fleetServiceController.getFleet(fleetId);
        
        // Assert
        assertThat(actualFleet).isEqualTo(expectedFleet);
    }

    @Test
    public void shouldUpdateFleet() {
        // Arrange
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        
        // Act
        fleetServiceController.updateFleet(fleetId, fleet);
        
        // Assert
        Mockito.verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Arrange
        Fleet fleet = new Fleet();
        
        // Act
        fleetServiceController.saveFleet(fleet);
        
        // Assert
        Mockito.verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        // Arrange
        String fleetId = "fleetId";
        
        // Act
        fleetServiceController.deleteFleet(fleetId);
        
        // Assert
        Mockito.verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        // Arrange
        String name = "fleetName";
        String organizationId = "organizationId";
        Fleet expectedFleet = new Fleet();
        Mockito.when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name))
                .thenReturn(Optional.of(expectedFleet));
        
        // Act
        Optional<Fleet> actualFleet = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);
        
        // Assert
        assertThat(actualFleet).isEqualTo(Optional.of(expectedFleet));
    }

    @Test
    public void shouldReturnFleetData() {
        // Arrange
        ResponseEntity expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        Mockito.when(fleetService.getFleetData(request)).thenReturn(expectedResponse);
        
        // Act
        ResponseEntity actualResponse = fleetServiceController.getFleetData(request);
        
        // Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnFleetsForCalendar() {
        // Arrange
        List<Fleet> expectedFleets = Collections.singletonList(new Fleet());
        Mockito.when(fleetService.getByOrganizationId(any())).thenReturn(expectedFleets);

        // Act
        List<Fleet> actualFleets = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertThat(actualFleets).isEqualTo(expectedFleets);
    }
}
