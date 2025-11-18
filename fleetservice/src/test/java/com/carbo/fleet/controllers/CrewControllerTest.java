
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
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
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDisplayObject mockCrewDisplayObject = new CrewDisplayObject();
        when(crewService.findAll(organizationId, 0, 10)).thenReturn(mockCrewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertNotNull(result);
        verify(crewService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "crew123";
        CrewDto mockCrewDto = new CrewDto();
        when(crewService.findById(id)).thenReturn(mockCrewDto);

        CrewDto result = crewController.getCrew(request, id);

        assertNotNull(result);
        verify(crewService).findById(id);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("Crew Name");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet123");

        Crew mockCrew = new Crew();
        when(crewService.saveCrew(crewDto)).thenReturn(mockCrew);

        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(mockCrew, result.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("Crew Name");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet123");

        when(crewService.saveCrew(crewDto)).thenReturn(null);

        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        Map<String, String> error = (Map<String, String>) result.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("Updated Crew Name");

        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertNotNull(result);
        assertEquals("Updated Crew Name", result.getName());
        verify(crewService).updateCrew(crewDto);
        verify(crewService).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String id = "crew123";

        crewController.deleteCrew(id);

        verify(crewService).deleteCrew(id);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        String fleetName = "Fleet A";

        CrewDisplayObject mockCrewDisplayObject = new CrewDisplayObject();
        when(crewService.findAllByFleet(organizationId, fleetName, 0, 10)).thenReturn(mockCrewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        assertNotNull(result);
        verify(crewService).findAllByFleet(organizationId, fleetName, 0, 10);
    }
}
