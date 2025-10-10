
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
    public void shouldReturnAllPersonnelWhenGetAllPersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().build();
        when(personnelService.findAll(organizationId, offSet, limit)).thenReturn(personnelDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        // Assert
        assertEquals(personnelDisplay, result);
        verify(personnelService, times(1)).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelIsCalled() {
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
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        String organizationId = "org123";
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        personnelDto.setOrganizationId(organizationId);
        boolean status = true;
        Map<String, String> message = new HashMap<>();
        message.put("successMessage", Constants.PERSONNEL_CREATED);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(status);

        // Act
        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(message, result.getBody());
        verify(personnelService, times(1)).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        personnelDto.setOrganizationId("org123");
        boolean status = false;
        Map<String, String> message = new HashMap<>();
        message.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(status);

        // Act
        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(message, result.getBody());
        verify(personnelService, times(1)).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnelWhenUpdatePersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        PersonnelDto personnelDto = PersonnelDto.builder().id("personnelId").build();
        PersonnelDto updatedPersonnelDto = new PersonnelDto();
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(updatedPersonnelDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertEquals(updatedPersonnelDto, result);
        verify(personnelService, times(1)).updatePersonnel(personnelDto);
        verify(personnelService, times(1)).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        // Arrange
        String id = "personnelId";

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        verify(personnelService, times(1)).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        String personnelName = "John Doe";
        String districtId = "district123";
        String jobTitle = "Engineer";
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().build();
        when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit)).thenReturn(personnelDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Assert
        assertEquals(personnelDisplay, result);
        verify(personnelService, times(1)).findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);
    }
}
