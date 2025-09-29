
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        Mockito.when(request.getAttribute("organizationId")).thenReturn(organizationId);
        Mockito.when(personnelService.findAll(eq(organizationId), eq(offSet), eq(limit))).thenReturn(personnelDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPersonnelDisplayObject().size());
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnelIsCalled() {
        // Arrange
        String id = "personnelId";
        PersonnelDto personnelDto = PersonnelDto.builder().id(id).firstName("John").build();
        Mockito.when(personnelService.findById(eq(id))).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.getPersonnel(request, id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        // Arrange
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").build();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("org123");
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        // Arrange
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").build();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("org123");
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelIsCalled() {
        // Arrange
        PersonnelDto personnelDto = PersonnelDto.builder().id("personnelId").firstName("John").build();
        Mockito.when(request.getAttribute("organizationId")).thenReturn("org123");
        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        Mockito.when(personnelService.findById(eq(personnelDto.getId()))).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertNotNull(result);
        assertEquals(personnelDto.getId(), result.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        // Arrange
        String id = "personnelId";

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        Mockito.verify(personnelService).deletePersonnel(eq(id));
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterIsCalled() {
        // Arrange
        String organizationId = "org123";
        String personnelName = "John";
        String districtId = "district1";
        String jobTitle = "Engineer";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        Mockito.when(request.getAttribute("organizationId")).thenReturn(organizationId);
        Mockito.when(personnelService.findbyValue(eq(organizationId), eq(personnelName), eq(districtId), eq(jobTitle), eq(offSet), eq(limit))).thenReturn(personnelDisplay);

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPersonnelDisplayObject().size());
    }
}
