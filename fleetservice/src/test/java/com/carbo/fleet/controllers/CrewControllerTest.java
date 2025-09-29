
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnCrewDisplayObjectWhenGetAllCrew() {
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "mockPrincipal");
        Mockito.when(crewService.findAll(anyString(), any(Integer.class), any(Integer.class))).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertEquals(crewDisplayObject, result);
        Mockito.verify(crewService).findAll(anyString(), any(Integer.class), any(Integer.class));
    }

    @Test
    public void shouldReturnCrewDtoWhenGetCrew() {
        CrewDto crewDto = new CrewDto();
        Mockito.when(crewService.findById(anyString())).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, "1");

        assertEquals(crewDto, result);
        Mockito.verify(crewService).findById("1");
    }

    @Test
    public void shouldCreateCrewAndReturn201WhenValidCrewDto() {
        CrewDto crewDto = new CrewDto();
        Crew crew = new Crew();
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "mockPrincipal");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(crew);
        crewDto.setId("1");
        
        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());
        Mockito.verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturn409WhenCrewAlreadyExists() {
        CrewDto crewDto = new CrewDto();
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "mockPrincipal");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);
        crewDto.setId("1");
        
        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        Mockito.verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnCrewDtoWhenUpdateCrew() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "mockPrincipal");
        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        Mockito.when(crewService.findById(anyString())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, result);
        Mockito.verify(crewService).findById("1");
    }

    @Test
    public void shouldDeleteCrewWhenIdProvided() {
        crewController.deleteCrew("1");
        
        Mockito.verify(crewService).deleteCrew("1");
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenGetAllCrewByFleet() {
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "mockPrincipal");
        Mockito.when(crewService.findAllByFleet(anyString(), anyString(), any(Integer.class), any(Integer.class)))
                .thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");

        assertEquals(crewDisplayObject, result);
        Mockito.verify(crewService).findAllByFleet(anyString(), anyString(), any(Integer.class), any(Integer.class));
    }
}
