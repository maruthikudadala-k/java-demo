
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

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnAllFleetsWhenOperator() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet A");
        List<Fleet> fleetList = Collections.singletonList(fleet);
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(fleetService.getByOrganizationId(anyString())).thenReturn(fleetList);

        // Act
        List<Fleet> result = fleetServiceController.getFleets(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fleet A", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetById() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet A");
        Mockito.when(fleetService.getFleet("1")).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet("1");

        // Assert
        assertEquals("Fleet A", result.getName());
    }

    @Test
    public void shouldUpdateFleet() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet A");

        // Act
        fleetServiceController.updateFleet("1", fleet);

        // Assert
        Mockito.verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setName("Fleet A");

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
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet A");
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(fleetService.findDistinctByOrganizationIdAndName(any(), anyString())).thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, "Fleet A");

        // Assert
        assertEquals("Fleet A", result.get().getName());
    }

    @Test
    public void shouldReturnFleetData() {
        // Arrange
        Mockito.when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(ResponseEntity.ok().build(), result);
    }
}
