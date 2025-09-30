
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        when(crewService.findAll(organizationId, 0, 10)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertEquals(crewDisplayObject, result);
        verify(crewService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "crew123";
        CrewDto crewDto = new CrewDto();

        when(crewService.findById(crewId)).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, crewId);

        assertEquals(crewDto, result);
        verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        Crew crew = new Crew();
        when(crewService.saveCrew(crewDto)).thenReturn(crew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExistsOnCreateCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        when(crewService.saveCrew(crewDto)).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        crewDto.setId("crew123");
        
        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, result);
        verify(crewService).updateCrew(crewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewCalled() {
        String crewId = "crew123";

        crewController.deleteCrew(crewId);

        verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnCrewByFleetWhenGetAllCrewByFleetCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        String fleetName = "fleetName";
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        when(crewService.findAllByFleet(organizationId, fleetName, 0, 10)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        assertEquals(crewDisplayObject, result);
        verify(crewService).findAllByFleet(organizationId, fleetName, 0, 10);
    }
}
