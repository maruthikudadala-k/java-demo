
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
        String organizationId = "org123";
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.findAll(organizationId, 0, 10)).thenReturn(expectedCrewDisplayObject);

        // Act
        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, 0, 10);

        // Assert
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        // Arrange
        String crewId = "crew123";
        CrewDto expectedCrewDto = new CrewDto();
        when(crewService.findById(crewId)).thenReturn(expectedCrewDto);

        // Act
        CrewDto actualCrewDto = crewController.getCrew(request, crewId);

        // Assert
        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        crewDto.setName("Crew Name");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00 AM");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet123");
        String organizationId = "org123";
        crewDto.setOrganizationId(organizationId);
        Crew crewNew = new Crew();
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", Constants.CREW_ALREADY_EXISTS);
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.saveCrew(crewDto)).thenReturn(crewNew);

        // Act
        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(crewNew, responseEntity.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        crewDto.setName("Crew Name");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00 AM");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet123");
        String organizationId = "org123";
        crewDto.setOrganizationId(organizationId);
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.saveCrew(crewDto)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Crew already exists", ((Map<String, String>) responseEntity.getBody()).get("errorMessage"));
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("Updated Crew Name");
        String organizationId = "org123";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        // Act
        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        // Assert
        assertEquals(crewDto, actualCrewDto);
        verify(crewService).updateCrew(crewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        // Arrange
        String crewId = "crew123";

        // Act
        crewController.deleteCrew(crewId);

        // Assert
        verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        // Arrange
        String organizationId = "org123";
        String fleetName = "Fleet Name";
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.findAllByFleet(organizationId, fleetName, 0, 10)).thenReturn(expectedCrewDisplayObject);

        // Act
        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        // Assert
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService).findAllByFleet(organizationId, fleetName, 0, 10);
    }
}
