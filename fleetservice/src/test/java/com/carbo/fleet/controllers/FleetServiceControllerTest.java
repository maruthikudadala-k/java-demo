
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnFleetsWhenValidRequest() {
        String organizationId = "testOrgId";
        String organizationType = "OPERATOR";

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);

        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Test Fleet");

        when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(Collections.singletonList(fleet));
        
        var result = fleetServiceController.getFleets(request);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Fleet", result.get(0).getName());
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Test Fleet");

        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet(fleetId);

        assertNotNull(result);
        assertEquals(fleetId, result.getId());
        assertEquals("Test Fleet", result.getName());
    }

    @Test
    public void shouldUpdateFleet() {
        String fleetId = "1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Updated Fleet");

        fleetServiceController.updateFleet(fleetId, fleet);

        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("New Fleet");

        fleetServiceController.saveFleet(fleet);

        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        String fleetId = "1";

        fleetServiceController.deleteFleet(fleetId);

        verify(fleetService, times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctFleet() {
        String name = "Unique Fleet";
        String organizationId = "orgId";
        
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(new Fleet()));

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertTrue(result.isPresent());
    }

    @Test
    public void shouldGetFleetData() {
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = fleetServiceController.getFleetData(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void shouldReturnFleetsForCalendar() {
        String organizationId = "testOrgId";
        String organizationType = "OPERATOR";

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);

        Fleet fleet = new Fleet();
        fleet.setId("1");
        fleet.setName("Calendar Fleet");

        when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(Collections.singletonList(fleet));

        var result = fleetServiceController.getFleetsForCalendar(request);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Calendar Fleet", result.get(0).getName());
    }
}
