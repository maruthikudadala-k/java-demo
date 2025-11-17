
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
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        // Arrange
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(crewService.findAll(anyString(), any(Integer.class), any(Integer.class))).thenReturn(expectedCrewDisplayObject);

        // Act
        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, 0, 10);

        // Assert
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        // Arrange
        CrewDto expectedCrewDto = new CrewDto();
        Mockito.when(crewService.findById(anyString())).thenReturn(expectedCrewDto);

        // Act
        CrewDto actualCrewDto = crewController.getCrew(request, "crewId");

        // Assert
        assertEquals(expectedCrewDto, actualCrewDto);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        Crew expectedCrew = new Crew();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(expectedCrew);

        // Act
        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedCrew, responseEntity.getBody());
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);
        Map<String, String> expectedError = new HashMap<>();
        expectedError.put("errorMessage", Constants.CREW_ALREADY_EXISTS);

        // Act
        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(expectedError, responseEntity.getBody());
    }

    @Test
    public void shouldUpdateCrewWhenUpdatePersonnelIsCalled() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        Mockito.when(crewService.findById(anyString())).thenReturn(crewDto);

        // Act
        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        // Assert
        assertEquals(crewDto, actualCrewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        // Arrange
        String crewId = "crewId";

        // Act
        crewController.deleteCrew(crewId);

        // Assert
        Mockito.verify(crewService, Mockito.times(1)).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        // Arrange
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("orgId");
        Mockito.when(crewService.findAllByFleet(anyString(), anyString(), any(Integer.class), any(Integer.class))).thenReturn(expectedCrewDisplayObject);

        // Act
        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");

        // Assert
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }
}
