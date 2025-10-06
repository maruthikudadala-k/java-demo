
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.Job;
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
import static org.mockito.ArgumentMatchers.anyString;

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
    public void shouldReturnFleetsWhenGetFleetsCalled() {
        // Arrange
        String organizationId = "org1";
        String organizationType = "OPERATOR";
        Job job = new Job();
        job.setFleet("Fleet1");
        job.setOrganizationId(organizationId);

        Mockito.when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        Mockito.when(request.getAttribute("organizationType")).thenReturn(organizationType);
        Mockito.when(mongoTemplate.find(any(), any())).thenReturn(Collections.singletonList(job));
        Mockito.when(mongoTemplate.find(any(), Mockito.eq(Fleet.class))).thenReturn(Collections.singletonList(new Fleet()));

        // Act
        List<Fleet> fleets = fleetServiceController.getFleets(request);

        // Assert
        assertNotNull(fleets);
        assertFalse(fleets.isEmpty());
    }

    @Test
    public void shouldReturnFleetWhenGetFleetCalled() {
        // Arrange
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        
        Mockito.when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertNotNull(result);
        assertEquals(fleetId, result.getId());
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetCalled() {
        // Arrange
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        
        // Act
        fleetServiceController.updateFleet(fleetId, fleet);

        // Assert
        Mockito.verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetCalled() {
        // Arrange
        Fleet fleet = new Fleet();
        
        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        Mockito.verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetCalled() {
        // Arrange
        String fleetId = "fleet123";
        
        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        Mockito.verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetWhenFindDistinctByOrganizationIdAndNameCalled() {
        // Arrange
        String organizationId = "org1";
        String fleetName = "Fleet1";
        Fleet fleet = new Fleet();
        
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        Mockito.when(fleetService.findDistinctByOrganizationIdAndName(organizationId, fleetName)).thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, fleetName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataCalled() {
        // Arrange
        ResponseEntity expectedResponse = ResponseEntity.ok(Collections.emptyMap());
        Mockito.when(fleetService.getFleetData(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity response = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenGetFleetsForCalendarCalled() {
        // Arrange
        String organizationId = "org1";
        String organizationType = "OPERATOR";
        Job job = new Job();
        job.setFleet("Fleet1");
        job.setOrganizationId(organizationId);

        Mockito.when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        Mockito.when(request.getAttribute("organizationType")).thenReturn(organizationType);
        Mockito.when(mongoTemplate.find(any(), any())).thenReturn(Collections.singletonList(job));
        Mockito.when(mongoTemplate.find(any(), Mockito.eq(Fleet.class))).thenReturn(Collections.singletonList(new Fleet()));

        // Act
        List<Fleet> fleets = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertNotNull(fleets);
        assertFalse(fleets.isEmpty());
    }
}
