
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        String organizationId = "org123";
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();

        when(crewService.findAll(organizationId, 0, 10)).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actual = crewController.getAllCrew(request, 0, 10);

        assertEquals(expectedCrewDisplayObject, actual);
        verify(crewService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewById() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "crew123";
        CrewDto expectedCrewDto = new CrewDto();

        when(crewService.findById(crewId)).thenReturn(expectedCrewDto);

        CrewDto actual = crewController.getCrew(request, crewId);

        assertEquals(expectedCrewDto, actual);
        verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        CrewDto crewDto = new CrewDto();
        Crew savedCrew = new Crew();

        request.addHeader("Authorization", "Bearer token");
        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(savedCrew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedCrew, response.getBody());
        verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();

        request.addHeader("Authorization", "Bearer token");
        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");

        when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, actualCrewDto);
        verify(crewService).updateCrew(any(CrewDto.class));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrew() {
        String crewId = "crew123";

        crewController.deleteCrew(crewId);

        verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnCrewByFleetWhenGetAllCrewByFleet() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        String fleetName = "fleetA";
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();

        when(crewService.findAllByFleet(organizationId, fleetName, 0, 10)).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actual = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        assertEquals(expectedCrewDisplayObject, actual);
        verify(crewService).findAllByFleet(organizationId, fleetName, 0, 10);
    }
}
