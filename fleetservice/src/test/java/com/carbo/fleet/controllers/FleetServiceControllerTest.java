
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
    public void shouldReturnFleetsWhenGetFleetsIsCalled() {
        // Given
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        // When
        List<Fleet> result = fleetServiceController.getFleets(request);

        // Then
        assertEquals(1, result.size());
        assertEquals("Fleet One", result.get(0).getName());
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        // Given
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Fleet One");

        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // When
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Then
        assertEquals(fleetId, result.getId());
        assertEquals("Fleet One", result.getName());
        verify(fleetService).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        // Given
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Updated Fleet");

        // When
        fleetServiceController.updateFleet(fleetId, fleet);

        // Then
        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        // Given
        Fleet fleet = new Fleet();
        fleet.setName("New Fleet");

        // When
        fleetServiceController.saveFleet(fleet);

        // Then
        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        // Given
        String fleetId = "fleet1";

        // When
        fleetServiceController.deleteFleet(fleetId);

        // Then
        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetWhenFindDistinctByOrganizationIdAndNameIsCalled() {
        // Given
        String organizationId = "org123";
        String name = "Fleet One";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName(name);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        // When
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Then
        assertEquals(fleet, result.get());
        verify(fleetService).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        // Given
        when(request.getUserPrincipal()).thenReturn(() -> "org123");
        ResponseEntity expectedResponse = ResponseEntity.ok(Collections.emptyMap());
        when(fleetService.getFleetData(request)).thenReturn(expectedResponse);

        // When
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Then
        assertEquals(expectedResponse, result);
        verify(fleetService).getFleetData(request);
    }
}
