
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        when(fleetService.getByOrganizationId(any())).thenReturn(fleets);

        List<Fleet> result = fleetServiceController.getFleets(request);

        assertEquals(fleets, result);
        verify(fleetService).getByOrganizationId(any());
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet(fleetId);

        assertEquals(fleet, result);
        verify(fleetService).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleet() {
        String fleetId = "fleetId";
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
        String fleetId = "fleetId";

        fleetServiceController.deleteFleet(fleetId);

        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        String name = "fleetName";
        Fleet fleet = new Fleet();
        when(fleetService.findDistinctByOrganizationIdAndName(any(), any())).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertEquals(Optional.of(fleet), result);
        verify(fleetService).findDistinctByOrganizationIdAndName(any(), any());
    }

    @Test
    public void shouldGetFleetData() {
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        ResponseEntity result = fleetServiceController.getFleetData(request);

        assertEquals(responseEntity, result);
        verify(fleetService).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOperator() {
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        when(fleetService.getByOrganizationId(any())).thenReturn(fleets);

        List<Fleet> result = fleetServiceController.getFleetsForCalendar(request);

        assertEquals(fleets, result);
        verify(fleetService).getByOrganizationId(any());
    }
}
