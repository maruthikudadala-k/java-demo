
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.services.CrewService;
import com.carbo.fleet.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    private MockHttpServletRequest request;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        request.setUserPrincipal(Mockito.mock(Principal.class));
    }

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        CrewDisplayObject mockCrewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.emptyList())
                .totalCount(0)
                .build();

        Mockito.when(crewService.findAll(anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(mockCrewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        Mockito.verify(crewService).findAll(anyString(), Mockito.eq(0), Mockito.eq(10));
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        CrewDto mockCrewDto = CrewDto.builder()
                .id("1")
                .name("Test Crew")
                .jobPattern("Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .fleetId("fleetId")
                .organizationId("orgId")
                .build();

        Mockito.when(crewService.findById(anyString())).thenReturn(mockCrewDto);

        CrewDto result = crewController.getCrew(request, "1");

        assertNotNull(result);
        assertEquals("Test Crew", result.getName());
        Mockito.verify(crewService).findById("1");
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        CrewDto mockCrewDto = CrewDto.builder()
                .name("New Crew")
                .jobPattern("Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .fleetId("fleetId")
                .organizationId("orgId")
                .build();
        
        Crew mockCrew = new Crew();
        mockCrew.setId("1");

        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(mockCrew);

        ResponseEntity<Object> response = crewController.createCrew(request, mockCrewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockCrew, response.getBody());
        Mockito.verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        CrewDto mockCrewDto = CrewDto.builder()
                .name("Existing Crew")
                .jobPattern("Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .fleetId("fleetId")
                .organizationId("orgId")
                .build();
        
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, mockCrewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertNotNull(error);
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        Mockito.verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        CrewDto mockCrewDto = CrewDto.builder()
                .id("1")
                .name("Updated Crew")
                .jobPattern("Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .fleetId("fleetId")
                .organizationId("orgId")
                .build();

        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        Mockito.when(crewService.findById(anyString())).thenReturn(mockCrewDto);

        CrewDto result = crewController.updatePersonnel(request, mockCrewDto);

        assertNotNull(result);
        assertEquals("Updated Crew", result.getName());
        Mockito.verify(crewService).updateCrew(any(CrewDto.class));
        Mockito.verify(crewService).findById("1");
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "1";

        crewController.deleteCrew(crewId);

        Mockito.verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        CrewDisplayObject mockCrewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.emptyList())
                .totalCount(0)
                .build();

        Mockito.when(crewService.findAllByFleet(anyString(), anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(mockCrewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "FleetName");

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        Mockito.verify(crewService).findAllByFleet(anyString(), Mockito.eq("FleetName"), Mockito.eq(0), Mockito.eq(10));
    }
}
