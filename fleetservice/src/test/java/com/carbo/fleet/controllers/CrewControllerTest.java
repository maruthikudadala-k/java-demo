
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.services.CrewService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.validation.Valid;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        when(crewService.findAll(organizationId, 0, 10)).thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        // Assert
        assertEquals(crewDisplayObject, result);
        verify(crewService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "crew123";
        CrewDto crewDto = new CrewDto();
        when(crewService.findById(crewId)).thenReturn(crewDto);

        // Act
        CrewDto result = crewController.getCrew(request, crewId);

        // Assert
        assertEquals(crewDto, result);
        verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        Crew crew = new Crew();
        when(crewService.saveCrew(crewDto)).thenReturn(crew);

        // Act
        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(crew, result.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        when(crewService.saveCrew(crewDto)).thenReturn(null);

        // Act
        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdatePersonnelCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        crewDto.setId("crew123");
        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        // Act
        CrewDto result = crewController.updatePersonnel(request, crewDto);

        // Assert
        assertEquals(crewDto, result);
        verify(crewService).updateCrew(crewDto);
        verify(crewService).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewCalled() {
        // Arrange
        String crewId = "crew123";

        // Act
        crewController.deleteCrew(crewId);

        // Assert
        verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        String fleetName = "Fleet A";
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        when(crewService.findAllByFleet(organizationId, fleetName, 0, 10)).thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        // Assert
        assertEquals(crewDisplayObject, result);
        verify(crewService).findAllByFleet(organizationId, fleetName, 0, 10);
    }
}
