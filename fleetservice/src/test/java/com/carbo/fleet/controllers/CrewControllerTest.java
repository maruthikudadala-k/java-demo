
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;

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
    public void shouldReturnAllCrewWhenGetAllCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDisplayObject mockCrewDisplayObject = new CrewDisplayObject();
        mockCrewDisplayObject.setCrews(Collections.emptyList());
        mockCrewDisplayObject.setTotalCount(0);

        when(crewService.findAll(organizationId, 0, 10)).thenReturn(mockCrewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertEquals(mockCrewDisplayObject, result);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String crewId = "crew123";

        CrewDto mockCrewDto = new CrewDto();
        mockCrewDto.setId(crewId);
        mockCrewDto.setName("John Doe");
        mockCrewDto.setJobPattern("Driver");
        mockCrewDto.setShiftStart("08:00");
        mockCrewDto.setStartDate("01/01/2021");
        mockCrewDto.setFleetId("fleet123");

        when(crewService.findById(crewId)).thenReturn(mockCrewDto);

        CrewDto result = crewController.getCrew(request, crewId);

        assertEquals(mockCrewDto, result);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("John Doe");
        crewDto.setJobPattern("Driver");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2021");
        crewDto.setFleetId("fleet123");

        Crew mockCrew = new Crew();
        mockCrew.setId(crewDto.getId());
        mockCrew.setName(crewDto.getName());

        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(mockCrew);

        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(mockCrew, result.getBody());
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("John Doe");
        crewDto.setJobPattern("Driver");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2021");
        crewDto.setFleetId("fleet123");

        when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertEquals(crewDto, result);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "crew123";

        crewController.deleteCrew(crewId);

        Mockito.verify(crewService).deleteCrew(crewId);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        CrewDisplayObject mockCrewDisplayObject = new CrewDisplayObject();
        mockCrewDisplayObject.setCrews(Collections.emptyList());
        mockCrewDisplayObject.setTotalCount(0);

        when(crewService.findAllByFleet(organizationId, "fleetName", 0, 10)).thenReturn(mockCrewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");

        assertEquals(mockCrewDisplayObject, result);
    }
}
