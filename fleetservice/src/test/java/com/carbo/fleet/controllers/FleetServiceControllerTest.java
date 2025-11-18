
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        List<Fleet> fleets = fleetServiceController.getFleets(request);

        assertNotNull(fleets);
        assertTrue(fleets.isEmpty());
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet(fleetId);

        assertNotNull(result);
        assertEquals(fleetId, result.getId());
        verify(fleetService).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleet() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        
        fleetServiceController.updateFleet(fleetId, fleet);

        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        
        fleetServiceController.saveFleet(fleet);

        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        String fleetId = "fleet123";
        
        fleetServiceController.deleteFleet(fleetId);

        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        String name = "Test Fleet";
        String organizationId = "org123";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.empty());

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertNotNull(result);
        assertFalse(result.isPresent());
        verify(fleetService).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetData() {
        ResponseEntity expectedResponse = ResponseEntity.ok().build();
        when(fleetService.getFleetData(request)).thenReturn(expectedResponse);

        ResponseEntity result = fleetServiceController.getFleetData(request);

        assertEquals(expectedResponse, result);
        verify(fleetService).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOperator() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        List<Fleet> fleets = fleetServiceController.getFleetsForCalendar(request);

        assertNotNull(fleets);
        assertTrue(fleets.isEmpty());
        verify(fleetService).getByOrganizationId(organizationId);
    }
}
