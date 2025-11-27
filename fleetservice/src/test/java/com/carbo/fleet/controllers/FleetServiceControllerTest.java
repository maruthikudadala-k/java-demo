
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        List<Fleet> result = fleetServiceController.getFleets(request);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Fleet One", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetById() {
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");

        when(fleetService.getFleet("fleet1")).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet("fleet1");

        assertNotNull(result);
        assertEquals("Fleet One", result.getName());
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");

        fleetServiceController.saveFleet(fleet);

        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldUpdateFleet() {
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");

        fleetServiceController.updateFleet("fleet1", fleet);

        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        fleetServiceController.deleteFleet("fleet1");

        verify(fleetService, times(1)).deleteFleet("fleet1");
    }

    @Test
    public void shouldFindDistinctFleet() {
        String organizationId = "org123";
        String name = "Fleet One";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertTrue(result.isPresent());
        assertEquals("Fleet One", result.get().getName());
    }

    @Test
    public void shouldGetFleetData() {
        ResponseEntity responseEntity = ResponseEntity.ok(Collections.emptyMap());
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        ResponseEntity result = fleetServiceController.getFleetData(request);

        assertEquals(responseEntity, result);
    }

    @Test
    public void shouldGetFleetsForCalendar() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet One");

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        List<Fleet> result = fleetServiceController.getFleetsForCalendar(request);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Fleet One", result.get(0).getName());
    }
}
