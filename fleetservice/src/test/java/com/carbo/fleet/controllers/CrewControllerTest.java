
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

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "userPrincipal");
        
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        crewDisplayObject.setCrews(Collections.emptyList());
        crewDisplayObject.setTotalCount(0);

        Mockito.when(crewService.findAll(any(), eq(0), eq(10))).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertEquals(crewDisplayObject, result);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "1";
        CrewDto crewDto = new CrewDto();
        crewDto.setId(crewId);
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00 AM");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("Fleet1");

        Mockito.when(crewService.findById(crewId)).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, crewId);

        assertEquals(crewDto, result);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setName("New Crew");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00 AM");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("Fleet1");

        Crew crew = new Crew();
        crew.setId("1");
        crew.setName("New Crew");

        request.setUserPrincipal(() -> "userPrincipal");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(crew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setName("Existing Crew");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00 AM");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("Fleet1");

        request.setUserPrincipal(() -> "userPrincipal");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        HashMap<String, String> error = (HashMap<String, String>) response.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
    }

    @Test
    public void shouldUpdateCrewWhenUpdatePersonnelCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Updated Crew");
        crewDto.setJobPattern("Updated Job Pattern");
        crewDto.setShiftStart("08:00 AM");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("Fleet1");

        request.setUserPrincipal(() -> "userPrincipal");
        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        Mockito.when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, result);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewCalled() {
        String crewId = "1";
        crewController.deleteCrew(crewId);

        Mockito.verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "userPrincipal");
        
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        crewDisplayObject.setCrews(Collections.emptyList());
        crewDisplayObject.setTotalCount(0);

        Mockito.when(crewService.findAllByFleet(any(), any(), eq(0), eq(10))).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "Fleet1");

        assertEquals(crewDisplayObject, result);
    }
}
