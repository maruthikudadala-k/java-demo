
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
        CrewDisplayObject displayObject = new CrewDisplayObject();
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(crewService.findAll(anyString(), any(Integer.class), any(Integer.class))).thenReturn(displayObject);
        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);
        assertNotNull(result);
        Mockito.verify(crewService).findAll(anyString(), any(Integer.class), any(Integer.class));
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        CrewDto crewDto = new CrewDto();
        Mockito.when(crewService.findById(anyString())).thenReturn(crewDto);
        CrewDto result = crewController.getCrew(request, "1");
        assertNotNull(result);
        Mockito.verify(crewService).findById(anyString());
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("orgId");
        Crew crew = new Crew();
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", Constants.CREW_ALREADY_EXISTS);

        Mockito.when(crewService.saveCrew(crewDto)).thenReturn(crew);
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());

        Mockito.when(crewService.saveCrew(crewDto)).thenReturn(null);
        ResponseEntity<Object> responseConf = crewController.createCrew(request, crewDto);
        assertEquals(HttpStatus.CONFLICT, responseConf.getStatusCode());
        assertEquals(error, responseConf.getBody());
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setOrganizationId("orgId");
        
        Mockito.when(crewService.updateCrew(crewDto)).thenReturn(true);
        Mockito.when(crewService.findById(anyString())).thenReturn(crewDto);
        
        CrewDto result = crewController.updatePersonnel(request, crewDto);
        assertNotNull(result);
        Mockito.verify(crewService).updateCrew(crewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        crewController.deleteCrew("1");
        Mockito.verify(crewService).deleteCrew("1");
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        CrewDisplayObject displayObject = new CrewDisplayObject();
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(crewService.findAllByFleet(anyString(), anyString(), any(Integer.class), any(Integer.class))).thenReturn(displayObject);
        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");
        assertNotNull(result);
        Mockito.verify(crewService).findAllByFleet(anyString(), anyString(), any(Integer.class), any(Integer.class));
    }
}
