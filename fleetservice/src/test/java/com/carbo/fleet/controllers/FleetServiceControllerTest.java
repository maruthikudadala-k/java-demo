
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);

        fleetServiceController.getFleets(request);

        verify(fleetService).getByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet(fleetId);

        assertEquals(fleet, result);
        verify(fleetService).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        
        fleetServiceController.updateFleet(fleetId, fleet);

        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        Fleet fleet = new Fleet();
        
        fleetServiceController.saveFleet(fleet);

        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        String fleetId = "fleet123";

        fleetServiceController.deleteFleet(fleetId);

        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetWhenFindDistinctByOrganizationIdAndNameIsCalled() {
        String name = "FleetName";
        String organizationId = "org123";
        Fleet fleet = new Fleet();

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertEquals(fleet, result.get());
        verify(fleetService).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        ResponseEntity result = fleetServiceController.getFleetData(request);

        assertEquals(responseEntity, result);
        verify(fleetService).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenGetFleetsForCalendarIsCalled() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);

        fleetServiceController.getFleetsForCalendar(request);

        verify(fleetService).getByOrganizationId(organizationId);
    }
}
