
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

import static org.junit.jupiter.api.Assertions.*;
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
    public void shouldReturnAllFleetsWhenOperatorType() {
        when(request.getHeader("organizationType")).thenReturn("OPERATOR");
        when(request.getHeader("organizationId")).thenReturn("orgId");

        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet1");
        fleet.setOrganizationId("orgId");

        when(fleetService.getByOrganizationId("orgId")).thenReturn(Collections.singletonList(fleet));

        var result = fleetServiceController.getFleets(request);

        assertEquals(1, result.size());
        assertEquals("Fleet1", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetById() {
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet1");

        when(fleetService.getFleet("1")).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet("1");

        assertNotNull(result);
        assertEquals("Fleet1", result.getName());
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        fleet.setName("Fleet1");

        fleetServiceController.saveFleet(fleet);

        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldUpdateFleet() {
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("UpdatedFleet");

        fleetServiceController.updateFleet("1", fleet);

        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        fleetServiceController.deleteFleet("1");

        verify(fleetService, times(1)).deleteFleet("1");
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        when(request.getHeader("organizationId")).thenReturn("orgId");
        when(fleetService.findDistinctByOrganizationIdAndName("orgId", "Fleet1")).thenReturn(Optional.of(new Fleet()));

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, "Fleet1");

        assertTrue(result.isPresent());
        verify(fleetService, times(1)).findDistinctByOrganizationIdAndName("orgId", "Fleet1");
    }

    @Test
    public void shouldGetFleetData() {
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = fleetServiceController.getFleetData(request);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void shouldReturnFleetsForCalendar() {
        when(request.getHeader("organizationId")).thenReturn("orgId");
        when(request.getHeader("organizationType")).thenReturn("OPERATOR");

        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Fleet1");

        when(fleetService.getByOrganizationId("orgId")).thenReturn(Collections.singletonList(fleet));

        var result = fleetServiceController.getFleetsForCalendar(request);

        assertEquals(1, result.size());
        assertEquals("Fleet1", result.get(0).getName());
    }
}
