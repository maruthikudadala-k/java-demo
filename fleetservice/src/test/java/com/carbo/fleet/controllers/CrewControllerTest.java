
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
        
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        crewDisplayObject.setCrews(Collections.emptyList());
        crewDisplayObject.setTotalCount(0L);

        Mockito.when(crewService.findAll(anyString(), any(Integer.class), any(Integer.class)))
                .thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrew(request, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        Mockito.verify(crewService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnCrewWhenGetCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "crew123";
        CrewDto crewDto = new CrewDto();
        crewDto.setId(id);
        crewDto.setName("John Doe");
        crewDto.setJobPattern("Driver");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet123");

        Mockito.when(crewService.findById(anyString())).thenReturn(crewDto);

        CrewDto result = crewController.getCrew(request, id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        Mockito.verify(crewService).findById(id);
    }

    @Test
    public void shouldCreateCrewWhenCreateCrewIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        CrewDto crewDto = new CrewDto();
        crewDto.setName("John Doe");
        crewDto.setJobPattern("Driver");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet123");
        
        Crew crew = new Crew();
        crew.setId("crew123");
        crew.setName("John Doe");

        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(crew);

        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(crew, result.getBody());
        Mockito.verify(crewService).saveCrew(crewDto);
    }

    @Test
    public void shouldReturnConflictWhenCrewAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        CrewDto crewDto = new CrewDto();
        crewDto.setName("John Doe");
        crewDto.setJobPattern("Driver");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet123");
        
        Mockito.when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        ResponseEntity<Object> result = crewController.createCrew(request, crewDto);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertTrue(result.getBody() instanceof Map);
        Map<String, String> errorResponse = (Map<String, String>) result.getBody();
        assertEquals("Crew already exists", errorResponse.get("errorMessage"));
        Mockito.verify(crewService).saveCrew(crewDto);
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
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleet123");

        Mockito.when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        Mockito.when(crewService.findById(anyString())).thenReturn(crewDto);

        CrewDto result = crewController.updatePersonnel(request, crewDto);

        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
        Mockito.verify(crewService).updateCrew(crewDto);
        Mockito.verify(crewService).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String id = "crew123";
        crewController.deleteCrew(id);
        
        Mockito.verify(crewService).deleteCrew(id);
    }

    @Test
    public void shouldReturnCrewByFleetWhenGetAllCrewByFleetIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        CrewDisplayObject crewDisplayObject = new CrewDisplayObject();
        crewDisplayObject.setCrews(Collections.emptyList());
        crewDisplayObject.setTotalCount(0L);

        Mockito.when(crewService.findAllByFleet(anyString(), anyString(), any(Integer.class), any(Integer.class)))
                .thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewController.getAllCrewByFleet(request, 0, 10, "fleetName");

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        Mockito.verify(crewService).findAllByFleet(organizationId, "fleetName", 0, 10);
    }
}
