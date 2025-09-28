
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CrewController crewController;

    @Test
    void shouldReturnCrewDisplayObjectWhenGetAllCrew() {
        String organizationId = "org123";
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.findAll(organizationId, 0, 10)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertEquals(crewDisplayObject, result);
        verify(crewService).findAll(organizationId, 0, 10);
    }

    @Test
    void shouldReturnCrewDtoWhenGetCrew() {
        String crewId = "crew123";
        CrewDto crewDto = new CrewDto();
        
        when(crewService.findById(crewId)).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, crewId);

        assertEquals(crewDto, result);
        verify(crewService).findById(crewId);
    }

    @Test
    void shouldCreateCrewAndReturnCreatedResponse() {
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("org123");
        Crew crew = new Crew();
        
        when(request.getUserPrincipal()).thenReturn(() -> "org123");
        when(crewService.saveCrew(crewDto)).thenReturn(crew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    void shouldReturnConflictWhenCrewAlreadyExists() {
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("org123");
        
        when(request.getUserPrincipal()).thenReturn(() -> "org123");
        when(crewService.saveCrew(crewDto)).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    void shouldReturnCrewDtoWhenUpdatePersonnel() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setOrganizationId("org123");
        
        when(request.getUserPrincipal()).thenReturn(() -> "org123");
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);
        when(crewService.updateCrew(crewDto)).thenReturn(true);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, result);
        verify(crewService).updateCrew(crewDto);
        verify(crewService).findById(crewDto.getId());
    }

    @Test
    void shouldDeleteCrewWhenDeleteCrew() {
        String crewId = "crew123";

        crewController.deleteCrew(crewId);

        verify(crewService).deleteCrew(crewId);
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenGetAllCrewByFleet() {
        String organizationId = "org123";
        String fleetName = "fleetA";
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.findAllByFleet(organizationId, fleetName, 0, 10)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        assertEquals(crewDisplayObject, result);
        verify(crewService).findAllByFleet(organizationId, fleetName, 0, 10);
    }
}
