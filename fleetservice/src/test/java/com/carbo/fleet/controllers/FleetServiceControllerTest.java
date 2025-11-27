
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void shouldReturnFleetsWhenOrganizationTypeIsOperator() {
        // Arrange
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);
        when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(Collections.singletonList(new Fleet()));

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertEquals(1, result.size());
        verify(mongoTemplate, times(1)).find(any(), eq(Fleet.class));
    }

    @Test
    public void shouldReturnFleetWhenFleetExists() {
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
    public void shouldUpdateFleet() {
        // Arrange
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();

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
        String fleetId = "fleet123";

        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService, times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        // Arrange
        String name = "FleetName";
        String organizationId = "org123";
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(new Fleet()));

        // Act
        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Assert
        assertEquals(Optional.of(new Fleet()), result);
        verify(fleetService, times(1)).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldGetFleetData() {
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
    public void shouldReturnFleetsForCalendarWhenOperator() {
        // Arrange
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);
        when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(Collections.singletonList(new Fleet()));

        // Act
        var result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertEquals(1, result.size());
        verify(mongoTemplate, times(1)).find(any(), eq(Fleet.class));
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenNotOperator() {
        // Arrange
        String organizationId = "org123";
        String organizationType = "NOT_OPERATOR";
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(new Fleet()));

        // Act
        var result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertEquals(1, result.size());
        verify(fleetService, times(1)).getByOrganizationId(organizationId);
    }
}
