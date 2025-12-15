
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
    public void shouldReturnFleetsWhenGetFleetsIsCalled() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet 1");

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        var result = fleetServiceController.getFleets(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fleet 1", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Fleet 1");

        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        var result = fleetServiceController.getFleet(fleetId);

        assertNotNull(result);
        assertEquals(fleetId, result.getId());
        assertEquals("Fleet 1", result.getName());
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Fleet 1");

        fleetServiceController.updateFleet(fleetId, fleet);

        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        Fleet fleet = new Fleet();
        fleet.setName("Fleet 1");

        fleetServiceController.saveFleet(fleet);

        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        String fleetId = "fleet1";

        fleetServiceController.deleteFleet(fleetId);

        verify(fleetService, times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetWhenFindDistinctByOrganizationIdAndNameIsCalled() {
        String organizationId = "org123";
        String name = "Fleet 1";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName(name);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        var result = fleetServiceController.getFleetData(request);

        assertEquals(responseEntity, result);
        verify(fleetService, times(1)).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenGetFleetsForCalendarIsCalled() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet 1");

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.singletonList(fleet));

        var result = fleetServiceController.getFleetsForCalendar(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fleet 1", result.get(0).getName());
    }
}
