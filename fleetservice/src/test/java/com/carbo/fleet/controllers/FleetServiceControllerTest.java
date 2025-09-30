
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
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void shouldReturnFleetsWhenOperator() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Test Fleet");
        
        Mockito.when(fleetService.getByOrganizationId(anyString())).thenReturn(Collections.singletonList(fleet));

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Fleet", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetWhenGetFleetById() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Test Fleet");

        Mockito.when(fleetService.getFleet(anyString())).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet("1");

        // Assert
        assertEquals("Test Fleet", result.getName());
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
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Distinct Fleet");

        Mockito.when(fleetService.findDistinctByOrganizationIdAndName(any(HttpServletRequest.class), anyString()))
                .thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, "Distinct Fleet");

        // Assert
        assertEquals("Distinct Fleet", result.get().getName());
    }

    @Test
    public void shouldGetFleetData() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        Mockito.when(fleetService.getFleetData(any(HttpServletRequest.class))).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void shouldReturnFleetsForCalendar() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Calendar Fleet");

        Mockito.when(fleetService.getByOrganizationId(anyString())).thenReturn(Collections.singletonList(fleet));

        // Act
        var result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Calendar Fleet", result.get(0).getName());
    }
}
