
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        String organizationId = "testOrgId";
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder().build();
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(personnelService.findAll(eq(organizationId), anyInt(), anyInt())).thenReturn(expectedDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        assertEquals(expectedDisplay, result);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        String id = "testId";
        PersonnelDto expectedPersonnel = PersonnelDto.builder().id(id).build();
        when(personnelService.findById(id)).thenReturn(expectedPersonnel);

        PersonnelDto result = personnelController.getPersonnel(request, id);

        assertEquals(expectedPersonnel, result);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        String organizationId = "testOrgId";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("personnel_created", ((ResponseEntity<?>) response).getBody().get("successMessage"));
    }

    @Test
    public void shouldReturnConflictResponseWhenPersonnelAlreadyExists() {
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        String organizationId = "testOrgId";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Personnel already exists", ((ResponseEntity<?>) response).getBody().get("errorMessage"));
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnelDto() {
        PersonnelDto personnelDto = PersonnelDto.builder().id("testId").build();
        String organizationId = "testOrgId";
        PersonnelDto updatedPersonnel = PersonnelDto.builder().id("testId").build();
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(updatedPersonnel);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(updatedPersonnel, result);
    }

    @Test
    public void shouldDeletePersonnel() {
        String id = "testId";

        personnelController.deletePersonnel(id);

        Mockito.verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        String organizationId = "testOrgId";
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder().build();
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(personnelService.findbyValue(eq(organizationId), anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(expectedDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "", "");

        assertEquals(expectedDisplay, result);
    }
}
