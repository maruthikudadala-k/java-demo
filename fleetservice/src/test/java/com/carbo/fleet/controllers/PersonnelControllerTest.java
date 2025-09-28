
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import com.carbo.fleet.utils.Constants;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        request.setUserPrincipal(() -> "testUser");
        
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();
        
        when(personnelService.findAll(any(String.class), any(Integer.class), any(Integer.class))).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDisplay, result);
        verify(personnelService, times(1)).findAll(any(String.class), any(Integer.class), any(Integer.class));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        // Arrange
        String id = "1";
        PersonnelDto expectedDto = new PersonnelDto();
        when(personnelService.findById(id)).thenReturn(expectedDto);

        MockHttpServletRequest request = new MockHttpServletRequest();

        // Act
        PersonnelDto result = personnelController.getPersonnel(request, id);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(personnelService, times(1)).findById(id);
    }

    @Test
    public void shouldCreatePersonnelAndReturnSuccessMessage() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Developer")
                .employeeId("123")
                .supervisor(false)
                .districtId("district1")
                .fleetId("fleet1")
                .crewId("crew1")
                .build();

        HashMap<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("successMessage", Constants.PERSONNEL_CREATED);
        
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(personnelService, times(1)).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictMessageWhenPersonnelAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Developer")
                .employeeId("123")
                .supervisor(false)
                .districtId("district1")
                .fleetId("fleet1")
                .crewId("crew1")
                .build();

        HashMap<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(personnelService, times(1)).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedDto() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("1")
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Developer")
                .employeeId("123")
                .supervisor(false)
                .districtId("district1")
                .fleetId("fleet1")
                .crewId("crew1")
                .build();

        PersonnelDto expectedDto = new PersonnelDto();
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(expectedDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(personnelService, times(1)).updatePersonnel(personnelDto);
        verify(personnelService, times(1)).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnel() {
        // Arrange
        String id = "1";

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        verify(personnelService, times(1)).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.findbyValue(any(String.class), any(String.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "John", "districtId", "jobTitle");

        // Assert
        assertNotNull(result);
        assertEquals(expectedDisplay, result);
        verify(personnelService, times(1)).findbyValue(any(String.class), any(String.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class));
    }
}
