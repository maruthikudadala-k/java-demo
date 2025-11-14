
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
        request.setUserPrincipal(() -> "test-user");

        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        crewDisplayObject.setCrews(Collections.emptyList());
        crewDisplayObject.setTotalCount(0);

        when(crewService.findAll(anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        verify(crewService).findAll(anyString(), anyInt(), anyInt());
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        String id = "1";
        CrewDto crewDto = new CrewDto();
        crewDto.setId(id);
        
        when(crewService.findById(id)).thenReturn(crewDto);

        MockHttpServletRequest request = new MockHttpServletRequest();
        CrewDto result = crewController.getCrew(request, id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(crewService).findById(id);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "test-user");

        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("org1");

        Crew crew = new Crew();
        crew.setId("1");

        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(crew);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(crew, response.getBody());
        verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExistsOnCreateCrew() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "test-user");

        CrewDto crewDto = new CrewDto();
        crewDto.setOrganizationId("org1");

        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> response = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof HashMap);
        assertEquals(Constants.CREW_ALREADY_EXISTS, ((HashMap) response.getBody()).get("errorMessage"));
        verify(crewService).saveCrew(any(CrewDto.class));
    }

    @Test
    public void shouldUpdateCrewWhenUpdatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "test-user");

        CrewDto crewDto = new CrewDto();
        crewDto.setId("1");
        crewDto.setOrganizationId("org1");

        when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        when(crewService.findById(crewDto.getId())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
        verify(crewService).updateCrew(any(CrewDto.class));
        verify(crewService).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String id = "1";
        when(crewService.findById(id)).thenReturn(Optional.of(new Crew()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        crewController.deleteCrew(id);

        verify(crewService).deleteCrew(id);
    }

    @Test
    public void shouldReturnAllCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        String fleetName = "Fleet1";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "test-user");

        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        crewDisplayObject.setCrews(Collections.emptyList());
        crewDisplayObject.setTotalCount(0);

        when(crewService.findAllByFleet(anyString(), anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, fleetName);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        verify(crewService).findAllByFleet(anyString(), anyString(), anyInt(), anyInt());
    }
}
