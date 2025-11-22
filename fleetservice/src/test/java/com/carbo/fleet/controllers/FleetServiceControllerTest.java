
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class FleetServiceControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnFleetsWhenGetFleets() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setName("Fleet1");
        List<Fleet> fleets = Collections.singletonList(fleet);

        Mockito.when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        Mockito.when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetServiceController.getFleets(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fleet1", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetWhenGetFleetById() {
        String fleetId = "fleetId123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Fleet1");

        Mockito.when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet(fleetId);

        assertNotNull(result);
        assertEquals(fleetId, result.getId());
        assertEquals("Fleet1", result.getName());
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleet() {
        String fleetId = "fleetId123";
        Fleet fleet = new Fleet();

        fleetServiceController.updateFleet(fleetId, fleet);

        Mockito.verify(fleetService).updateFleet(eq(fleet));
    }

    @Test
    public void shouldSaveFleetWhenSaveFleet() {
        Fleet fleet = new Fleet();

        fleetServiceController.saveFleet(fleet);

        Mockito.verify(fleetService).saveFleet(eq(fleet));
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleet() {
        String fleetId = "fleetId123";

        fleetServiceController.deleteFleet(fleetId);

        Mockito.verify(fleetService).deleteFleet(eq(fleetId));
    }

    @Test
    public void shouldReturnOptionalFleetWhenFindDistinctByOrganizationIdAndName() {
        String organizationId = "org123";
        String name = "Fleet1";
        Fleet fleet = new Fleet();
        fleet.setName(name);
        Optional<Fleet> optionalFleet = Optional.of(fleet);

        Mockito.when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        Mockito.when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(optionalFleet);

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
    }

    @Test
    public void shouldReturnResponseEntityWhenGetFleetData() {
        ResponseEntity responseEntity = ResponseEntity.ok().build();

        Mockito.when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        ResponseEntity result = fleetServiceController.getFleetData(request);

        assertNotNull(result);
        assertEquals(responseEntity.getStatusCode(), result.getStatusCode());
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenGetFleetsForCalendar() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setName("Fleet1");
        List<Fleet> fleets = Collections.singletonList(fleet);

        Mockito.when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        Mockito.when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetServiceController.getFleetsForCalendar(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fleet1", result.get(0).getName());
    }
}
