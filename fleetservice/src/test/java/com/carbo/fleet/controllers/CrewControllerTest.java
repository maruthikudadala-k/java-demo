
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

import static org.junit.jupiter.api.Assertions.*;
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
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.findAll(organizationId, 0, 10)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertEquals(crewDisplayObject, result);
        verify(crewService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        String id = "crew123";
        CrewDto crewDto = new CrewDto();
        when(crewService.findById(id)).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, id);

        assertEquals(crewDto, result);
        verify(crewService).findById(id);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("org123");
        Crew crew = new Crew();
        when(request.getUserPrincipal()).thenReturn(() -> crewDto.getOrganizationId());
        when(crewService.saveCrew(crewDto)).thenReturn(crew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("org123");
        when(request.getUserPrincipal()).thenReturn(() -> crewDto.getOrganizationId());
        when(crewService.saveCrew(crewDto)).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setOrganizationId("org123");
        when(request.getUserPrincipal()).thenReturn(() -> crewDto.getOrganizationId());
        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, result);
        verify(crewService).updateCrew(crewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String id = "crew123";

        crewController.deleteCrew(id);

        verify(crewService).deleteCrew(id);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        String organizationId = "org123";
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        String fleetName = "fleetA";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.findAllByFleet(organizationId, fleetName, 0, 10)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        assertEquals(crewDisplayObject, result);
        verify(crewService).findAllByFleet(organizationId, fleetName, 0, 10);
    }
}
