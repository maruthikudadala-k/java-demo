
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
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

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org-id";
        ((MockHttpServletRequest) request).addHeader("Authorization", "Bearer token");
        Mockito.when(personnelService.findAll(anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build());

        // When
        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        Mockito.verify(personnelService, Mockito.times(1)).findAll(anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnelById() {
        // Given
        String personnelId = "personnel-id";
        PersonnelDto personnelDto = PersonnelDto.builder().id(personnelId).firstName("John").build();
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        // When
        PersonnelDto result = personnelController.getPersonnel(new MockHttpServletRequest(), personnelId);

        // Then
        assertNotNull(result);
        assertEquals(personnelId, result.getId());
        Mockito.verify(personnelService, Mockito.times(1)).findById(anyString());
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").build();
        Mockito.when(personnelService.savePersonnel(any())).thenReturn(true);
        ((MockHttpServletRequest) request).addHeader("Authorization", "Bearer token");

        // When
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof java.util.Map);
        assertEquals(Constants.PERSONNEL_CREATED, ((java.util.Map) response.getBody()).get("successMessage"));
        Mockito.verify(personnelService, Mockito.times(1)).savePersonnel(any());
    }

    @Test
    public void shouldReturnConflictResponseWhenPersonnelAlreadyExists() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").build();
        Mockito.when(personnelService.savePersonnel(any())).thenReturn(false);
        ((MockHttpServletRequest) request).addHeader("Authorization", "Bearer token");

        // When
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof java.util.Map);
        assertEquals(Constants.PERSONNEL_ALREADY_EXISTS, ((java.util.Map) response.getBody()).get("errorMessage"));
        Mockito.verify(personnelService, Mockito.times(1)).savePersonnel(any());
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnelDto() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().id("personnel-id").firstName("John").build();
        Mockito.when(personnelService.updatePersonnel(any())).thenReturn(true);
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);
        ((MockHttpServletRequest) request).addHeader("Authorization", "Bearer token");

        // When
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Then
        assertNotNull(result);
        assertEquals(personnelDto.getId(), result.getId());
        Mockito.verify(personnelService, Mockito.times(1)).updatePersonnel(any());
        Mockito.verify(personnelService, Mockito.times(1)).findById(anyString());
    }

    @Test
    public void shouldDeletePersonnel() {
        // Given
        String personnelId = "personnel-id";

        // When
        personnelController.deletePersonnel(personnelId);

        // Then
        Mockito.verify(personnelService, Mockito.times(1)).deletePersonnel(anyString());
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org-id";
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        Mockito.when(personnelService.findbyValue(anyString(), anyString(), anyString(), anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(personnelDisplay);
        ((MockHttpServletRequest) request).addHeader("Authorization", "Bearer token");

        // When
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "", "");

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        Mockito.verify(personnelService, Mockito.times(1)).findbyValue(anyString(), anyString(), anyString(), anyString(), Mockito.anyInt(), Mockito.anyInt());
    }
}
