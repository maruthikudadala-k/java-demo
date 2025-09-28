
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.services.CrewService;
import com.carbo.fleet.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CrewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    private ObjectMapper objectMapper;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(crewController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllCrew_Success() throws Exception {
        // Given
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject expectedResult = CrewDisplayObject.builder().build();

        when(crewService.findAll(eq(organizationId), eq(offSet), eq(limit))).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/v1/crew/")
                .param("offSet", "0")
                .param("limit", "10")
                .header("X-Organization-Id", organizationId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        verify(crewService).findAll(eq(organizationId), eq(offSet), eq(limit));
    }

    @Test
    void testGetAllCrew_WithDefaultParameters() throws Exception {
        // Given
        String organizationId = "org123";
        CrewDisplayObject expectedResult = CrewDisplayObject.builder().build();

        when(crewService.findAll(eq(organizationId), eq(0), eq(10))).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/v1/crew/")
                .header("X-Organization-Id", organizationId))
                .andExpect(status().isOk());

        verify(crewService).findAll(eq(organizationId), eq(0), eq(10));
    }

    @Test
    void testGetCrew_Success() throws Exception {
        // Given
        String crewId = "crew123";
        String organizationId = "org123";
        CrewDto expectedCrew = CrewDto.builder()
                .id(crewId)
                .name("Test Crew")
                .build();

        when(crewService.findById(eq(crewId))).thenReturn(expectedCrew);

        // When & Then
        mockMvc.perform(get("/v1/crew/{id}", crewId)
                .header("X-Organization-Id", organizationId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        verify(crewService).findById(eq(crewId));
    }

    @Test
    void testCreateCrew_Success() throws Exception {
        // Given
        String organizationId = "org123";
        CrewDto inputCrew = CrewDto.builder()
                .name("New Crew")
                .jobPattern("Pattern1")
                .shiftStart("08:00")
                .startDate("01/01/2024")
                .fleetId("fleet123")
                .build();

        Crew savedCrew = Crew.builder()
                .id("crew123")
                .name("New Crew")
                .organizationId(organizationId)
                .build();

        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(savedCrew);

        // When & Then
        mockMvc.perform(post("/v1/crew/")
                .header("X-Organization-Id", organizationId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(inputCrew)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"));

        verify(crewService).saveCrew(argThat(crew ->
            crew.getOrganizationId().equals(organizationId) &&
            crew.getName().equals("New Crew")
        ));
    }

    @Test
    void testCreateCrew_AlreadyExists() throws Exception {
        // Given
        String organizationId = "org123";
        CrewDto inputCrew = CrewDto.builder()
                .name("Existing Crew")
                .jobPattern("Pattern1")
                .shiftStart("08:00")
                .startDate("01/01/2024")
                .fleetId("fleet123")
                .build();

        when(crewService.saveCrew(any(CrewDto.class))).thenReturn(null);

        Map<String, String> expectedError = new HashMap<>();
        expectedError.put("errorMessage", Constants.CREW_ALREADY_EXISTS);

        // When & Then
        mockMvc.perform(post("/v1/crew/")
                .header("X-Organization-Id", organizationId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(inputCrew)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errorMessage").value(Constants.CREW_ALREADY_EXISTS));
    }

    @Test
    void testUpdateCrew_Success() throws Exception {
        // Given
        String organizationId = "org123";
        String crewId = "crew123";
        CrewDto inputCrew = CrewDto.builder()
                .id(crewId)
                .name("Updated Crew")
                .jobPattern("Pattern1")
                .shiftStart("08:00")
                .startDate("01/01/2024")
                .fleetId("fleet123")
                .build();

        when(crewService.updateCrew(any(CrewDto.class))).thenReturn(true);
        when(crewService.findById(eq(crewId))).thenReturn(inputCrew);

        // When & Then
        mockMvc.perform(put("/v1/crew/")
                .header("X-Organization-Id", organizationId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(inputCrew)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        verify(crewService).updateCrew(argThat(crew ->
            crew.getOrganizationId().equals(organizationId) &&
            crew.getId().equals(crewId)
        ));
        verify(crewService).findById(eq(crewId));
    }

    @Test
    void testDeleteCrew_Success() throws Exception {
        // Given
        String crewId = "crew123";

        // When & Then
        mockMvc.perform(delete("/v1/crew/{id}", crewId))
                .andExpect(status().isNoContent());

        verify(crewService).deleteCrew(eq(crewId));
    }

    @Test
    void testGetAllCrewByFleet_Success() throws Exception {
        // Given
        String organizationId = "org123";
        String fleetName = "Test Fleet";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject expectedResult = CrewDisplayObject.builder().build();

        when(crewService.findAllByFleet(eq(organizationId), eq(fleetName), eq(offSet), eq(limit)))
                .thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/v1/crew/getByFleet")
                .header("X-Organization-Id", organizationId)
                .param("fleetName", fleetName)
                .param("offSet", "0")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        verify(crewService).findAllByFleet(eq(organizationId), eq(fleetName), eq(offSet), eq(limit));
    }

    @Test
    void testGetAllCrewByFleet_WithDefaultPagination() throws Exception {
        // Given
        String organizationId = "org123";
        String fleetName = "Test Fleet";
        CrewDisplayObject expectedResult = CrewDisplayObject.builder().build();

        when(crewService.findAllByFleet(eq(organizationId), eq(fleetName), eq(0), eq(10)))
                .thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/v1/crew/getByFleet")
                .header("X-Organization-Id", organizationId)
                .param("fleetName", fleetName))
                .andExpect(status().isOk());

        verify(crewService).findAllByFleet(eq(organizationId), eq(fleetName), eq(0), eq(10));
    }

    @Test
    void testCreateCrew_ValidationError() throws Exception {
        // Given
        String organizationId = "org123";
        CrewDto invalidCrew = CrewDto.builder()
                .name("") // Invalid: empty name
                .jobPattern("Pattern1")
                .shiftStart("08:00")
                .startDate("01/01/2024")
                .fleetId("fleet123")
                .build();

        // When & Then
        mockMvc.perform(post("/v1/crew/")
                .header("X-Organization-Id", organizationId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(invalidCrew)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCrew_NotFound() throws Exception {
        // Given
        String crewId = "nonexistent";
        String organizationId = "org123";

        when(crewService.findById(eq(crewId))).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/v1/crew/{id}", crewId)
                .header("X-Organization-Id", organizationId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
