
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
        request.setUserPrincipal(() -> "user");
        Mockito.when(crewService.findAll(eq(organizationId), any(Integer.class), any(Integer.class)))
                .thenReturn(new CrewDisplayObject(Collections.emptyList(), 0));

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        Mockito.verify(crewService).findAll(eq(organizationId), eq(0), eq(10));
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "crew123";
        CrewDto crewDto = new CrewDto();
        crewDto.setId(id);
        Mockito.when(crewService.findById(eq(id))).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        Mockito.verify(crewService).findById(eq(id));
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto crewDto = new CrewDto();
        crewDto.setName("Test Crew");
        crewDto.setJobPattern("Job Pattern");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2022");
        crewDto.setFleetId("fleet123");
        
        String organizationId = "org123";
        request.setUserPrincipal(() -> "user");
        crewDto.setOrganizationId(organizationId);
        
        Crew crew = new Crew();
        crew.setId("crew123");
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(crew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());
        Mockito.verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> "user");
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crew123");
        crewDto.setName("Updated Crew");
        crewDto.setJobPattern("Updated Job Pattern");
        crewDto.setShiftStart("09:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet123");
        
        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        Mockito.when(crewService.findById(eq(crewDto.getId()))).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
        Mockito.verify(crewService).updateCrew(any(CrewDto.class));
        Mockito.verify(crewService).findById(eq(crewDto.getId()));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String id = "crew123";

        crewController.deleteCrew(id);

        Mockito.verify(crewService).deleteCrew(eq(id));
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> "user");
        String fleetName = "fleetName";

        Mockito.when(crewService.findAllByFleet(eq(organizationId), eq(fleetName), any(Integer.class), any(Integer.class)))
                .thenReturn(new CrewDisplayObject(Collections.emptyList(), 0));

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        Mockito.verify(crewService).findAllByFleet(eq(organizationId), eq(fleetName), eq(0), eq(10));
    }
}
