
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
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
        request.addHeader("Authorization", "Bearer token");
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        when(personnelService.findAll(organizationId, offSet, limit)).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, offSet, limit);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnelById() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String personnelId = "personnel123";
        PersonnelDto expectedDto = PersonnelDto.builder().id(personnelId).firstName("John").secondName("Doe").build();
        when(personnelService.findById(personnelId)).thenReturn(expectedDto);

        // Act
        PersonnelDto actualDto = personnelController.getPersonnel(request, personnelId);

        // Assert
        assertEquals(expectedDto, actualDto);
        verify(personnelService).findById(personnelId);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedStatus() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").secondName("Doe").jobTitle("Engineer").employeeId("emp123").supervisor(false).districtId("dist1").fleetId("fleet1").crewId("crew1").build();
        String organizationId = "org123";
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof java.util.Map);
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictStatusWhenPersonnelAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").secondName("Doe").jobTitle("Engineer").employeeId("emp123").supervisor(false).districtId("dist1").fleetId("fleet1").crewId("crew1").build();
        String organizationId = "org123";
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof java.util.Map);
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnPersonnelDto() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().id("personnel123").firstName("John").secondName("Doe").jobTitle("Engineer").employeeId("emp123").supervisor(false).districtId("dist1").fleetId("fleet1").crewId("crew1").build();
        String organizationId = "org123";
        personnelDto.setOrganizationId(organizationId);
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
        int offSet = 0;
        int limit = 10;
        String personnelName = "John";
        String districtId = "dist1";
        String jobTitle = "Engineer";
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit)).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);
    }
}
