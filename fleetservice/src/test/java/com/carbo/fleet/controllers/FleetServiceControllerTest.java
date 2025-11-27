
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.Job;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
    public void shouldReturnAllFleetsWhenOperator() {
        String organizationId = "orgId";
        String organizationType = "OPERATOR";
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);
        
        List<Job> jobs = new ArrayList<>();
        Job job = new Job();
        job.setFleet("fleet1");
        job.setOrganizationId(organizationId);
        jobs.add(job);
        
        when(mongoTemplate.find(any(), Mockito.eq(Job.class))).thenReturn(jobs);
        
        Fleet fleet = new Fleet();
        fleet.setName("fleet1");
        List<Fleet> fleetList = new ArrayList<>();
        fleetList.add(fleet);
        
        when(mongoTemplate.find(any(), Mockito.eq(Fleet.class))).thenReturn(fleetList);
        
        List<Fleet> result = fleetServiceController.getFleets(request);
        
        Mockito.verify(mongoTemplate).find(any(), Mockito.eq(Job.class));
        Mockito.verify(mongoTemplate).find(any(), Mockito.eq(Fleet.class));
        assert result.size() == 1;
        assert result.get(0).getName().equals("fleet1");
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));
        
        Fleet result = fleetServiceController.getFleet(fleetId);
        
        Mockito.verify(fleetService).getFleet(fleetId);
        assert result.getId().equals(fleetId);
    }

    @Test
    public void shouldUpdateFleet() {
        String fleetId = "fleetId";
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
        String fleetId = "fleetId";
        
        fleetServiceController.deleteFleet(fleetId);
        
        Mockito.verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        String name = "fleetName";
        String organizationId = "orgId";
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        
        Fleet fleet = new Fleet();
        when(fleetService.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));
        
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);
        
        Mockito.verify(fleetService).findDistinctByOrganizationIdAndName(organizationId, name);
        assert result.isPresent();
    }

    @Test
    public void shouldGetFleetData() {
        ResponseEntity responseEntity = ResponseEntity.ok().build();
        when(fleetService.getFleetData(request)).thenReturn(responseEntity);
        
        ResponseEntity result = fleetServiceController.getFleetData(request);
        
        Mockito.verify(fleetService).getFleetData(request);
        assert result.equals(responseEntity);
    }

    @Test
    public void shouldGetFleetsForCalendar() {
        String organizationId = "orgId";
        String organizationType = "OPERATOR";
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(request.getAttribute("organizationType")).thenReturn(organizationType);
        
        List<Job> jobs = new ArrayList<>();
        Job job = new Job();
        job.setFleet("fleet1");
        job.setOrganizationId(organizationId);
        jobs.add(job);
        
        when(mongoTemplate.find(any(), Mockito.eq(Job.class))).thenReturn(jobs);
        
        Fleet fleet = new Fleet();
        fleet.setName("fleet1");
        List<Fleet> fleetList = new ArrayList<>();
        fleetList.add(fleet);
        
        when(mongoTemplate.find(any(), Mockito.eq(Fleet.class))).thenReturn(fleetList);
        
        List<Fleet> result = fleetServiceController.getFleetsForCalendar(request);
        
        Mockito.verify(mongoTemplate).find(any(), Mockito.eq(Job.class));
        Mockito.verify(mongoTemplate).find(any(), Mockito.eq(Fleet.class));
        assert result.size() == 1;
        assert result.get(0).getName().equals("fleet1");
    }
}
