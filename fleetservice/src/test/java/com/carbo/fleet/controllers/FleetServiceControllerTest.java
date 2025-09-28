
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    public void shouldReturnFleetsWhenOperator() {
        String organizationId = "org-123";
        String organizationType = "OPERATOR";

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        var result = fleetServiceController.getFleets(request);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleet-123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet(fleetId);
        assertEquals(fleetId, result.getId());
    }

    @Test
    public void shouldUpdateFleet() {
        String fleetId = "fleet-123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);

        fleetServiceController.updateFleet(fleetId, fleet);

        Mockito.verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();

        fleetServiceController.saveFleet(fleet);

        Mockito.verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        String fleetId = "fleet-123";

        fleetServiceController.deleteFleet(fleetId);

        Mockito.verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctFleetByOrganizationIdAndName() {
        String name = "Fleet A";
        Fleet fleet = new Fleet();
        fleet.setName(name);
        when(fleetService.findDistinctByOrganizationIdAndName(any(), any())).thenReturn(Optional.of(fleet));

        var result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);
        assertEquals(name, result.get().getName());
    }
}
