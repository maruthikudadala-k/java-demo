
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FleetServiceControllerTest {

    @Mock
    private FleetService fleetService;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnAllFleetsWhenOperator() {
        // Arrange
        String organizationId = "orgId";
        String organizationType = "OPERATOR";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId); // Mock principal
        // Assume getOrganizationId and getOrganizationType will fetch the mocked values
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetById() {
        // Arrange
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertNotNull(result);
        assertEquals(fleet, result);
        verify(fleetService).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleet() {
        // Arrange
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();

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
        String fleetId = "fleetId";

        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        // Arrange
        String name = "Fleet A";
        String organizationId = "orgId";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(new Fleet()));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Assert
        assertTrue(result.isPresent());
        verify(fleetService).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldGetFleetData() {
        // Arrange
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok(Collections.emptyMap()));

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(fleetService).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendar() {
        // Arrange
        String organizationId = "orgId";
        String organizationType = "OPERATOR";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Act
        var result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fleetService).getByOrganizationId(organizationId);
    }
}
