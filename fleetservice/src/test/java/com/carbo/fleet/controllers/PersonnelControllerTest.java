
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import com.carbo.fleet.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        request.setUserPrincipal(() -> "testPrincipal");
        String organizationId = "orgId";
        when(personnelService.findAll(organizationId, 0, 10)).thenReturn(new PersonnelDisplay());

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        // Assert
        assertNotNull(result);
        verify(personnelService, times(1)).findAll(organizationId, 0, 10);
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
        verify(personnelService, times(1)).findById(id);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId";
        request.setUserPrincipal(() -> "testPrincipal");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, String> message = (Map<String, String>) response.getBody();
        assertEquals(Constants.PERSONNEL_CREATED, message.get("successMessage"));
        verify(personnelService, times(1)).savePersonnel(any(PersonnelDto.class));
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId";
        request.setUserPrincipal(() -> "testPrincipal");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> message = (Map<String, String>) response.getBody();
        assertEquals(Constants.PERSONNEL_ALREADY_EXISTS, message.get("errorMessage"));
        verify(personnelService, times(1)).savePersonnel(any(PersonnelDto.class));
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId";
        request.setUserPrincipal(() -> "testPrincipal");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("personnelId");
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        when(personnelService.findById(any(String.class))).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertEquals(personnelDto, result);
        verify(personnelService, times(1)).updatePersonnel(any(PersonnelDto.class));
        verify(personnelService, times(1)).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelCalled() {
        // Arrange
        String id = "personnelId";

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        verify(personnelService, times(1)).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId";
        request.setUserPrincipal(() -> "testPrincipal");
        String personnelName = "John";
        String districtId = "districtId";
        String jobTitle = "jobTitle";
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, 0, 10)).thenReturn(personnelDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, personnelName, districtId, jobTitle);

        // Assert
        assertNotNull(result);
        verify(personnelService, times(1)).findbyValue(organizationId, personnelName, districtId, jobTitle, 0, 10);
    }
}
