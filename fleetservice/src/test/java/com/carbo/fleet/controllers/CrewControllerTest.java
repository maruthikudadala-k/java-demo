
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
    public void shouldReturnAllCrewWhenGetAllCrewCalled() {
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.findAll(organizationId, offSet, limit)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, offSet, limit);

        assertEquals(crewDisplayObject, result);
        verify(crewService).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewCalled() {
        String id = "crewId";
        CrewDto crewDto = new CrewDto();
        
        when(crewService.findById(id)).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, id);

        assertEquals(crewDto, result);
        verify(crewService).findById(id);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewCalled() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crewId");
        crewDto.setName("Crew Name");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleetId");
        
        String organizationId = "org123";
        Crew crewNew = new Crew();

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.saveCrew(crewDto)).thenReturn(crewNew);

        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(crewNew, result.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crewId");
        
        String organizationId = "org123";
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", Constants.CREW_ALREADY_EXISTS);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.saveCrew(crewDto)).thenReturn(null);

        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(error, result.getBody());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewCalled() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crewId");
        
        String organizationId = "org123";

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, result);
        verify(crewService).updateCrew(crewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewCalled() {
        String id = "crewId";

        crewController.deleteCrew(id);

        verify(crewService).deleteCrew(id);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetCalled() {
        String organizationId = "org123";
        String fleetName = "FleetName";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(crewService.findAllByFleet(organizationId, fleetName, offSet, limit)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, offSet, limit, fleetName);

        assertEquals(crewDisplayObject, result);
        verify(crewService).findAllByFleet(organizationId, fleetName, offSet, limit);
    }
}
