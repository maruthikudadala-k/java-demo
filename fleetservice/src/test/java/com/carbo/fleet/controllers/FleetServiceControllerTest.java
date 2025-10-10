
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("fleet123");
        fleet.setName("Fleet A");
        List<Fleet> fleets = Collections.singletonList(fleet);

        when(request.getHeader("organizationId")).thenReturn(organizationId);
        when(request.getHeader("organizationType")).thenReturn(organizationType);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        // When
        List<Fleet> result = fleetServiceController.getFleets(request);

        // Then
        assertEquals(1, result.size());
        assertEquals("Fleet A", result.get(0).getName());
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetById() {
        // Given
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // When
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Then
        assertEquals(fleetId, result.getId());
        verify(fleetService).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleet() {
        // Given
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);

        // When
        fleetServiceController.updateFleet(fleetId, fleet);

        // Then
        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Given
        Fleet fleet = new Fleet();
        
        // When
        fleetServiceController.saveFleet(fleet);

        // Then
        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        // Given
        String fleetId = "fleet123";

        // When
        fleetServiceController.deleteFleet(fleetId);

        // Then
        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        // Given
        String name = "Fleet A";
        String organizationId = "org123";
        Fleet fleet = new Fleet();
        fleet.setName(name);
        when(request.getHeader("organizationId")).thenReturn(organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        // When
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Then
        assertEquals(name, result.get().getName());
        verify(fleetService).findDistinctByOrganizationIdAndName(organizationId, name);
    }
}
