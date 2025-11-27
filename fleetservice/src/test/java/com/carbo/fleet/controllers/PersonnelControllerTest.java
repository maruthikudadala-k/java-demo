
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
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        when(personnelService.findAll(any(), anyInt(), anyInt())).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findAll(any(), eq(0), eq(10));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "someId";
        PersonnelDto expectedDto = PersonnelDto.builder().build();
        when(personnelService.findById(id)).thenReturn(expectedDto);

        // Act
        PersonnelDto actualDto = personnelController.getPersonnel(request, id);

        // Assert
        assertEquals(expectedDto, actualDto);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        assertEquals(Constants.PERSONNEL_CREATED, ((Map<String, String>) response.getBody()).get("successMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictResponseWhenPersonnelAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        assertEquals(Constants.PERSONNEL_ALREADY_EXISTS, ((Map<String, String>) response.getBody()).get("errorMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedDto() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().id("someId").build();
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
        String id = "someId";
        
        // Act
        personnelController.deletePersonnel(id);

        // Assert
        verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String personnelName = "John Doe";
        String districtId = "district123";
        String jobTitle = "Engineer";
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        when(personnelService.findbyValue(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, personnelName, districtId, jobTitle);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findbyValue(any(), eq(personnelName), eq(districtId), eq(jobTitle), eq(0), eq(10));
    }
}
