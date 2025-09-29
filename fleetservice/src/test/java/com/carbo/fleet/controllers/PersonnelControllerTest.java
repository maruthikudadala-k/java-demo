
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().build();

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(personnelService.findAll(organizationId, offSet, limit)).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        assertEquals(personnelDisplay, result);
        verify(personnelService).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnelById() {
        String id = "personnelId";
        PersonnelDto personnelDto = PersonnelDto.builder().build();

        when(personnelService.findById(id)).thenReturn(personnelDto);

        PersonnelDto result = personnelController.getPersonnel(request, id);

        assertEquals(personnelDto, result);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        String organizationId = "org123";
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        personnelDto.setOrganizationId(organizationId);
        Boolean status = true;

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(status);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("successMessage", Constants.PERSONNEL_CREATED);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictResponseWhenPersonnelAlreadyExists() {
        String organizationId = "org123";
        PersonnelDto personnelDto = PersonnelDto.builder().build();
        personnelDto.setOrganizationId(organizationId);
        Boolean status = false;

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(personnelService.savePersonnel(personnelDto)).thenReturn(status);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnelDto() {
        String organizationId = "org123";
        PersonnelDto personnelDto = PersonnelDto.builder().id("personnelId").build();
        personnelDto.setOrganizationId(organizationId);
        PersonnelDto updatedPersonnelDto = PersonnelDto.builder().build();

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(updatedPersonnelDto);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(updatedPersonnelDto, result);
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
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        String personnelName = "John";
        String districtId = "district123";
        String jobTitle = "Engineer";
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().build();

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit)).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        assertEquals(personnelDisplay, result);
        verify(personnelService).findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);
    }
}
