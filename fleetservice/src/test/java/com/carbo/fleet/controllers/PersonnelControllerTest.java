
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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

        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(personnelService.findAll(any(String.class), eq(0), eq(10)))
                .thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);

        // Assert
        assertNotNull(actualDisplay);
        assertEquals(expectedDisplay, actualDisplay);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        // Arrange
        String personnelId = "123";
        PersonnelDto expectedDto = new PersonnelDto();
        expectedDto.setId(personnelId);
        
        Mockito.when(personnelService.findById(eq(personnelId)))
                .thenReturn(expectedDto);

        MockHttpServletRequest request = new MockHttpServletRequest();

        // Act
        PersonnelDto actualDto = personnelController.getPersonnel(request, personnelId);

        // Assert
        assertNotNull(actualDto);
        assertEquals(expectedDto.getId(), actualDto.getId());
    }

    @Test
    public void shouldCreatePersonnelAndReturnSuccessMessage() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Manager")
                .employeeId("EMP123")
                .supervisor(true)
                .districtId("DIST001")
                .fleetId("FLEET001")
                .crewId("CREW001")
                .build();

        request.setUserPrincipal(() -> "testPrincipal");
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class)))
                .thenReturn(true);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Constants.PERSONNEL_CREATED, ((Map<String, String>) response.getBody()).get("successMessage"));
    }

    @Test
    public void shouldReturnConflictMessageWhenPersonnelAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Manager")
                .employeeId("EMP123")
                .supervisor(true)
                .districtId("DIST001")
                .fleetId("FLEET001")
                .crewId("CREW001")
                .build();

        request.setUserPrincipal(() -> "testPrincipal");
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class)))
                .thenReturn(false);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(Constants.PERSONNEL_ALREADY_EXISTS, ((Map<String, String>) response.getBody()).get("errorMessage"));
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedDto() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("123")
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Manager")
                .employeeId("EMP123")
                .supervisor(true)
                .districtId("DIST001")
                .fleetId("FLEET001")
                .crewId("CREW001")
                .build();

        request.setUserPrincipal(() -> "testPrincipal");
        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class)))
                .thenReturn(true);
        Mockito.when(personnelService.findById(eq("123")))
                .thenReturn(personnelDto);

        // Act
        PersonnelDto updatedDto = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertNotNull(updatedDto);
        assertEquals(personnelDto.getId(), updatedDto.getId());
    }

    @Test
    public void shouldDeletePersonnel() {
        // Arrange
        String personnelId = "123";
        
        MockHttpServletRequest request = new MockHttpServletRequest();

        // Act
        personnelController.deletePersonnel(personnelId);

        // Assert
        Mockito.verify(personnelService).deletePersonnel(eq(personnelId));
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");

        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(personnelService.findbyValue(any(String.class), any(String.class), any(String.class), any(String.class), eq(0), eq(10)))
                .thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "");

        // Assert
        assertNotNull(actualDisplay);
        assertEquals(expectedDisplay, actualDisplay);
    }
}
