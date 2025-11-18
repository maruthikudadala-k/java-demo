
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
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(crewService.findAll(organizationId, offSet, limit)).thenReturn(expectedCrewDisplayObject);
        
        // Act
        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, offSet, limit);

        // Assert
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "crew123";
        CrewDto expectedCrewDto = new CrewDto();
        when(crewService.findById(id)).thenReturn(expectedCrewDto);
        
        // Act
        CrewDto actualCrewDto = crewController.getCrew(request, id);

        // Assert
        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService).findById(id);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        String organizationId = "org123";
        Crew newCrew = new Crew();
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", Constants.CREW_ALREADY_EXISTS);
        when(crewService.saveCrew(crewDto)).thenReturn(newCrew);

        // Act
        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCrew, response.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        String organizationId = "org123";
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", Constants.CREW_ALREADY_EXISTS);
        when(crewService.saveCrew(crewDto)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(error, response.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdatePersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        CrewDto expectedCrewDto = new CrewDto();
        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(expectedCrewDto);

        // Act
        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        // Assert
        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService).updateCrew(crewDto);
        verify(crewService).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        // Arrange
        String id = "crew123";

        // Act
        crewController.deleteCrew(id);

        // Assert
        verify(crewService).deleteCrew(id);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        String fleetName = "Fleet1";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(crewService.findAllByFleet(organizationId, fleetName, offSet, limit)).thenReturn(expectedCrewDisplayObject);
        
        // Act
        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, offSet, limit, fleetName);

        // Assert
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService).findAllByFleet(organizationId, fleetName, offSet, limit);
    }
}
