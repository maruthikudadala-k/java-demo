
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> organizationId);
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        
        when(crewService.findAll(organizationId, 0, 10)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertNotNull(result);
        verify(crewService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "testCrewId";
        CrewDto crewDto = new CrewDto();
        
        when(crewService.findById(crewId)).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, crewId);

        assertNotNull(result);
        verify(crewService).findById(crewId);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> organizationId);
        CrewDto crewDto = new CrewDto();
        crewDto.setId("testId");
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("testFleetId");

        Crew crewNew = new Crew();

        when(crewService.saveCrew(crewDto)).thenReturn(crewNew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> organizationId);
        CrewDto crewDto = new CrewDto();
        crewDto.setId("testId");
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("testFleetId");

        when(crewService.saveCrew(crewDto)).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
        verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldUpdateCrewWhenUpdatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> organizationId);
        CrewDto crewDto = new CrewDto();
        crewDto.setId("testId");
        crewDto.setName("Updated Crew");

        when(crewService.updateCrew(crewDto)).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertNotNull(result);
        verify(crewService).updateCrew(crewDto);
        verify(crewService).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "testCrewId";

        crewController.deleteCrew(crewId);

        verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> organizationId);
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        
        when(crewService.findAllByFleet(organizationId, "testFleetName", 0, 10)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "testFleetName");

        assertNotNull(result);
        verify(crewService).findAllByFleet(organizationId, "testFleetName", 0, 10);
    }
}
