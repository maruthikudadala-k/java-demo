
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        String organizationId = "org123";
        when(crewService.findAll(eq(organizationId), any(Integer.class), any(Integer.class)))
                .thenReturn(new CrewDisplayObject());

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);
        assertNotNull(result);
        Mockito.verify(crewService).findAll(eq(organizationId), eq(0), eq(10));
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "crew123";
        CrewDto crewDto = new CrewDto();
        when(crewService.findById(crewId)).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, crewId);
        assertEquals(crewDto, result);
        Mockito.verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        String organizationId = "org123";
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet1");

        Crew crew = new Crew();
        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(crew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());
        Mockito.verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet1");

        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        Mockito.verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("Updated Crew");

        when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);
        assertEquals(crewDto, result);
        Mockito.verify(crewService).updateCrew(any(CrewDto.class));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "crew123";
        crewController.deleteCrew(crewId);
        Mockito.verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        String organizationId = "org123";
        String fleetName = "Fleet 1";
        when(crewService.findAllByFleet(eq(organizationId), eq(fleetName), any(Integer.class), any(Integer.class)))
                .thenReturn(new CrewDisplayObject());

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, fleetName);
        assertNotNull(result);
        Mockito.verify(crewService).findAllByFleet(eq(organizationId), eq(fleetName), eq(0), eq(10));
    }
}
