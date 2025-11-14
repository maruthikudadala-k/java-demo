
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
import org.springframework.mock.web.MockHttpServletRequest;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnCrewDisplayObjectWhenGetAllCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "principal");
        
        CrewDisplayObject expectedObject = new CrewDisplayObject();
        when(crewService.findAll(anyString(), anyInt(), anyInt())).thenReturn(expectedObject);

        CrewDisplayObject actualObject = crewController.getAllCrew(request, 0, 10);
        
        assertEquals(expectedObject, actualObject);
        verify(crewService).findAll(anyString(), eq(0), eq(10));
    }

    @Test
    public void shouldReturnCrewDtoWhenGetCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "crewId";
        CrewDto expectedCrewDto = new CrewDto();
        when(crewService.findById(anyString())).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.getCrew(request, crewId);

        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService).findById(eq(crewId));
    }

    @Test
    public void shouldCreateCrewAndReturnResponseEntityWhenCreateCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Crew 1");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleetId");
        
        Crew createdCrew = new Crew();
        when(crewService.saveCrew(any())).thenReturn(createdCrew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCrew, response.getBody());
        verify(crewService).saveCrew(any());
    }

    @Test
    public void shouldReturnConflictResponseWhenCreateCrewFails() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Crew 1");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleetId");

        when(crewService.saveCrew(any())).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        verify(crewService).saveCrew(any());
    }

    @Test
    public void shouldReturnCrewDtoWhenUpdateCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        CrewDto expectedCrewDto = new CrewDto();
        when(crewService.updateCrew(any())).thenReturn(true);
        when(crewService.findById(anyString())).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService).updateCrew(any());
        verify(crewService).findById(eq(crewDto.getId()));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrew() {
        String crewId = "crewId";

        crewController.deleteCrew(crewId);

        verify(crewService).deleteCrew(eq(crewId));
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenGetAllCrewByFleet() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "principal");
        
        CrewDisplayObject expectedObject = new CrewDisplayObject();
        when(crewService.findAllByFleet(anyString(), anyString(), anyInt(), anyInt())).thenReturn(expectedObject);

        CrewDisplayObject actualObject = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");
        
        assertEquals(expectedObject, actualObject);
        verify(crewService).findAllByFleet(anyString(), eq("fleetName"), eq(0), eq(10));
    }
}
