
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        // Arrange
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(request.getUserPrincipal()).thenReturn(() -> "dummyPrincipal");
        when(crewService.findAll(any(String.class), eq(0), eq(10))).thenReturn(expectedCrewDisplayObject);

        // Act
        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, 0, 10);

        // Assert
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        // Arrange
        String crewId = "1";
        CrewDto expectedCrewDto = new CrewDto();
        when(crewService.findById(crewId)).thenReturn(expectedCrewDto);

        // Act
        CrewDto actualCrewDto = crewController.getCrew(request, crewId);

        // Assert
        assertEquals(expectedCrewDto, actualCrewDto);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("orgId");
        Crew expectedCrew = new Crew();
        when(request.getUserPrincipal()).thenReturn(() -> "dummyPrincipal");
        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(expectedCrew);

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
        crewDto.setOrganizationId("orgId");
        when(request.getUserPrincipal()).thenReturn(() -> "dummyPrincipal");
        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        Map<String, String> error = (Map<String, String>) responseEntity.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        when(request.getUserPrincipal()).thenReturn(() -> "dummyPrincipal");
        when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        // Act
        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        // Assert
        assertEquals(crewDto, actualCrewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        // Arrange
        String crewId = "1";

        // Act
        crewController.deleteCrew(crewId);

        // Assert
        verify(crewService, times(1)).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        // Arrange
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        String fleetName = "Fleet1";
        when(request.getUserPrincipal()).thenReturn(() -> "dummyPrincipal");
        when(crewService.findAllByFleet(any(String.class), eq(fleetName), eq(0), eq(10))).thenReturn(expectedCrewDisplayObject);

        // Act
        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        // Assert
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }
}
