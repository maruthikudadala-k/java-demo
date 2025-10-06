
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
        when(request.getAttribute("organizationId")).thenReturn("orgId");
        when(request.getAttribute("organizationType")).thenReturn("OPERATOR");
        when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(Collections.emptyList());

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertNotNull(result);
        verify(fleetService, times(1)).getByOrganizationId("orgId");
    }

    @Test
    public void shouldReturnFleetById() {
        // Arrange
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertNotNull(result);
        assertEquals(fleetId, result.getId());
        verify(fleetService, times(1)).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleet() {
        // Arrange
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        
        // Act
        fleetServiceController.updateFleet(fleetId, fleet);

        // Assert
        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Arrange
        Fleet fleet = new Fleet();

        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        // Arrange
        String fleetId = "fleetId";

        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService, times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        // Arrange
        String name = "fleetName";
        when(request.getAttribute("organizationId")).thenReturn("orgId");
        when(fleetService.findDistinctByOrganizationIdAndName("orgId", name)).thenReturn(Optional.of(new Fleet()));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Assert
        assertTrue(result.isPresent());
        verify(fleetService, times(1)).findDistinctByOrganizationIdAndName("orgId", name);
    }

    @Test
    public void shouldGetFleetData() {
        // Arrange
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity<?> result = fleetServiceController.getFleetData(request);

        // Assert
        assertNotNull(result);
        verify(fleetService, times(1)).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOperator() {
        // Arrange
        when(request.getAttribute("organizationId")).thenReturn("orgId");
        when(request.getAttribute("organizationType")).thenReturn("OPERATOR");
        when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(Collections.emptyList());

        // Act
        var result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertNotNull(result);
        verify(fleetService, times(1)).getByOrganizationId("orgId");
    }
}
