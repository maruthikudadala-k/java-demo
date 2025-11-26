
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.services.CrewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> "testUser");

        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(crewService.findAll(eq(organizationId), anyInt(), anyInt())).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrew(request, 0, 10);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "testCrewId";
        CrewDto expectedCrewDto = new CrewDto();
        when(crewService.findById(crewId)).thenReturn(expectedCrewDto);

        CrewDto actualCrewDto = crewController.getCrew(request, crewId);

        assertEquals(expectedCrewDto, actualCrewDto);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> "testUser");

        CrewDto crewDto = new CrewDto();
        crewDto.setId("testCrewId");
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleetId");
        
        Crew crewNew = new Crew();
        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(crewNew);

        ResponseEntity<Object> responseEntity = crewController.createCrew(request, crewDto);

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals(crewNew, responseEntity.getBody());
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> "testUser");

        CrewDto crewDto = new CrewDto();
        crewDto.setId("testCrewId");
        crewDto.setName("Updated Crew");
        
        when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto actualCrewDto = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, actualCrewDto);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "testCrewId";

        crewController.deleteCrew(crewId);

        Mockito.verify(crewService, Mockito.times(1)).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "testOrgId";
        request.setUserPrincipal(() -> "testUser");
        
        CrewDisplayObject expectedCrewDisplayObject = new CrewDisplayObject();
        when(crewService.findAllByFleet(eq(organizationId), anyString(), anyInt(), anyInt())).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewController.getAllCrewByFleet(request, 0, 10, "testFleetName");

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }
}
