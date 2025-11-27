
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        
        when(crewService.findAll(organizationId, offSet, limit)).thenReturn(crewDisplayObject);

        // When
        CrewDisplayObject result = crewController.getAllCrew(request, offSet, limit);

        // Then
        assertEquals(crewDisplayObject, result);
        verify(crewService).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "crewId";
        CrewDto crewDto = new CrewDto();
        
        when(crewService.findById(id)).thenReturn(crewDto);

        // When
        CrewDto result = crewController.getCrew(request, id);

        // Then
        assertEquals(crewDto, result);
        verify(crewService).findById(id);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crewId");
        crewDto.setName("Crew Name");
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", Constants.CREW_ALREADY_EXISTS);
        Crew crew = new Crew();

        when(crewService.saveCrew(crewDto)).thenReturn(crew);

        // When
        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        // Then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(crew, result.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExistsInCreateCrew() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", Constants.CREW_ALREADY_EXISTS);

        when(crewService.saveCrew(crewDto)).thenReturn(null);

        // When
        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        // Then
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(error, result.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crewId");
        crewDto.setName("Updated Crew Name");
        
        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        // When
        CrewDto result = crewController.updatePersonnel(request, crewDto);

        // Then
        assertEquals(crewDto, result);
        verify(crewService).updateCrew(crewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        // Given
        String id = "crewId";

        // When
        crewController.deleteCrew(id);

        // Then
        verify(crewService).deleteCrew(id);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        String fleetName = "Fleet 1";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();

        when(crewService.findAllByFleet(organizationId, fleetName, offSet, limit)).thenReturn(crewDisplayObject);

        // When
        CrewDisplayObject result = crewController.getAllCrewByFleet(request, offSet, limit, fleetName);

        // Then
        assertEquals(crewDisplayObject, result);
        verify(crewService).findAllByFleet(organizationId, fleetName, offSet, limit);
    }
}
