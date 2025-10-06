
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnelCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        String organizationId = "org123";
        when(personnelService.findAll(organizationId, 0, 10)).thenReturn(new PersonnelDisplay());

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        // Assert
        assertEquals(new PersonnelDisplay(), result);
        verify(personnelService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "personnelId";
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.findById(id)).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.getPersonnel(request, id);

        // Assert
        assertEquals(personnelDto, result);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.addHeader("Authorization", "Bearer token");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        // Act
        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        // Assert
        Map<String, String> expectedMessage = new HashMap<>();
        expectedMessage.put("successMessage", Constants.PERSONNEL_CREATED);
        assertEquals(new ResponseEntity<>(expectedMessage, HttpStatus.CREATED), result);
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        // Act
        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        // Assert
        Map<String, String> expectedMessage = new HashMap<>();
        expectedMessage.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        assertEquals(new ResponseEntity<>(expectedMessage, HttpStatus.CONFLICT), result);
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.addHeader("Authorization", "Bearer token");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertEquals(personnelDto, result);
        verify(personnelService).updatePersonnel(personnelDto);
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelCalled() {
        // Arrange
        String id = "personnelId";

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.addHeader("Authorization", "Bearer token");
        String personnelName = "John";
        String districtId = "district1";
        String jobTitle = "Engineer";
        when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, 0, 10)).thenReturn(new PersonnelDisplay());

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, personnelName, districtId, jobTitle);

        // Assert
        assertEquals(new PersonnelDisplay(), result);
        verify(personnelService).findbyValue(organizationId, personnelName, districtId, jobTitle, 0, 10);
    }
}
