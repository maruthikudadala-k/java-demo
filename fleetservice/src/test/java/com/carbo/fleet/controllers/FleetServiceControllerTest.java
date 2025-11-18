
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
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
    public void shouldReturnAllFleetsWhenOperatorType() {
        // Given
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        List<Fleet> expectedFleets = Collections.singletonList(new Fleet());
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(expectedFleets);

        // When
        List<Fleet> actualFleets = fleetServiceController.getFleets(request);

        // Then
        assertEquals(expectedFleets, actualFleets);
    }

    @Test
    public void shouldReturnFleetWhenFleetIdIsProvided() {
        // Given
        String fleetId = "fleet123";
        Fleet expectedFleet = new Fleet();
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(expectedFleet));

        // When
        Fleet actualFleet = fleetServiceController.getFleet(fleetId);

        // Then
        assertEquals(expectedFleet, actualFleet);
    }

    @Test
    public void shouldUpdateFleet() {
        // Given
        String fleetId = "fleet123";
        Fleet fleetToUpdate = new Fleet();

        // When
        fleetServiceController.updateFleet(fleetId, fleetToUpdate);

        // Then
        Mockito.verify(fleetService).updateFleet(fleetToUpdate);
    }

    @Test
    public void shouldSaveFleet() {
        // Given
        Fleet fleetToSave = new Fleet();

        // When
        fleetServiceController.saveFleet(fleetToSave);

        // Then
        Mockito.verify(fleetService).saveFleet(fleetToSave);
    }

    @Test
    public void shouldDeleteFleet() {
        // Given
        String fleetId = "fleet123";

        // When
        fleetServiceController.deleteFleet(fleetId);

        // Then
        Mockito.verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetByOrganizationIdAndName() {
        // Given
        String name = "fleetName";
        String organizationId = "org123";
        Optional<Fleet> expectedFleet = Optional.of(new Fleet());
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(expectedFleet);

        // When
        Optional<Fleet> actualFleet = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Then
        assertEquals(expectedFleet, actualFleet);
    }
    
    // Additional tests for getFleetData and getFleetsForCalendar can be added similarly
}
