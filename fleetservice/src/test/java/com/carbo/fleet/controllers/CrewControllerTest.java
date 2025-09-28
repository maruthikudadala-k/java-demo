
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    void shouldReturnCrewDisplayObjectWhenGetAllCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(0).build();
        when(crewService.findAll(any(String.class), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertEquals(crewDisplayObject, result);
        verify(crewService).findAll(any(String.class), eq(0), eq(10));
    }

    @Test
    void shouldReturnCrewDtoWhenGetCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Pattern A").shiftStart("08:00").startDate("01/01/2022").fleetId("fleet1").build();
        when(crewService.findById("1")).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, "1");

        assertEquals(crewDto, result);
        verify(crewService).findById("1");
    }

    @Test
    void shouldCreateCrewAndReturnCreatedResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Pattern A").shiftStart("08:00").startDate("01/01/2022").fleetId("fleet1").build();
        Crew crew = new Crew();
        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(crew);

        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(crew, result.getBody());
        verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    void shouldReturnConflictResponseWhenCrewAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Pattern A").shiftStart("08:00").startDate("01/01/2022").fleetId("fleet1").build();
        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        Map<String, String> error = (Map<String, String>) result.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    void shouldReturnCrewDtoWhenUpdatePersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Pattern A").shiftStart("08:00").startDate("01/01/2022").fleetId("fleet1").build();
        when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        when(crewService.findById("1")).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, result);
        verify(crewService).updateCrew(any(CrewDto.class));
        verify(crewService).findById("1");
    }

    @Test
    void shouldDeleteCrewWhenDeleteCrew() {
        String crewId = "1";

        crewController.deleteCrew(crewId);

        verify(crewService).deleteCrew(crewId);
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenGetAllCrewByFleet() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(0).build();
        when(crewService.findAllByFleet(any(String.class), any(String.class), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "Fleet A");

        assertEquals(crewDisplayObject, result);
        verify(crewService).findAllByFleet(any(String.class), eq("Fleet A"), eq(0), eq(10));
    }
}
