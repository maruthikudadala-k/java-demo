
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

import javax.validation.Valid;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    PersonnelService personnelService;

    @InjectMocks
    PersonnelController personnelController;

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        Mockito.when(personnelService.findAll(anyString(), any(Integer.class), any(Integer.class))).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        assertEquals(personnelDisplay, result);
        Mockito.verify(personnelService).findAll(organizationId, 0, 10);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelByIdIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String personnelId = "personnelId";
        PersonnelDto personnelDto = new PersonnelDto();
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        PersonnelDto result = personnelController.getPersonnel(request, personnelId);

        assertEquals(personnelDto, result);
        Mockito.verify(personnelService).findById(personnelId);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        HashMap<String, String> message = (HashMap<String, String>) response.getBody();
        assertEquals(Constants.PERSONNEL_CREATED, message.get("successMessage"));
        Mockito.verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        HashMap<String, String> message = (HashMap<String, String>) response.getBody();
        assertEquals(Constants.PERSONNEL_ALREADY_EXISTS, message.get("errorMessage"));
        Mockito.verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(personnelDto, result);
        Mockito.verify(personnelService).updatePersonnel(personnelDto);
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        String personnelId = "personnelId";

        personnelController.deletePersonnel(personnelId);

        Mockito.verify(personnelService).deletePersonnel(personnelId);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        Mockito.when(personnelService.findbyValue(anyString(), anyString(), anyString(), anyString(), any(Integer.class), any(Integer.class))).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "John", "district1", "job1");

        assertEquals(personnelDisplay, result);
        Mockito.verify(personnelService).findbyValue(organizationId, "John", "district1", "job1", 0, 10);
    }
}
