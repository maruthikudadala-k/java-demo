
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        when(request.getHeader("organizationId")).thenReturn(organizationId);
        when(request.getHeader("organizationType")).thenReturn(organizationType);
        
        Fleet fleet = new Fleet();
        fleet.setId("fleet123");
        fleet.setName("Fleet A");
        
        List<Fleet> fleetList = Collections.singletonList(fleet);
        when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(fleetList);
        when(mongoTemplate.find(any(), eq(Job.class))).thenReturn(Collections.emptyList());

        // Act
        List<Fleet> result = fleetServiceController.getFleets(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fleet A", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetWhenFleetIdExists() {
        // Arrange
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertNotNull(result);
        assertEquals(fleetId, result.getId());
    }

    @Test
    public void shouldUpdateFleetWhenFleetIdExists() {
        // Arrange
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Updated Fleet");

        // Act
        fleetServiceController.updateFleet(fleetId, fleet);

        // Assert
        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setName("New Fleet");

        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenFleetIdExists() {
        // Arrange
        String fleetId = "fleet123";

        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService, times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnOptionalFleetWhenSearchingByOrganizationIdAndName() {
        // Arrange
        String name = "Fleet A";
        String organizationId = "org123";
        when(request.getHeader("organizationId")).thenReturn(organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(new Fleet()));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    public void shouldReturnResponseEntityWhenGettingFleetData() {
        // Arrange
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(ResponseEntity.ok().build(), result);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOrganizationTypeIsOperator() {
        // Arrange
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        when(request.getHeader("organizationId")).thenReturn(organizationId);
        when(request.getHeader("organizationType")).thenReturn(organizationType);
        
        Fleet fleet = new Fleet();
        fleet.setId("fleet123");
        fleet.setName("Fleet A");
        
        List<Fleet> fleetList = Collections.singletonList(fleet);
        when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(fleetList);
        when(mongoTemplate.find(any(), eq(Job.class))).thenReturn(Collections.emptyList());

        // Act
        List<Fleet> result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fleet A", result.get(0).getName());
    }
}
