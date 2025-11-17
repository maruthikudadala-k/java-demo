
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FleetServiceControllerTest {

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Mock
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnAllFleetsWhenOperator() {
        // Given
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet1 = new Fleet();
        fleet1.setId("1");
        fleet1.setName("Fleet A");
        
        Fleet fleet2 = new Fleet();
        fleet2.setId("2");
        fleet2.setName("Fleet B");

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Arrays.asList(fleet1, fleet2));

        // When
        List<Fleet> fleets = fleetServiceController.getFleets(request);

        // Then
        assertEquals(2, fleets.size());
        assertEquals("Fleet A", fleets.get(0).getName());
        assertEquals("Fleet B", fleets.get(1).getName());
    }

    @Test
    public void shouldReturnFleetById() {
        // Given
        String fleetId = "1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Fleet A");

        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // When
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Then
        assertNotNull(result);
        assertEquals(fleetId, result.getId());
        assertEquals("Fleet A", result.getName());
    }

    @Test
    public void shouldUpdateFleet() {
        // Given
        String fleetId = "1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Updated Fleet");

        // When
        fleetServiceController.updateFleet(fleetId, fleet);

        // Then
        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        // Given
        Fleet fleet = new Fleet();
        fleet.setName("New Fleet");

        // When
        fleetServiceController.saveFleet(fleet);

        // Then
        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        // Given
        String fleetId = "1";

        // When
        fleetServiceController.deleteFleet(fleetId);

        // Then
        verify(fleetService, times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        // Given
        String organizationId = "org123";
        String fleetName = "Fleet A";
        Fleet fleet = new Fleet();
        fleet.setName(fleetName);
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, fleetName)).thenReturn(Optional.of(fleet));

        // When
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, fleetName);

        // Then
        assertTrue(result.isPresent());
        assertEquals(fleetName, result.get().getName());
    }

    @Test
    public void shouldGetFleetData() {
        // Given
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok(Collections.emptyMap()));

        // When
        ResponseEntity responseEntity = fleetServiceController.getFleetData(request);

        // Then
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOperator() {
        // Given
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet A");

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        // When
        List<Fleet> fleets = fleetServiceController.getFleetsForCalendar(request);

        // Then
        assertEquals(1, fleets.size());
        assertEquals("Fleet A", fleets.get(0).getName());
    }
}
