
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
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");
        String organizationId = "org123";
        when(personnelService.findAll(organizationId, 0, 10)).thenReturn(new PersonnelDisplay());

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        // Assert
        assertNotNull(result);
        verify(personnelService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String personnelId = "personnel123";
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.findById(personnelId)).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.getPersonnel(request, personnelId);

        // Assert
        assertNotNull(result);
        verify(personnelService).findById(personnelId);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Developer")
                .employeeId("EMP123")
                .supervisor(true)
                .districtId("DIST123")
                .fleetId("FLEET123")
                .crewId("CREW123")
                .build();
        request.setUserPrincipal(() -> "testPrincipal");
        String organizationId = "org123";
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        // Act
        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Map<String, String> message = (Map<String, String>) result.getBody();
        assertEquals(Constants.PERSONNEL_CREATED, message.get("successMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("Jane")
                .secondName("Doe")
                .jobTitle("Manager")
                .employeeId("EMP456")
                .supervisor(false)
                .districtId("DIST456")
                .fleetId("FLEET456")
                .crewId("CREW456")
                .build();
        request.setUserPrincipal(() -> "testPrincipal");
        String organizationId = "org456";
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        // Act
        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        Map<String, String> message = (Map<String, String>) result.getBody();
        assertEquals(Constants.PERSONNEL_ALREADY_EXISTS, message.get("errorMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnPersonnelDto() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("personnel123")
                .firstName("Updated")
                .secondName("Name")
                .jobTitle("Senior Developer")
                .employeeId("EMP789")
                .supervisor(false)
                .districtId("DIST789")
                .fleetId("FLEET789")
                .crewId("CREW789")
                .build();
        request.setUserPrincipal(() -> "testPrincipal");
        String organizationId = "org789";
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertNotNull(result);
        assertEquals(personnelDto, result);
        verify(personnelService).updatePersonnel(personnelDto);
        verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnel() {
        // Arrange
        String personnelId = "personnel123";

        // Act
        personnelController.deletePersonnel(personnelId);

        // Assert
        verify(personnelService).deletePersonnel(personnelId);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        when(personnelService.findbyValue(organizationId, "John", "DIST123", "Developer", 0, 10)).thenReturn(new PersonnelDisplay());

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "John", "DIST123", "Developer");

        // Assert
        assertNotNull(result);
        verify(personnelService).findbyValue(organizationId, "John", "DIST123", "Developer", 0, 10);
    }
}
