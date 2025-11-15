
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
    public void shouldReturnFleetsWhenOrganizationIsOperator() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet A");
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(fleetService.getByOrganizationId(any(String.class))).thenReturn(Collections.singletonList(fleet));
        Mockito.when(request.getHeader("X-Organization-Type")).thenReturn("OPERATOR");

        // Act
        List<Fleet> result = fleetServiceController.getFleets(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fleet A", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetWhenFleetIdIsProvided() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        Mockito.when(fleetService.getFleet(eq("1"))).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet("1");

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void shouldUpdateFleetSuccessfully() {
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
    public void shouldSaveFleetSuccessfully() {
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
    public void shouldDeleteFleetSuccessfully() {
        // Act
        fleetServiceController.deleteFleet("1");

        // Assert
        Mockito.verify(fleetService).deleteFleet("1");
    }

    @Test
    public void shouldReturnDistinctFleetByOrgIdAndName() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        Mockito.when(fleetService.findDistinctByOrganizationIdAndName(any(), any())).thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, "Fleet A");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
    }

    @Test
    public void shouldReturnFleetDataWhenRequestIsMade() {
        // Arrange
        Mockito.when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok(Collections.emptyMap()));

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(ResponseEntity.ok(Collections.emptyMap()), result);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOrganizationIsOperator() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Calendar Fleet A");
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(fleetService.getByOrganizationId(any(String.class))).thenReturn(Collections.singletonList(fleet));
        Mockito.when(request.getHeader("X-Organization-Type")).thenReturn("OPERATOR");

        // Act
        List<Fleet> result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Calendar Fleet A", result.get(0).getName());
    }
}
