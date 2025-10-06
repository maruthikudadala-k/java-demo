
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
import java.util.Collections;
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
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(request.getParameter("offSet")).thenReturn("0");
        when(request.getParameter("limit")).thenReturn("10");
        when(crewService.findAll(any(), eq(0), eq(10))).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, 0, 10);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService).findAll(any(), eq(0), eq(10));
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        String crewId = "123";
        CrewDto expectedCrewDto = new CrewDto();
        when(crewService.findById(crewId)).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.getCrew(request, crewId);

        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        CrewDto crewDto = new CrewDto();
        Crew newCrew = new Crew();
        when(request.getParameter("organizationId")).thenReturn("orgId");
        when(crewService.saveCrew(crewDto)).thenReturn(newCrew);

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(newCrew, responseEntity.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        CrewDto crewDto = new CrewDto();
        when(request.getParameter("organizationId")).thenReturn("orgId");
        when(crewService.saveCrew(crewDto)).thenReturn(null);

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        Map<String, String> error = (Map<String, String>) responseEntity.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        CrewDto crewDto = new CrewDto();
        when(request.getParameter("organizationId")).thenReturn("orgId");
        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, actualCrewDto);
        verify(crewService).updateCrew(crewDto);
        verify(crewService).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "123";

        crewController.deleteCrew(crewId);

        verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(request.getParameter("fleetName")).thenReturn("fleetName");
        when(crewService.findAllByFleet(any(), eq("fleetName"), eq(0), eq(10))).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService).findAllByFleet(any(), eq("fleetName"), eq(0), eq(10));
    }
}
