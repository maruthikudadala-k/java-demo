
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import com.carbo.fleet.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public void shouldReturnAllPersonnelWhenGetAllPersonnelIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        when(personnelService.findAll(organizationId, offSet, limit)).thenReturn(personnelDisplay);

        // When
        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getPersonnelDisplayObject().size());
        verify(personnelService).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelIsCalled() {
        // Given
        String id = "123";
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.findById(id)).thenReturn(personnelDto);

        // When
        PersonnelDto result = personnelController.getPersonnel(mock(HttpServletRequest.class), id);

        // Then
        assertNotNull(result);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").secondName("Doe").jobTitle("Engineer").employeeId("emp123").supervisor(true).districtId("dist1").fleetId("fleet1").crewId("crew1").build();
        Map<String, String> message = new HashMap<>();
        message.put("successMessage", Constants.PERSONNEL_CREATED);
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        // When
        ResponseEntity<Object> responseEntity = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(message, responseEntity.getBody());
        verify(personnelService).savePersonnel(any(PersonnelDto.class));
    }

    @Test
    public void shouldConflictWhenPersonnelAlreadyExists() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").secondName("Doe").jobTitle("Engineer").employeeId("emp123").supervisor(true).districtId("dist1").fleetId("fleet1").crewId("crew1").build();
        Map<String, String> message = new HashMap<>();
        message.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        // When
        ResponseEntity<Object> responseEntity = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(message, responseEntity.getBody());
        verify(personnelService).savePersonnel(any(PersonnelDto.class));
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().id("123").firstName("John").secondName("Doe").jobTitle("Engineer").employeeId("emp123").supervisor(true).districtId("dist1").fleetId("fleet1").crewId("crew1").build();
        when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        // When
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Then
        assertNotNull(result);
        verify(personnelService).updatePersonnel(any(PersonnelDto.class));
        verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        // Given
        String id = "123";
        doNothing().when(personnelService).deletePersonnel(id);

        // When
        personnelController.deletePersonnel(id);

        // Then
        verify(personnelService).deletePersonnel(id);
    }
    
    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterIsCalled() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        String personnelName = "John";
        String districtId = "dist1";
        String jobTitle = "Engineer";
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build();
        when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit)).thenReturn(personnelDisplay);

        // When
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getPersonnelDisplayObject().size());
        verify(personnelService).findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);
    }
}
