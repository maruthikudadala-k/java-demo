
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
import java.util.Arrays;
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
        Fleet fleet1 = new Fleet();
        fleet1.setId("1");

        Fleet fleet2 = new Fleet();
        fleet2.setId("2");

        when(fleetService.getByOrganizationId("orgId")).thenReturn(Arrays.asList(fleet1, fleet2));

        var result = fleetServiceController.getFleets(request);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(fleetService).getByOrganizationId("orgId");
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        Fleet fleet = new Fleet();
        fleet.setId("1");
        
        when(fleetService.getFleet("1")).thenReturn(Optional.of(fleet));

        var result = fleetServiceController.getFleet("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(fleetService).getFleet("1");
    }

    @Test
    public void shouldCallUpdateFleetWhenUpdateFleetIsCalled() {
        Fleet fleet = new Fleet();
        fleet.setId("1");

        fleetServiceController.updateFleet("1", fleet);

        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldCallSaveFleetWhenSaveFleetIsCalled() {
        Fleet fleet = new Fleet();

        fleetServiceController.saveFleet(fleet);

        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldCallDeleteFleetWhenDeleteFleetIsCalled() {
        fleetServiceController.deleteFleet("1");

        verify(fleetService).deleteFleet("1");
    }

    @Test
    public void shouldReturnOptionalFleetWhenFindDistinctByOrganizationIdAndNameIsCalled() {
        Fleet fleet = new Fleet();
        fleet.setId("1");

        when(fleetService.findDistinctByOrganizationIdAndName("orgId", "fleetName"))
                .thenReturn(Optional.of(fleet));

        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, "fleetName");

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        verify(fleetService).findDistinctByOrganizationIdAndName("orgId", "fleetName");
    }

    @Test
    public void shouldReturnResponseEntityWhenGetFleetDataIsCalled() {
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        var result = fleetServiceController.getFleetData(request);

        assertEquals(responseEntity, result);
        verify(fleetService).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsWhenGetFleetsForCalendarIsCalled() {
        Fleet fleet1 = new Fleet();
        fleet1.setId("1");

        Fleet fleet2 = new Fleet();
        fleet2.setId("2");

        when(fleetService.getByOrganizationId("orgId")).thenReturn(Arrays.asList(fleet1, fleet2));

        var result = fleetServiceController.getFleetsForCalendar(request);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(fleetService).getByOrganizationId("orgId");
    }
}
