
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FleetServiceControllerTest {

    @Mock
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Test
    public void shouldReturnAllFleetsWhenOperator() {
        // Given
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Test Fleet");
        String organizationId = "org123";
        
        when(request.getHeader("organizationId")).thenReturn(organizationId); // Assuming this method to get organizationId
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        // When
        var result = fleetServiceController.getFleets(request);

        // Then
        assertEquals(1, result.size());
        assertEquals("Test Fleet", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetWhenFleetIdExists() {
        // Given
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Test Fleet");
        
        when(fleetService.getFleet("1")).thenReturn(Optional.of(fleet));

        // When
        Fleet result = fleetServiceController.getFleet("1");

        // Then
        assertEquals("Test Fleet", result.getName());
    }

    @Test
    public void shouldUpdateFleet() {
        // Given
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Updated Fleet");

        // When
        fleetServiceController.updateFleet("1", fleet);

        // Then
        Mockito.verify(fleetService, Mockito.times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Given
        Fleet fleet = new Fleet();
        fleet.setName("New Fleet");

        // When
        fleetServiceController.saveFleet(fleet);

        // Then
        Mockito.verify(fleetService, Mockito.times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        // Given
        String fleetId = "1";

        // When
        fleetServiceController.deleteFleet(fleetId);

        // Then
        Mockito.verify(fleetService, Mockito.times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        // Given
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Distinct Fleet");
        
        when(request.getHeader("organizationId")).thenReturn("org123");
        when(fleetService.findDistinctByOrganizationIdAndName("org123", "Distinct Fleet")).thenReturn(Optional.of(fleet));

        // When
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, "Distinct Fleet");

        // Then
        assertEquals("Distinct Fleet", result.get().getName());
    }

    @Test
    public void shouldGetFleetData() {
        // Given
        ResponseEntity responseEntity = ResponseEntity.ok(Collections.emptyMap());
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        // When
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Then
        assertEquals(responseEntity, result);
    }
}
