
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.findAll(organizationId, offSet, limit)).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, offSet, limit);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService, times(1)).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        String crewId = "crew123";
        CrewDto expectedCrewDto = new CrewDto();
        
        when(crewService.findById(crewId)).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.getCrew(request, crewId);

        assertEquals(expectedCrewDto, actualCrewDto);
        verify(crewService, times(1)).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("org123");
        Crew newCrew = new Crew();

        when(request.getUserPrincipal()).thenReturn(() -> "org123");
        when(crewService.saveCrew(crewDto)).thenReturn(newCrew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCrew, response.getBody());
        verify(crewService, times(1)).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("org123");

        when(request.getUserPrincipal()).thenReturn(() -> "org123");
        when(crewService.saveCrew(crewDto)).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        verify(crewService, times(1)).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdatePersonnelIsCalled() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setOrganizationId("org123");

        when(request.getUserPrincipal()).thenReturn(() -> "org123");
        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, actualCrewDto);
        verify(crewService, times(1)).updateCrew(crewDto);
        verify(crewService, times(1)).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "crew123";

        crewController.deleteCrew(crewId);

        verify(crewService, times(1)).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        String organizationId = "org123";
        String fleetName = "FleetA";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.findAllByFleet(organizationId, fleetName, offSet, limit)).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, offSet, limit, fleetName);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
        verify(crewService, times(1)).findAllByFleet(organizationId, fleetName, offSet, limit);
    }
}
