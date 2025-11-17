
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.services.CrewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> organizationId);

        CrewDisplayObject mockCrewDisplayObject = new CrewDisplayObject();
        Mockito.when(crewService.findAll(eq(organizationId), eq(0), eq(10))).thenReturn(mockCrewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);
        
        assertNotNull(result);
        Mockito.verify(crewService).findAll(eq(organizationId), eq(0), eq(10));
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        String crewId = "testCrewId";
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto mockCrewDto = new CrewDto();
        Mockito.when(crewService.findById(eq(crewId))).thenReturn(mockCrewDto);

        CrewDto result = crewController.getCrew(request, crewId);
        
        assertNotNull(result);
        Mockito.verify(crewService).findById(eq(crewId));
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> organizationId);
        
        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2022");
        crewDto.setFleetId("fleetId");
        
        Crew mockCrew = new Crew();
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(mockCrew);

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockCrew, responseEntity.getBody());
        Mockito.verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> organizationId);
        
        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2022");
        crewDto.setFleetId("fleetId");
        
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        Map<String, String> error = (Map<String, String>) responseEntity.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        Mockito.verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> organizationId);
        
        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Test Crew Updated");
        
        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        Mockito.when(crewService.findById(eq(crewDto.getId()))).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);
        
        assertNotNull(result);
        assertEquals("Test Crew Updated", result.getName());
        Mockito.verify(crewService).updateCrew(any(CrewDto.class));
        Mockito.verify(crewService).findById(eq(crewDto.getId()));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "testCrewId";
        
        crewController.deleteCrew(crewId);
        
        Mockito.verify(crewService).deleteCrew(eq(crewId));
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> organizationId);
        
        CrewDisplayObject mockCrewDisplayObject = new CrewDisplayObject();
        Mockito.when(crewService.findAllByFleet(eq(organizationId), eq("fleetName"), eq(0), eq(10)))
               .thenReturn(mockCrewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");
        
        assertNotNull(result);
        Mockito.verify(crewService).findAllByFleet(eq(organizationId), eq("fleetName"), eq(0), eq(10));
    }
}
