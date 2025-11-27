
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @InjectMocks
    private PersonnelController personnelController;

    @Mock
    private PersonnelService personnelService;

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnelIsCalled() {
        // Arrange
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay mockDisplay = PersonnelDisplay.builder().build();
        
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "principal");
        Mockito.when(personnelService.findAll(organizationId, offSet, limit)).thenReturn(mockDisplay);
        Mockito.when(getOrganizationId(request)).thenReturn(organizationId);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        // Assert
        assertNotNull(result);
        Mockito.verify(personnelService).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelIsCalled() {
        // Arrange
        String id = "personnelId";
        PersonnelDto mockPersonnelDto = new PersonnelDto();
        
        Mockito.when(personnelService.findById(id)).thenReturn(mockPersonnelDto);

        // Act
        PersonnelDto result = personnelController.getPersonnel(request, id);

        // Assert
        assertNotNull(result);
        Mockito.verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        // Arrange
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("org123");
        HashMap<String, String> message = new HashMap<>();
        message.put("successMessage", Constants.PERSONNEL_CREATED);
        Mockito.when(personnelService.savePersonnel(personnelDto)).thenReturn(true);
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "principal");
        Mockito.when(getOrganizationId(request)).thenReturn("org123");

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(message, response.getBody());
        Mockito.verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        // Arrange
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("org123");
        HashMap<String, String> message = new HashMap<>();
        message.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        Mockito.when(personnelService.savePersonnel(personnelDto)).thenReturn(false);
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "principal");
        Mockito.when(getOrganizationId(request)).thenReturn("org123");

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(message, response.getBody());
        Mockito.verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelIsCalled() {
        // Arrange
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("personnelId");
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "principal");
        Mockito.when(getOrganizationId(request)).thenReturn("org123");
        Mockito.when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        Mockito.when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertNotNull(result);
        Mockito.verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        // Arrange
        String id = "personnelId";

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        Mockito.verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterIsCalled() {
        // Arrange
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        String personnelName = "John Doe";
        String districtId = "dist123";
        String jobTitle = "Engineer";
        PersonnelDisplay mockDisplay = PersonnelDisplay.builder().build();
        
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "principal");
        Mockito.when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit)).thenReturn(mockDisplay);
        Mockito.when(getOrganizationId(request)).thenReturn(organizationId);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Assert
        assertNotNull(result);
        Mockito.verify(personnelService).findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);
    }
}
