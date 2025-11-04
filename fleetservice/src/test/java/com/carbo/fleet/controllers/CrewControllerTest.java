
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
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
        request.setUserPrincipal(() -> "user");
        
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.emptyList())
                .totalCount(0)
                .build();
        Mockito.when(crewService.findAll(any(String.class), eq(0), eq(10))).thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("Test Crew")
                .jobPattern("Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2022")
                .fleetId("fleetId")
                .organizationId("orgId")
                .build();
        Mockito.when(crewService.findById("1")).thenReturn(crewDto);

        // Act
        CrewDto result = crewController.getCrew(request, "1");

        // Assert
        assertNotNull(result);
        assertEquals("Test Crew", result.getName());
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        
        CrewDto crewDto = CrewDto.builder()
                .name("Test Crew")
                .jobPattern("Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2022")
                .fleetId("fleetId")
                .build();
        Crew crew = new Crew();
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(crew);

        // Act
        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        
        CrewDto crewDto = CrewDto.builder()
                .name("Test Crew")
                .jobPattern("Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2022")
                .fleetId("fleetId")
                .build();
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        // Act
        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, errorResponse.get("errorMessage"));
    }

    @Test
    public void shouldUpdateCrewWhenUpdatePersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("Updated Crew")
                .jobPattern("Updated Pattern")
                .shiftStart("09:00")
                .startDate("01/02/2022")
                .fleetId("fleetId")
                .build();
        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        Mockito.when(crewService.findById("1")).thenReturn(crewDto);

        // Act
        CrewDto result = crewController.updatePersonnel(request, crewDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Crew", result.getName());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        // Arrange
        Mockito.doNothing().when(crewService).deleteCrew("1");

        // Act
        crewController.deleteCrew("1");

        // Assert
        Mockito.verify(crewService, Mockito.times(1)).deleteCrew("1");
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.emptyList())
                .totalCount(0)
                .build();
        Mockito.when(crewService.findAllByFleet(any(String.class), any(String.class), eq(0), eq(10))).thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }
}
