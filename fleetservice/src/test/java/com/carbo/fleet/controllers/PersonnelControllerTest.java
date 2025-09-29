
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnel() {
        // Arrange
        String organizationId = "org123";
        request.addHeader("Authorization", "Bearer token");
        when(personnelService.findAll(organizationId, 0, 10)).thenReturn(new PersonnelDisplay());

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        // Assert
        assertEquals(new PersonnelDisplay(), result);
        verify(personnelService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelById() {
        // Arrange
        String personnelId = "pers123";
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.findById(personnelId)).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.getPersonnel(request, personnelId);

        // Assert
        assertEquals(personnelDto, result);
        verify(personnelService).findById(personnelId);
    }

    @Test
    public void shouldCreatePersonnelWhenValidPostRequest() {
        // Arrange
        request.addHeader("Authorization", "Bearer token");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("org123");
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("personnel_created", ((HashMap<String, String>) response.getBody()).get("successMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        // Arrange
        request.addHeader("Authorization", "Bearer token");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("org123");
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Personnel already exists", ((HashMap<String, String>) response.getBody()).get("errorMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelWhenValidPutRequest() {
        // Arrange
        request.addHeader("Authorization", "Bearer token");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("pers123");
        personnelDto.setOrganizationId("org123");
        when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        when(personnelService.findById("pers123")).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertEquals(personnelDto, result);
        verify(personnelService).updatePersonnel(personnelDto);
        verify(personnelService).findById("pers123");
    }

    @Test
    public void shouldDeletePersonnelWhenValidId() {
        // Arrange
        String personnelId = "pers123";

        // Act
        personnelController.deletePersonnel(personnelId);

        // Assert
        verify(personnelService).deletePersonnel(personnelId);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilter() {
        // Arrange
        String organizationId = "org123";
        request.addHeader("Authorization", "Bearer token");
        when(personnelService.findbyValue(any(String.class), any(String.class), any(String.class), any(String.class), anyInt(), anyInt()))
                .thenReturn(new PersonnelDisplay());

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "John", "district123", "Engineer");

        // Assert
        assertEquals(new PersonnelDisplay(), result);
        verify(personnelService).findbyValue(organizationId, "John", "district123", "Engineer", 0, 10);
    }
}
