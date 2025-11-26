
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    private final MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    public void shouldReturnAllCrewWhenGetAllCrew() {
        // Arrange
        String organizationId = "org123";
        request.addHeader("Authorization", "Bearer token");
        Mockito.when(crewService.findAll(eq(organizationId), eq(0), eq(10)))
                .thenReturn(new CrewDisplayObject());

        // Act
        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        // Assert
        assertEquals(new CrewDisplayObject(), result);
        Mockito.verify(crewService).findAll(eq(organizationId), eq(0), eq(10));
    }

    @Test
    public void shouldReturnCrewWhenGetCrew() {
        // Arrange
        String crewId = "crew123";
        CrewDto crewDto = new CrewDto();
        Mockito.when(crewService.findById(crewId)).thenReturn(crewDto);

        // Act
        CrewDto result = crewController.getCrew(request, crewId);

        // Assert
        assertEquals(crewDto, result);
        Mockito.verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrew() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("org123");
        crewDto.setId("crew123");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(new Crew());

        // Act
        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Mockito.verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrew() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        Mockito.when(crewService.findById(crewDto.getId())).thenReturn(crewDto);
        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);

        // Act
        CrewDto result = crewController.updatePersonnel(request, crewDto);

        // Assert
        assertEquals(crewDto, result);
        Mockito.verify(crewService).updateCrew(any(CrewDto.class));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrew() {
        // Arrange
        String crewId = "crew123";

        // Act
        crewController.deleteCrew(crewId);

        // Assert
        Mockito.verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleet() {
        // Arrange
        String organizationId = "org123";
        String fleetName = "Fleet1";
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        Mockito.when(crewService.findAllByFleet(eq(organizationId), eq(fleetName), eq(0), eq(10)))
                .thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        // Assert
        assertEquals(crewDisplayObject, result);
        Mockito.verify(crewService).findAllByFleet(eq(organizationId), eq(fleetName), eq(0), eq(10));
    }
}
