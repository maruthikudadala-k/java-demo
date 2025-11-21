
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
    public void shouldReturnFleetsWhenGetFleetsCalled() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Test Fleet");

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        var result = fleetServiceController.getFleets(request);

        assertEquals(1, result.size());
        assertEquals("Test Fleet", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetWhenGetFleetByIdCalled() {
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Test Fleet");

        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        var result = fleetServiceController.getFleet(fleetId);

        assertEquals(fleetId, result.getId());
        assertEquals("Test Fleet", result.getName());
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetCalled() {
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Updated Fleet");

        fleetServiceController.updateFleet(fleetId, fleet);

        Mockito.verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetCalled() {
        Fleet fleet = new Fleet();
        fleet.setName("New Fleet");

        fleetServiceController.saveFleet(fleet);

        Mockito.verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetCalled() {
        String fleetId = "fleet1";

        fleetServiceController.deleteFleet(fleetId);

        Mockito.verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnOptionalFleetWhenFindDistinctByOrganizationIdAndNameCalled() {
        String organizationId = "org123";
        String name = "Test Fleet";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName(name);

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertEquals(fleet.getId(), result.get().getId());
        assertEquals(name, result.get().getName());
    }

    @Test
    public void shouldReturnResponseEntityWhenGetFleetDataCalled() {
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = fleetServiceController.getFleetData(request);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenGetFleetsForCalendarCalled() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Calendar Fleet");

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        var result = fleetServiceController.getFleetsForCalendar(request);

        assertEquals(1, result.size());
        assertEquals("Calendar Fleet", result.get(0).getName());
    }
}
