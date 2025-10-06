
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        CrewDisplayObject expectedCrewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.emptyList())
                .totalCount(0)
                .build();

        when(crewService.findAll(any(String.class), anyInt(), anyInt())).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, 0, 10);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService).findAll(any(String.class), anyInt(), anyInt());
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "crewId";
        CrewDto expectedCrewDto = new CrewDto();

        when(crewService.findById(crewId)).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.getCrew(request, crewId);

        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        CrewDto crewDto = new CrewDto();
        Crew expectedCrew = new Crew();

        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(expectedCrew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedCrew, response.getBody());
        verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExistsOnCreate() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        CrewDto crewDto = new CrewDto();

        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> expectedError = new HashMap<>();
        expectedError.put("errorMessage", Constants.CREW_ALREADY_EXISTS);
        assertEquals(expectedError, response.getBody());
        verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crewId");
        CrewDto expectedCrewDto = new CrewDto();

        when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService).updateCrew(any(CrewDto.class));
        verify(crewService).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "crewId";

        crewController.deleteCrew(crewId);

        verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        CrewDisplayObject expectedCrewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.emptyList())
                .totalCount(0)
                .build();
        String fleetName = "fleetName";

        when(crewService.findAllByFleet(any(String.class), any(String.class), anyInt(), anyInt())).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService).findAllByFleet(any(String.class), any(String.class), anyInt(), anyInt());
    }
}
