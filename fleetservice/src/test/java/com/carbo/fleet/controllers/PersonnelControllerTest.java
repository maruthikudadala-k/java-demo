
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

import javax.validation.Valid;
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
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = new PersonnelDisplay();
        when(personnelService.findAll(organizationId, offSet, limit)).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, offSet, limit);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService, times(1)).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "123";
        PersonnelDto expectedDto = new PersonnelDto();
        when(personnelService.findById(id)).thenReturn(expectedDto);

        // Act
        PersonnelDto actualDto = personnelController.getPersonnel(request, id);

        // Assert
        assertEquals(expectedDto, actualDto);
        verify(personnelService, times(1)).findById(id);
    }

    @Test
    public void shouldCreatePersonnelWhenPostPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("orgId");
        boolean status = true;
        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("successMessage", Constants.PERSONNEL_CREATED);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(status);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(personnelService, times(1)).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("orgId");
        boolean status = false;
        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(status);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(personnelService, times(1)).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdateAndReturnPersonnelDtoWhenPutPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("123");
        PersonnelDto expectedDto = new PersonnelDto();
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(expectedDto);

        // Act
        PersonnelDto actualDto = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertEquals(expectedDto, actualDto);
        verify(personnelService, times(1)).updatePersonnel(personnelDto);
        verify(personnelService, times(1)).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnel() {
        // Arrange
        String id = "123";
        doNothing().when(personnelService).deletePersonnel(id);

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        verify(personnelService, times(1)).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId";
        String personnelName = "John";
        String districtId = "districtId";
        String jobTitle = "Engineer";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = new PersonnelDisplay();
        when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit)).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService, times(1)).findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);
    }
}
