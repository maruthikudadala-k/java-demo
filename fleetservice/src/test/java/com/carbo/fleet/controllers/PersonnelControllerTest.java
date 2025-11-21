
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
        request.addHeader("Authorization", "Bearer token");
        String organizationId = "orgId";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        Mockito.when(personnelService.findAll(anyString(), Mockito.eq(offSet), Mockito.eq(limit)))
                .thenReturn(personnelDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        // Assert
        assertEquals(personnelDisplay, result);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnelById() {
        // Arrange
        String id = "personnelId";
        PersonnelDto personnelDto = new PersonnelDto();
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.getPersonnel(new MockHttpServletRequest(), id);

        // Assert
        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("orgId");
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);
        Map<String, String> expectedMessage = new HashMap<>();
        expectedMessage.put("successMessage", Constants.PERSONNEL_CREATED);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    public void shouldReturnConflictResponseWhenCreatingPersonnelFails() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);
        Map<String, String> expectedMessage = new HashMap<>();
        expectedMessage.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("personnelId");
        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldDeletePersonnel() {
        // Arrange
        String id = "personnelId";
        Mockito.doNothing().when(personnelService).deletePersonnel(anyString());

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        Mockito.verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "orgId";
        int offSet = 0;
        int limit = 10;
        String personnelName = "John Doe";
        String districtId = "districtId";
        String jobTitle = "Engineer";
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        Mockito.when(personnelService.findbyValue(anyString(), anyString(), anyString(), anyString(), Mockito.eq(offSet), Mockito.eq(limit)))
                .thenReturn(personnelDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Assert
        assertEquals(personnelDisplay, result);
    }
}
