
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @Test
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        expectedCrewDisplayObject.setCrews(Collections.emptyList());
        expectedCrewDisplayObject.setTotalCount(0);

        when(crewService.findAll(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, 0, 10);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        String crewId = "1";
        CrewDto expectedCrewDto = new CrewDto();
        expectedCrewDto.setId(crewId);
        expectedCrewDto.setName("Test Crew");
        expectedCrewDto.setJobPattern("Pattern");
        expectedCrewDto.setShiftStart("08:00");
        expectedCrewDto.setStartDate("01/01/2022");
        expectedCrewDto.setFleetId("Fleet1");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        when(crewService.findById(crewId)).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.getCrew(request, crewId);

        assertEquals(expectedCrewDto, actualCrewDto);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        CrewDto crewDto = new CrewDto();
        crewDto.setName("New Crew");
        crewDto.setJobPattern("Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2022");
        crewDto.setFleetId("Fleet1");

        Crew expectedCrew = new Crew();
        expectedCrew.setId("1");
        expectedCrew.setName(crewDto.getName());

        when(crewService.saveCrew(Mockito.any(CrewDto.class))).thenReturn(expectedCrew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedCrew, response.getBody());
    }

    @Test
    public void shouldReturnConflictWhenCreatingCrewFails() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        CrewDto crewDto = new CrewDto();
        crewDto.setName("New Crew");
        crewDto.setJobPattern("Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2022");
        crewDto.setFleetId("Fleet1");

        when(crewService.saveCrew(Mockito.any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        Map<String, String> expectedError = new HashMap<>();
        expectedError.put("errorMessage", Constants.CREW_ALREADY_EXISTS);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedError, response.getBody());
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setName("Updated Crew");
        crewDto.setJobPattern("Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2022");
        crewDto.setFleetId("Fleet1");

        when(crewService.updateCrew(Mockito.any(CrewDto.class))).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto updatedCrewDto = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, updatedCrewDto);
    }

    @Test
    public void shouldReturnNullWhenUpdatingCrewFails() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");

        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");

        when(crewService.updateCrew(Mockito.any(CrewDto.class))).thenReturn(false);

        CrewDto updatedCrewDto = crewController.updatePersonnel(request, crewDto);

        assertNull(updatedCrewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "1";

        crewController.deleteCrew(crewId);

        Mockito.verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        expectedCrewDisplayObject.setCrews(Collections.emptyList());
        expectedCrewDisplayObject.setTotalCount(0);

        when(crewService.findAllByFleet(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, 0, 10, "FleetName");

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }
}
