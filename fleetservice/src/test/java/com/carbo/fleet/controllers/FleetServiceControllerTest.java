
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
    public void shouldReturnFleetsWhenGetFleetsCalled() {
        // Arrange
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet 1");
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        // Act
        List<Fleet> result = fleetServiceController.getFleets(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fleet 1", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetWhenGetFleetCalled() {
        // Arrange
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Fleet 1");
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertNotNull(result);
        assertEquals(fleetId, result.getId());
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetCalled() {
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
    public void shouldSaveFleetWhenSaveFleetCalled() {
        // Arrange
        Fleet fleet = new Fleet();
        fleet.setName("New Fleet");

        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetCalled() {
        // Arrange
        String fleetId = "fleet1";

        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnOptionalFleetWhenFindDistinctByOrganizationIdAndNameCalled() {
        // Arrange
        String organizationId = "org123";
        String name = "Fleet 1";
        Fleet fleet = new Fleet();
        fleet.setName(name);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
    }

    @Test
    public void shouldReturnResponseEntityWhenGetFleetDataCalled() {
        // Arrange
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenGetFleetsForCalendarCalled() {
        // Arrange
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet 1");
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        // Act
        List<Fleet> result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fleet 1", result.get(0).getName());
    }
}
