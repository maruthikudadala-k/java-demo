
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;

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
    public void shouldReturnAllPersonnelWhenGetAllPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        
        when(personnelService.findAll(any(), anyInt(), anyInt())).thenReturn(expectedDisplay);
        
        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);
        
        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findAll(eq(organizationId), eq(0), eq(10));
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelById() {
        // Arrange
        String id = "personnelId";
        PersonnelDto expectedDto = new PersonnelDto();
        expectedDto.setId(id);
        
        when(personnelService.findById(id)).thenReturn(expectedDto);
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        // Act
        PersonnelDto actualDto = personnelController.getPersonnel(request, id);
        
        // Assert
        assertEquals(expectedDto, actualDto);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelWhenPostCreatePersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("org123");
        
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);
        
        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);
        
        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof java.util.Map);
        assertEquals("personnel_created", ((java.util.Map) response.getBody()).get("successMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExistsOnCreate() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("org123");
        
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);
        
        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);
        
        // Assert
        assertEquals(409, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof java.util.Map);
        assertEquals("Personnel already exists", ((java.util.Map) response.getBody()).get("errorMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelWhenPutUpdatePersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("personnelId");
        
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);
        
        // Act
        PersonnelDto actualDto = personnelController.updatePersonnel(request, personnelDto);
        
        // Assert
        assertEquals(personnelDto, actualDto);
        verify(personnelService).updatePersonnel(personnelDto);
        verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnel() {
        // Arrange
        String id = "personnelId";
        
        // Act
        personnelController.deletePersonnel(id);
        
        // Assert
        verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilter() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        
        when(personnelService.findbyValue(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(expectedDisplay);
        
        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "", "");
        
        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findbyValue(eq(organizationId), any(), any(), any(), eq(0), eq(10));
    }
}
