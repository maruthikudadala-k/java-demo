
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FleetServiceControllerTest {
    
    @Mock
    private FleetService fleetService;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnAllFleetsWhenOrganizationIsOperator() {
        when(request.getUserPrincipal()).thenReturn(() -> "principal");
        when(fleetService.getByOrganizationId("organizationId")).thenReturn(Collections.emptyList());

        fleetServiceController.getFleets(request);

        verify(fleetService).getByOrganizationId("organizationId");
    }

    @Test
    public void shouldReturnFleetById() {
        Fleet fleet = new Fleet();
        fleet.setId("1");
        when(fleetService.getFleet("1")).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet("1");

        verify(fleetService).getFleet("1");
        assert result.equals(fleet);
    }

    @Test
    public void shouldUpdateFleet() {
        Fleet fleet = new Fleet();
        fleet.setId("1");

        fleetServiceController.updateFleet("1", fleet);

        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();

        fleetServiceController.saveFleet(fleet);

        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        fleetServiceController.deleteFleet("1");

        verify(fleetService).deleteFleet("1");
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        String name = "Test Fleet";
        when(fleetService.findDistinctByOrganizationIdAndName(any(), any())).thenReturn(Optional.empty());

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        verify(fleetService).findDistinctByOrganizationIdAndName(any(), eq(name));
        assert result.isEmpty();
    }

    @Test
    public void shouldGetFleetData() {
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = fleetServiceController.getFleetData(request);

        verify(fleetService).getFleetData(request);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    public void shouldGetFleetsForCalendarWhenOrganizationIsOperator() {
        when(request.getUserPrincipal()).thenReturn(() -> "principal");
        when(fleetService.getByOrganizationId("organizationId")).thenReturn(Collections.emptyList());

        fleetServiceController.getFleetsForCalendar(request);

        verify(fleetService).getByOrganizationId("organizationId");
    }
}
