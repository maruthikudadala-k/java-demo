
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnelCalled() {
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
    public void shouldReturnPersonnelWhenGetPersonnelByIdCalled() {
        String id = "personnelId";
        PersonnelDto expectedPersonnel = new PersonnelDto();
        expectedPersonnel.setId(id);

        when(personnelService.findById(id)).thenReturn(expectedPersonnel);

        PersonnelDto actualPersonnel = personnelController.getPersonnel(new MockHttpServletRequest(), id);

        assertEquals(expectedPersonnel, actualPersonnel);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        personnelDto.setFirstName("First");
        personnelDto.setSecondName("Last");
        personnelDto.setJobTitle("Job");
        personnelDto.setEmployeeId("emp123");
        personnelDto.setSupervisor(true);
        personnelDto.setDistrictId("dist123");
        personnelDto.setFleetId("fleet123");
        personnelDto.setCrewId("crew123");

        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);

        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("personnelId");
        personnelDto.setOrganizationId(organizationId);

        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        PersonnelDto updatedPersonnel = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(personnelDto, updatedPersonnel);
        verify(personnelService).updatePersonnel(personnelDto);
        verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelCalled() {
        String id = "personnelId";

        personnelController.deletePersonnel(id);

        verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);

        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.findbyValue(organizationId, "name", "dist123", "job", 0, 10)).thenReturn(expectedDisplay);

        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "name", "dist123", "job");

        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findbyValue(organizationId, "name", "dist123", "job", 0, 10);
    }
}
