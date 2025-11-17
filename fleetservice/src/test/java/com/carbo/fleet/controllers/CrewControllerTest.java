
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
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId123";
        request.setUserPrincipal(() -> organizationId);
        
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(crewService.findAll(organizationId, 0, 10)).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, 0, 10);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "crewId123";
        CrewDto expectedCrewDto = new CrewDto();
        when(crewService.findById(crewId)).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.getCrew(request, crewId);

        assertEquals(expectedCrewDto, actualCrewDto);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        Crew newCrew = new Crew();
        when(crewService.saveCrew(crewDto)).thenReturn(newCrew);

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(newCrew, responseEntity.getBody());
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId(organizationId);
        when(crewService.saveCrew(crewDto)).thenReturn(null);
        
        Map<String, String> expectedError = new HashMap<>();
        expectedError.put("errorMessage", Constants.CREW_ALREADY_EXISTS);

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(expectedError, responseEntity.getBody());
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setId("crewId123");
        crewDto.setOrganizationId(organizationId);
        when(crewService.updateCrew(any())).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, actualCrewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewCalled() {
        String crewId = "crewId123";

        crewController.deleteCrew(crewId);

        Mockito.verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId123";
        request.setUserPrincipal(() -> organizationId);
        
        String fleetName = "Fleet A";
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(crewService.findAllByFleet(organizationId, fleetName, 0, 10)).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }
}
