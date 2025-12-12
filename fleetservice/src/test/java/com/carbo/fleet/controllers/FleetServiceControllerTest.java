
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
    public void shouldReturnAllFleetsWhenOrganizationTypeIsOperator() {
        // Arrange
        String organizationId = "org123";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fleet One", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetWhenFleetIdIsProvided() {
        // Arrange
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Fleet One");
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        var result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertEquals(fleetId, result.getId());
        assertEquals("Fleet One", result.getName());
    }

    @Test
    public void shouldUpdateFleet() {
        // Arrange
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Updated Fleet");

        // Act
        fleetServiceController.updateFleet(fleetId, fleet);

        // Assert
        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setName("New Fleet");

        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        // Arrange
        String fleetId = "fleet1";

        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        // Arrange
        String organizationId = "org123";
        String name = "Fleet One";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName(name);
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        // Act
        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Assert
        assertEquals(fleet.getId(), result.get().getId());
        assertEquals(name, result.get().getName());
    }

    @Test
    public void shouldReturnFleetData() {
        // Arrange
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok(Collections.emptyMap()));

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOperator() {
        // Arrange
        String organizationId = "org123";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        // Act
        var result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fleet One", result.get(0).getName());
    }
}
