
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class FleetServiceControllerTest {

    @Mock
    private FleetService fleetService;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Mock
    private HttpServletRequest request;

    @Test
    void shouldReturnFleetsWhenOrganizationIsOperator() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        
        Mockito.when(request.getAttribute("organizationId")).thenReturn(organizationId);
        Mockito.when(request.getAttribute("organizationType")).thenReturn(organizationType);
        
        Fleet fleet = new Fleet();
        fleet.setId("fleetId");
        fleet.setName("Test Fleet");
        
        Mockito.when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(Collections.singletonList(fleet));

        List<Fleet> fleets = fleetServiceController.getFleets(request);

        assertNotNull(fleets);
        assertEquals(1, fleets.size());
        assertEquals("Test Fleet", fleets.get(0).getName());
    }

    @Test
    void shouldReturnFleetById() {
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        fleet.setName("Test Fleet");

        Mockito.when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet(fleetId);

        assertNotNull(result);
        assertEquals(fleetId, result.getId());
        assertEquals("Test Fleet", result.getName());
    }

    @Test
    void shouldUpdateFleet() {
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        
        fleetServiceController.updateFleet(fleetId, fleet);

        Mockito.verify(fleetService).updateFleet(fleet);
    }

    @Test
    void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        
        fleetServiceController.saveFleet(fleet);

        Mockito.verify(fleetService).saveFleet(fleet);
    }

    @Test
    void shouldDeleteFleet() {
        String fleetId = "fleetId";
        
        fleetServiceController.deleteFleet(fleetId);

        Mockito.verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    void shouldFindDistinctByOrganizationIdAndName() {
        String organizationId = "org123";
        String name = "Test Fleet";
        Mockito.when(request.getAttribute("organizationId")).thenReturn(organizationId);
        
        Optional<Fleet> fleet = Optional.of(new Fleet());
        Mockito.when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(fleet);

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldGetFleetData() {
        ResponseEntity responseEntity = ResponseEntity.ok(Collections.emptyMap());
        Mockito.when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        ResponseEntity result = fleetServiceController.getFleetData(request);

        assertNotNull(result);
        assertEquals(responseEntity, result);
    }

    @Test
    void shouldReturnFleetsForCalendarWhenOperator() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        
        Mockito.when(request.getAttribute("organizationId")).thenReturn(organizationId);
        Mockito.when(request.getAttribute("organizationType")).thenReturn(organizationType);
        
        Fleet fleet = new Fleet();
        fleet.setId("fleetId");
        fleet.setName("Test Fleet");
        
        Mockito.when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(Collections.singletonList(fleet));

        List<Fleet> fleets = fleetServiceController.getFleetsForCalendar(request);

        assertNotNull(fleets);
        assertEquals(1, fleets.size());
        assertEquals("Test Fleet", fleets.get(0).getName());
    }
}
