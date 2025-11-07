
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
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> "user");

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
        PersonnelDto expectedDto = PersonnelDto.builder().id(id).firstName("John").build();

        when(personnelService.findById(id)).thenReturn(expectedDto);

        PersonnelDto actualDto = personnelController.getPersonnel(new MockHttpServletRequest(), id);

        assertEquals(expectedDto, actualDto);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> "user");

        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(false)
                .districtId("dist1")
                .fleetId("fleet1")
                .crewId("crew1")
                .build();

        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        assertEquals("personnel_created", ((Map) response.getBody()).get("successMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> "user");

        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(false)
                .districtId("dist1")
                .fleetId("fleet1")
                .crewId("crew1")
                .build();

        personnelDto.setOrganizationId(organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        assertEquals("Personnel already exists", ((Map) response.getBody()).get("errorMessage"));
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnelDto() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> "user");

        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("personnelId")
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(false)
                .districtId("dist1")
                .fleetId("fleet1")
                .crewId("crew1")
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
    public void shouldDeletePersonnel() {
        String id = "personnelId";

        personnelController.deletePersonnel(id);

        verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> "user");

        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.findbyValue(organizationId, "", "", "", 0, 10)).thenReturn(expectedDisplay);

        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "", "");

        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findbyValue(organizationId, "", "", "", 0, 10);
    }
}
