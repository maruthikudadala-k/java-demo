
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

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
    public void shouldReturnFleetsWhenGetFleetsCalled() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        var result = fleetServiceController.getFleets(request);

        assertNotNull(result);
        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetCalled() {
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
    public void shouldUpdateFleetWhenUpdateFleetCalled() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);

        fleetServiceController.updateFleet(fleetId, fleet);

        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetCalled() {
        Fleet fleet = new Fleet();

        fleetServiceController.saveFleet(fleet);

        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetCalled() {
        String fleetId = "fleet123";

        fleetServiceController.deleteFleet(fleetId);

        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetWhenFindDistinctByOrganizationIdAndNameCalled() {
        String organizationId = "org123";
        String name = "Fleet A";
        Fleet fleet = new Fleet();
        fleet.setName(name);
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
        verify(fleetService).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataCalled() {
        when(fleetService.getFleetData(request)).thenReturn(null); // Assuming a mock response

        var result = fleetServiceController.getFleetData(request);

        assertNull(result);
        verify(fleetService).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenGetFleetsForCalendarCalled() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        var result = fleetServiceController.getFleetsForCalendar(request);

        assertNotNull(result);
        verify(fleetService).getByOrganizationId(organizationId);
    }
}
