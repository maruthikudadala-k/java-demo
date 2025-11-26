
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import com.carbo.fleet.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        request.setUserPrincipal(() -> organizationId);
        PersonnelDisplay expectedDisplay = new PersonnelDisplay();
        when(personnelService.findAll(organizationId, 0, 10)).thenReturn(expectedDisplay);

        // When
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);

        // Then
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnelById() {
        // Given
        String id = "person123";
        PersonnelDto expectedDto = new PersonnelDto();
        when(personnelService.findById(id)).thenReturn(expectedDto);
        MockHttpServletRequest request = new MockHttpServletRequest();

        // When
        PersonnelDto actualDto = personnelController.getPersonnel(request, id);

        // Then
        assertEquals(expectedDto, actualDto);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("org123");
        boolean status = true;
        HashMap<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("successMessage", Constants.PERSONNEL_CREATED);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(status);
        request.setUserPrincipal(() -> "org123");

        // When
        ResponseEntity<Object> actualResponse = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse.getBody());
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictResponseWhenPersonnelAlreadyExists() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("org123");
        boolean status = false;
        HashMap<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(status);
        request.setUserPrincipal(() -> "org123");

        // When
        ResponseEntity<Object> actualResponse = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CONFLICT, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse.getBody());
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedDto() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("person123");
        personnelDto.setOrganizationId("org123");
        PersonnelDto expectedDto = new PersonnelDto();
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(expectedDto);
        request.setUserPrincipal(() -> "org123");

        // When
        PersonnelDto actualDto = personnelController.updatePersonnel(request, personnelDto);

        // Then
        assertEquals(expectedDto, actualDto);
        verify(personnelService).updatePersonnel(personnelDto);
        verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelById() {
        // Given
        String id = "person123";

        // When
        personnelController.deletePersonnel(id);

        // Then
        verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDisplay expectedDisplay = new PersonnelDisplay();
        when(personnelService.findbyValue(organizationId, "", "", "", 0, 10)).thenReturn(expectedDisplay);

        // When
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "", "");

        // Then
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findbyValue(organizationId, "", "", "", 0, 10);
    }
}
