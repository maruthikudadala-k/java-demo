
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().build();
        
        Mockito.when(personnelService.findAll(anyString(), Mockito.eq(offSet), Mockito.eq(limit))).thenReturn(personnelDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        // Assert
        assertNotNull(result);
        assertEquals(personnelDisplay, result);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelCalled() {
        // Arrange
        String id = "personnelId";
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.getPersonnel(new MockHttpServletRequest(), id);

        // Assert
        assertNotNull(result);
        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        Map<String, String> message = new HashMap<>();
        message.put("successMessage", Constants.PERSONNEL_CREATED);
        
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(message, responseEntity.getBody());
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExistsOnCreatePersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        Map<String, String> message = new HashMap<>();
        message.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(message, responseEntity.getBody());
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().id("personnelId").build();
        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertNotNull(result);
        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelCalled() {
        // Arrange
        String id = "personnelId";

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        Mockito.verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        String personnelName = "John Doe";
        String districtId = "district123";
        String jobTitle = "Engineer";
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().build();
        
        Mockito.when(personnelService.findbyValue(anyString(), anyString(), anyString(), anyString(), Mockito.eq(offSet), Mockito.eq(limit))).thenReturn(personnelDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Assert
        assertNotNull(result);
        assertEquals(personnelDisplay, result);
    }
}
