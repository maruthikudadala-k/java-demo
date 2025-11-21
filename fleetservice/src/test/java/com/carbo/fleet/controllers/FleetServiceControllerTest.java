
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
        String organizationId = "org123";
        Fleet fleet = new Fleet();
        fleet.setId("fleet123");
        fleet.setName("Fleet A");

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        var result = fleetServiceController.getFleets(request);

        assertEquals(1, result.size());
        assertEquals("Fleet A", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Fleet A");

        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        var result = fleetServiceController.getFleet(fleetId);

        assertEquals("Fleet A", result.getName());
        assertEquals(fleetId, result.getId());
    }

    @Test
    public void shouldUpdateFleet() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Updated Fleet");

        fleetServiceController.updateFleet(fleetId, fleet);

        Mockito.verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        fleet.setName("New Fleet");

        fleetServiceController.saveFleet(fleet);

        Mockito.verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        String fleetId = "fleet123";

        fleetServiceController.deleteFleet(fleetId);

        Mockito.verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        String organizationId = "org123";
        String name = "Fleet A";
        Fleet fleet = new Fleet();
        fleet.setId("fleet123");
        fleet.setName(name);

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertEquals(name, result.get().getName());
    }

    @Test
    public void shouldReturnFleetData() {
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok("Some Data"));

        ResponseEntity<?> result = fleetServiceController.getFleetData(request);

        assertEquals("Some Data", result.getBody());
    }

    @Test
    public void shouldReturnFleetsForCalendar() {
        String organizationId = "org123";

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        var result = fleetServiceController.getFleetsForCalendar(request);

        assertEquals(0, result.size());
    }
}
