
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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoJUnitRunner.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        when(request.getUserPrincipal()).thenReturn(Mockito.mock(java.security.Principal.class));
        String organizationId = "org123";
        when(crewService.findAll(organizationId, 0, 10)).thenReturn(new CrewDisplayObject());

        // When
        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        // Then
        assertNotNull(result);
        Mockito.verify(crewService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        String crewId = "crew123";
        CrewDto crewDto = new CrewDto();
        when(crewService.findById(crewId)).thenReturn(crewDto);

        // When
        CrewDto result = crewController.getCrew(request, crewId);

        // Then
        assertNotNull(result);
        Mockito.verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        when(request.getUserPrincipal()).thenReturn(Mockito.mock(java.security.Principal.class));
        String organizationId = "org123";
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        Crew crew = new Crew();
        when(crewService.saveCrew(crewDto)).thenReturn(crew);

        // When
        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());
        Mockito.verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        when(request.getUserPrincipal()).thenReturn(Mockito.mock(java.security.Principal.class));
        String organizationId = "org123";
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        when(crewService.saveCrew(crewDto)).thenReturn(null);
    
        // When
        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);
    
        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        Mockito.verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdatePersonnelIsCalled() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        crewDto.setId("crew123");
        CrewDto updatedCrewDto = new CrewDto();
        when(crewService.findById(crewDto.getId())).thenReturn(updatedCrewDto);
        when(crewService.updateCrew(crewDto)).thenReturn(Boolean.TRUE);

        // When
        CrewDto result = crewController.updatePersonnel(request, crewDto);

        // Then
        assertNotNull(result);
        Mockito.verify(crewService).updateCrew(crewDto);
        Mockito.verify(crewService).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        // Given
        String crewId = "crew123";

        // When
        crewController.deleteCrew(crewId);

        // Then
        Mockito.verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        String fleetName = "Fleet A";
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        when(crewService.findAllByFleet(organizationId, fleetName, 0, 10)).thenReturn(crewDisplayObject);

        // When
        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        // Then
        assertNotNull(result);
        Mockito.verify(crewService).findAllByFleet(organizationId, fleetName, 0, 10);
    }
}
