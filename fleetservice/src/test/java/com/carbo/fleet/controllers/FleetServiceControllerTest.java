
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    public void shouldReturnAllFleetsWhenOperator() {
        // Arrange
        String organizationId = "org1";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");
        when(request.getUserPrincipal()).thenReturn(() -> organizationId); // Mocking principal
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        // Act
        var result = fleetServiceController.getFleets(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Fleet One", result.get(0).getName());
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetById() {
        // Arrange
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertEquals(fleetId, result.getId());
        verify(fleetService).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleet() {
        // Arrange
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);

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
        String fleetId = "fleet1";

        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        // Arrange
        String organizationId = "org1";
        String name = "Fleet One";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Assert
        assertEquals("fleet1", result.get().getId());
        verify(fleetService).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetData() {
        // Arrange
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity response = fleetServiceController.getFleetData(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(fleetService).getFleetData(request);
    }
}
