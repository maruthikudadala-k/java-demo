
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.Job;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
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
    public void shouldReturnFleetsWhenOrganizationTypeIsOperator() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        List<Job> jobs = new ArrayList<>();
        Job job = new Job();
        job.setFleet("Fleet1");
        job.setOrganizationId(organizationId);
        jobs.add(job);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(mongoTemplate.find(any(), eq(Job.class))).thenReturn(jobs);
        when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(new ArrayList<>());

        List<Fleet> fleets = fleetServiceController.getFleets(request);

        assertNotNull(fleets);
        verify(mongoTemplate, times(1)).find(any(), eq(Job.class));
        verify(mongoTemplate, times(1)).find(any(), eq(Fleet.class));
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        Fleet result = fleetServiceController.getFleet(fleetId);

        assertNotNull(result);
        assertEquals(fleetId, result.getId());
        verify(fleetService, times(1)).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleet() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);

        fleetServiceController.updateFleet(fleetId, fleet);

        verify(fleetService, times(1)).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();

        fleetServiceController.saveFleet(fleet);

        verify(fleetService, times(1)).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        String fleetId = "fleet123";

        fleetServiceController.deleteFleet(fleetId);

        verify(fleetService, times(1)).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        String name = "Fleet1";
        String organizationId = "org123";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.empty());

        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        assertTrue(result.isEmpty());
        verify(fleetService, times(1)).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldGetFleetData() {
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);

        ResponseEntity result = fleetServiceController.getFleetData(request);

        assertEquals(responseEntity, result);
        verify(fleetService, times(1)).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendar() {
        String organizationId = "org123";
        String organizationType = "OPERATOR";
        List<Job> jobs = new ArrayList<>();
        Job job = new Job();
        job.setFleet("Fleet1");
        job.setOrganizationId(organizationId);
        jobs.add(job);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(mongoTemplate.find(any(), eq(Job.class))).thenReturn(jobs);
        when(mongoTemplate.find(any(), eq(Fleet.class))).thenReturn(new ArrayList<>());

        List<Fleet> fleets = fleetServiceController.getFleetsForCalendar(request);

        assertNotNull(fleets);
        verify(mongoTemplate, times(1)).find(any(), eq(Job.class));
        verify(mongoTemplate, times(1)).find(any(), eq(Fleet.class));
    }
}
