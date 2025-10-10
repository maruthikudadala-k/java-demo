
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoJUnitRunner.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        Mockito.when(crewService.findAll(anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, 0, 10);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto expectedCrewDto = new CrewDto();
        Mockito.when(crewService.findById(anyString())).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.getCrew(request, "testId");

        assertEquals(expectedCrewDto, actualCrewDto);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setId("testId");
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Test Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("testFleetId");
        Crew savedCrew = new Crew();
        savedCrew.setId("testId");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(savedCrew);
        request.setUserPrincipal(() -> "testPrincipal");
        request.setAttribute("organizationId", "testOrgId");

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedCrew, responseEntity.getBody());
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExistsWhileCreating() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setId("testId");
        CrewDto savedCrew = null;
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(savedCrew);
        request.setUserPrincipal(() -> "testPrincipal");
        request.setAttribute("organizationId", "testOrgId");

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        Map<String, String> errorResponse = (Map<String, String>) responseEntity.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, errorResponse.get("errorMessage"));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setId("testId");
        crewDto.setName("Updated Crew");
        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        Mockito.when(crewService.findById(anyString())).thenReturn(crewDto);
        request.setUserPrincipal(() -> "testPrincipal");
        request.setAttribute("organizationId", "testOrgId");

        CrewDto updatedCrewDto = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, updatedCrewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        crewController.deleteCrew("testId");
        Mockito.verify(crewService, Mockito.times(1)).deleteCrew("testId");
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        Mockito.when(crewService.findAllByFleet(anyString(), anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, 0, 10, "testFleet");

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }
}
