
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenValidRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        Mockito.when(crewService.findAll(anyString(), any(Integer.class), any(Integer.class)))
                .thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, 0, 10);
        
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }

    @Test
    public void shouldReturnCrewWhenValidId() {
        String crewId = "123";
        CrewDto expectedCrewDto = new CrewDto();
        Mockito.when(crewService.findById(crewId)).thenReturn(expectedCrewDto);

        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto actualCrewDto = crewController.getCrew(request, crewId);
        
        assertEquals(expectedCrewDto, actualCrewDto);
    }

    @Test
    public void shouldCreateCrewWhenValidCrewDto() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleetId");
        
        Crew expectedCrew = new Crew();
        Mockito.when(crewService.saveCrew(crewDto)).thenReturn(expectedCrew);

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);
        
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedCrew, responseEntity.getBody());
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleetId");
        
        Mockito.when(crewService.saveCrew(crewDto)).thenReturn(null);

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);
        
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        Map<String, String> error = (Map<String, String>) responseEntity.getBody();
        assertEquals(Constants.CREW_ALREADY_EXISTS, error.get("errorMessage"));
    }

    @Test
    public void shouldUpdateCrewWhenValidCrewDto() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Updated Crew");

        Mockito.when(crewService.updateCrew(crewDto)).thenReturn(true);
        Mockito.when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);
        
        assertEquals(crewDto, actualCrewDto);
    }

    @Test
    public void shouldReturnNullWhenCrewUpdateFails() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Updated Crew");

        Mockito.when(crewService.updateCrew(crewDto)).thenReturn(false);

        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);
        
        assertNull(actualCrewDto);
    }

    @Test
    public void shouldDeleteCrewWhenValidId() {
        String crewId = "123";
        crewController.deleteCrew(crewId);

        Mockito.verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenValidRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        Mockito.when(crewService.findAllByFleet(anyString(), anyString(), any(Integer.class), any(Integer.class)))
                .thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");
        
        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }
}
