
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
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        when(personnelService.findAll(eq(organizationId), eq(offSet), eq(limit))).thenReturn(personnelDisplay);

        // When
        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        // Then
        assertEquals(personnelDisplay, result);
        verify(personnelService).findAll(eq(organizationId), eq(offSet), eq(limit));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "personnelId";
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.findById(eq(id))).thenReturn(personnelDto);

        // When
        PersonnelDto result = personnelController.getPersonnel(request, id);

        // Then
        assertEquals(personnelDto, result);
        verify(personnelService).findById(eq(id));
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        boolean status = true;
        HashMap<String, String> message = new HashMap<>();
        message.put("successMessage", Constants.PERSONNEL_CREATED);
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(status);

        // When
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(message, response.getBody());
        verify(personnelService).savePersonnel(eq(personnelDto));
    }

    @Test
    public void shouldReturnConflictResponseWhenPersonnelAlreadyExists() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        boolean status = false;
        HashMap<String, String> message = new HashMap<>();
        message.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(status);

        // When
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(message, response.getBody());
        verify(personnelService).savePersonnel(eq(personnelDto));
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnelDto() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("personnelId");
        PersonnelDto updatedPersonnelDto = new PersonnelDto();
        when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        when(personnelService.findById(eq(personnelDto.getId()))).thenReturn(updatedPersonnelDto);

        // When
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Then
        assertEquals(updatedPersonnelDto, result);
        verify(personnelService).updatePersonnel(eq(personnelDto));
        verify(personnelService).findById(eq(personnelDto.getId()));
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelCalled() {
        // Given
        String id = "personnelId";

        // When
        personnelController.deletePersonnel(id);

        // Then
        verify(personnelService).deletePersonnel(eq(id));
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        String personnelName = "John";
        String districtId = "districtId";
        String jobTitle = "jobTitle";
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        when(personnelService.findbyValue(eq(organizationId), eq(personnelName), eq(districtId), eq(jobTitle), eq(offSet), eq(limit)))
                .thenReturn(personnelDisplay);

        // When
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Then
        assertEquals(personnelDisplay, result);
        verify(personnelService).findbyValue(eq(organizationId), eq(personnelName), eq(districtId), eq(jobTitle), eq(offSet), eq(limit));
    }
}
