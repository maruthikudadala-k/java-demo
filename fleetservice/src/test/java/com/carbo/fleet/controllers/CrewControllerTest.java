
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.services.CrewService;
import com.carbo.fleet.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(crewService.findAll(organizationId, offSet, limit)).thenReturn(expectedCrewDisplayObject);
        
        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, offSet, limit);
        
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        String crewId = "crew123";
        CrewDto expectedCrewDto = new CrewDto();
        
        when(crewService.findById(crewId)).thenReturn(expectedCrewDto);
        
        CrewDto actualCrewDto = crewController.getCrew(request, crewId);
        
        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("John Doe");
        crewDto.setJobPattern("Pattern A");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2022");
        crewDto.setFleetId("fleet123");
        
        String organizationId = "org123";
        crewDto.setOrganizationId(organizationId);
        Crew expectedCrew = new Crew();

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(crewService.saveCrew(crewDto)).thenReturn(expectedCrew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedCrew, response.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCreatingCrewFails() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("John Doe");
        crewDto.setJobPattern("Pattern A");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2022");
        crewDto.setFleetId("fleet123");
        
        String organizationId = "org123";
        crewDto.setOrganizationId(organizationId);
        
        Map<String, String> expectedError = new HashMap<>();
        expectedError.put("errorMessage", Constants.CREW_ALREADY_EXISTS);

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(crewService.saveCrew(crewDto)).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedError, response.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("John Doe");
        crewDto.setJobPattern("Pattern A");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2022");
        crewDto.setFleetId("fleet123");
        
        String organizationId = "org123";
        crewDto.setOrganizationId(organizationId);
        CrewDto expectedCrewDto = new CrewDto();

        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);
        
        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService).updateCrew(crewDto);
        verify(crewService).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "crew123";
        
        crewController.deleteCrew(crewId);
        
        verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        String organizationId = "org123";
        String fleetName = "Fleet A";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        
        when(request.getAttribute("organizationId")).thenReturn(organizationId);
        when(crewService.findAllByFleet(organizationId, fleetName, offSet, limit)).thenReturn(expectedCrewDisplayObject);
        
        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, offSet, limit, fleetName);
        
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService).findAllByFleet(organizationId, fleetName, offSet, limit);
    }
}
