
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FleetServiceControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Test
    public void shouldReturnFleetsWhenOrganizationIsOperator() {
        HttpServletRequest request = new MockHttpServletRequest();
        Fleet fleet = new Fleet();
        fleet.setName("Test Fleet");
        when(fleetService.getByOrganizationId(anyString())).thenReturn(Collections.singletonList(fleet));

        var result = fleetServiceController.getFleets(request);

        assertEquals(1, result.size());
        assertEquals("Test Fleet", result.get(0).getName());
        verify(fleetService, times(1)).getByOrganizationId(anyString());
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet(fleetId);

        assertEquals(fleetId, result.getId());
        verify(fleetService, times(1)).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleet() {
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        
        fleetServiceController.updateFleet(fleetId, fleet);

        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        fleetServiceController.saveFleet(fleet);

        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        String fleetId = "fleetId";
        
        fleetServiceController.deleteFleet(fleetId);

        verify(fleetService, times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        HttpServletRequest request = new MockHttpServletRequest();
        String name = "Test Fleet";
        Fleet fleet = new Fleet();
        fleet.setName(name);
        when(fleetService.findDistinctByOrganizationIdAndName(any(), anyString())).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertEquals(name, result.get().getName());
        verify(fleetService, times(1)).findDistinctByOrganizationIdAndName(any(), anyString());
    }

    @Test
    public void shouldReturnFleetData() {
        HttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        ResponseEntity result = fleetServiceController.getFleetData(request);

        assertEquals(responseEntity, result);
        verify(fleetService, times(1)).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOperator() {
        HttpServletRequest request = new MockHttpServletRequest();
        Fleet fleet = new Fleet();
        fleet.setName("Test Fleet");
        when(fleetService.getByOrganizationId(anyString())).thenReturn(Collections.singletonList(fleet));

        var result = fleetServiceController.getFleetsForCalendar(request);

        assertEquals(1, result.size());
        assertEquals("Test Fleet", result.get(0).getName());
        verify(fleetService, times(1)).getByOrganizationId(anyString());
    }
}
