
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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();

        when(personnelService.findAll(organizationId, offSet, limit)).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        assertEquals(personnelDisplay, result);
        verify(personnelService, times(1)).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "personnelId";
        PersonnelDto personnelDto = new PersonnelDto();

        when(personnelService.findById(id)).thenReturn(personnelDto);

        PersonnelDto result = personnelController.getPersonnel(request, id);

        assertEquals(personnelDto, result);
        verify(personnelService, times(1)).findById(id);
    }

    @Test
    public void shouldCreatePersonnelAndReturnSuccessMessage() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        Map<String, String> message = new HashMap<>();
        message.put("successMessage", Constants.PERSONNEL_CREATED);

        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(message, response.getBody());
        verify(personnelService, times(1)).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictMessageWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        Map<String, String> message = new HashMap<>();
        message.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);

        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(message, response.getBody());
        verify(personnelService, times(1)).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnelDto() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("personnelId");
        personnelDto.setOrganizationId(organizationId);
        PersonnelDto updatedPersonnelDto = new PersonnelDto();

        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(updatedPersonnelDto);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(updatedPersonnelDto, result);
        verify(personnelService, times(1)).updatePersonnel(personnelDto);
        verify(personnelService, times(1)).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenIdIsProvided() {
        String id = "personnelId";

        personnelController.deletePersonnel(id);

        verify(personnelService, times(1)).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        int offSet = 0;
        int limit = 10;
        String personnelName = "John Doe";
        String districtId = "districtId";
        String jobTitle = "Engineer";
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();

        when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit)).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        assertEquals(personnelDisplay, result);
        verify(personnelService, times(1)).findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);
    }
}
