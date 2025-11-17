
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.validation.Valid;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.findAll(organizationId, 0, 10)).thenReturn(expectedDisplay);

        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);

        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
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

        PersonnelDto actualDto = personnelController.getPersonnel(mock(HttpServletRequest.class), id);

        assertEquals(expectedDto, actualDto);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelAndReturnSuccessMessage() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("Jane")
                .secondName("Smith")
                .jobTitle("Manager")
                .employeeId("emp456")
                .supervisor(false)
                .districtId("dist456")
                .fleetId("fleet456")
                .crewId("crew456")
                .build();

        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("successMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictMessageWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("Jane")
                .secondName("Smith")
                .jobTitle("Manager")
                .employeeId("emp456")
                .supervisor(false)
                .districtId("dist456")
                .fleetId("fleet456")
                .crewId("crew456")
                .build();

        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(409, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("errorMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnelDto() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("personnelId")
                .firstName("Jane")
                .secondName("Smith")
                .jobTitle("Manager")
                .employeeId("emp456")
                .supervisor(false)
                .districtId("dist456")
                .fleetId("fleet456")
                .crewId("crew456")
                .build();

        personnelDto.setOrganizationId(organizationId);
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        PersonnelDto updatedDto = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(personnelDto, updatedDto);
        verify(personnelService).updatePersonnel(personnelDto);
        verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelById() {
        String id = "personnelId";

        personnelController.deletePersonnel(id);

        verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.findbyValue(organizationId, "John", "dist456", "Manager", 0, 10)).thenReturn(expectedDisplay);

        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "John", "dist456", "Manager");

        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findbyValue(organizationId, "John", "dist456", "Manager", 0, 10);
    }
}
