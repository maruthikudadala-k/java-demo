
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

import static org.junit.jupiter.api.Assertions.*;
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
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDisplay expectedDisplay = new PersonnelDisplay();
        when(personnelService.findAll(organizationId, 0, 10)).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "personnelId";
        PersonnelDto expectedDto = new PersonnelDto();
        when(personnelService.findById(id)).thenReturn(expectedDto);

        // Act
        PersonnelDto actualDto = personnelController.getPersonnel(request, id);

        // Assert
        assertEquals(expectedDto, actualDto);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelAndReturnSuccessMessage() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();
        
        personnelDto.setOrganizationId(organizationId);
        
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals(Constants.PERSONNEL_CREATED, responseBody.get("successMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictMessageWhenCreatingExistingPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();
        
        personnelDto.setOrganizationId(organizationId);
        
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals(Constants.PERSONNEL_ALREADY_EXISTS, responseBody.get("errorMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedDto() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("personnelId")
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();
        
        personnelDto.setOrganizationId(organizationId);
        
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        // Act
        PersonnelDto updatedDto = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertEquals(personnelDto, updatedDto);
        verify(personnelService).updatePersonnel(personnelDto);
        verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnel() {
        // Arrange
        String id = "personnelId";

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDisplay expectedDisplay = new PersonnelDisplay();
        when(personnelService.findbyValue(organizationId, "John", "dist123", "Engineer", 0, 10)).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "John", "dist123", "Engineer");

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findbyValue(organizationId, "John", "dist123", "Engineer", 0, 10);
    }
}
