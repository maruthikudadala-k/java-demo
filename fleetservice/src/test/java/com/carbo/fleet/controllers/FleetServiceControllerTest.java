
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

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Mock
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnFleetsWhenGetFleetsCalled() {
        // Arrange
        String organizationId = "org123";
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertEquals(Collections.emptyList(), result);
        verify(fleetService, times(1)).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetCalled() {
        // Arrange
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertEquals(fleet, result);
        verify(fleetService, times(1)).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetCalled() {
        // Arrange
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();

        // Act
        fleetServiceController.updateFleet(fleetId, fleet);

        // Assert
        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetCalled() {
        // Arrange
        Fleet fleet = new Fleet();

        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetCalled() {
        // Arrange
        String fleetId = "fleet123";

        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService, times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetWhenFindDistinctByOrganizationIdAndNameCalled() {
        // Arrange
        String organizationId = "org123";
        String name = "Fleet Name";
        Fleet fleet = new Fleet();
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Assert
        assertEquals(Optional.of(fleet), result);
        verify(fleetService, times(1)).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataCalled() {
        // Arrange
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(responseEntity, result);
        verify(fleetService, times(1)).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenGetFleetsForCalendarCalled() {
        // Arrange
        String organizationId = "org123";
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Act
        var result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertEquals(Collections.emptyList(), result);
        verify(fleetService, times(1)).getByOrganizationId(organizationId);
    }
}
