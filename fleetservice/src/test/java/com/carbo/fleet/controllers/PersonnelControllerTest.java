
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

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
    public void shouldReturnAllPersonnelWhenGetAllPersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        Mockito.when(personnelService.findAll(anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelIsCalled() {
        // Arrange
        String id = "1";
        PersonnelDto expectedDto = PersonnelDto.builder().id(id).firstName("John").secondName("Doe").build();
        Mockito.when(personnelService.findById(anyString())).thenReturn(expectedDto);

        // Act
        PersonnelDto actualDto = personnelController.getPersonnel(new MockHttpServletRequest(), id);

        // Assert
        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").secondName("Doe").build();
        personnelDto.setOrganizationId("orgId");
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("personnel_created", ((Map<String, String>) responseEntity.getBody()).get("successMessage"));
    }

    @Test
    public void shouldReturnConflictWhenCreatePersonnelFails() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").secondName("Doe").build();
        personnelDto.setOrganizationId("orgId");
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Personnel already exists", ((Map<String, String>) responseEntity.getBody()).get("errorMessage"));
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().id("1").firstName("John").secondName("Doe").build();
        personnelDto.setOrganizationId("orgId");
        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        // Act
        PersonnelDto actualDto = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertEquals(personnelDto, actualDto);
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        // Arrange
        String id = "1";
        Mockito.doNothing().when(personnelService).deletePersonnel(anyString());

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        Mockito.verify(personnelService, Mockito.times(1)).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        Mockito.when(personnelService.findbyValue(anyString(), anyString(), anyString(), anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "John", "district1", "JobTitle");

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
    }
}
