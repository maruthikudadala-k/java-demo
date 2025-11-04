
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();
        when(personnelService.findAll(organizationId, 0, 10)).thenReturn(expectedDisplay);

        // When
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);

        // Then
        assertEquals(expectedDisplay, actualDisplay);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnelById() {
        // Given
        String id = "personnelId";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();
        when(personnelService.findById(id)).thenReturn(expectedDto);

        // When
        PersonnelDto actualDto = personnelController.getPersonnel(new MockHttpServletRequest(), id);

        // Then
        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedMessage() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();
        personnelDto.setOrganizationId(organizationId);
        Map<String, String> expectedMessage = new HashMap<>();
        expectedMessage.put("successMessage", Constants.PERSONNEL_CREATED);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        // When
        ResponseEntity<Object> actualResponse = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(expectedMessage, actualResponse.getBody());
    }

    @Test
    public void shouldReturnConflictMessageWhenPersonnelAlreadyExists() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();
        personnelDto.setOrganizationId(organizationId);
        Map<String, String> expectedMessage = new HashMap<>();
        expectedMessage.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        // When
        ResponseEntity<Object> actualResponse = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CONFLICT, actualResponse.getStatusCode());
        assertEquals(expectedMessage, actualResponse.getBody());
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedDto() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("personnelId")
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        // When
        PersonnelDto actualDto = personnelController.updatePersonnel(request, personnelDto);

        // Then
        assertEquals(personnelDto, actualDto);
    }

    @Test
    public void shouldDeletePersonnel() {
        // Given
        String id = "personnelId";

        // When
        personnelController.deletePersonnel(id);

        // Then
        Mockito.verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();
        when(personnelService.findbyValue(organizationId, "", "", "", 0, 10)).thenReturn(expectedDisplay);

        // When
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "", "");

        // Then
        assertEquals(expectedDisplay, actualDisplay);
    }
}
