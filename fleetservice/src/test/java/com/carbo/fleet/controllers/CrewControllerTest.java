
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
    public void shouldReturnAllCrewWhenGetAllCrew() {
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(crewService.findAll(anyString(), any(Integer.class), any(Integer.class)))
                .thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertEquals(crewDisplayObject, result);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewById() {
        CrewDto crewDto = new CrewDto();
        Mockito.when(crewService.findById(anyString())).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, "1");

        assertEquals(crewDto, result);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrew() {
        CrewDto crewDto = CrewDto.builder().name("Test Crew").build();
        Crew crew = new Crew();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(crew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        CrewDto crewDto = CrewDto.builder().name("Test Crew").build();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrew() {
        CrewDto crewDto = CrewDto.builder().id("1").name("Updated Crew").build();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        Mockito.when(crewService.findById(anyString())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, result);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrew() {
        Mockito.doNothing().when(crewService).deleteCrew(anyString());

        crewController.deleteCrew("1");

        Mockito.verify(crewService, Mockito.times(1)).deleteCrew("1");
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleet() {
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(crewService.findAllByFleet(anyString(), anyString(), any(Integer.class), any(Integer.class)))
                .thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");

        assertEquals(crewDisplayObject, result);
    }
}
