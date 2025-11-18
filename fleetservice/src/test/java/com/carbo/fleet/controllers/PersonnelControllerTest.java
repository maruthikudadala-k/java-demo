
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        request.setUserPrincipal(() -> "testPrincipal");
        
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        when(personnelService.findAll(any(String.class), anyInt(), anyInt())).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        assertEquals(personnelDisplay, result);
        verify(personnelService).findAll(any(String.class), eq(0), eq(10));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        String personnelId = "123";
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.findById(personnelId)).thenReturn(personnelDto);

        PersonnelDto result = personnelController.getPersonnel(mock(HttpServletRequest.class), personnelId);

        assertEquals(personnelDto, result);
        verify(personnelService).findById(personnelId);
    }

    @Test
    public void shouldCreatePersonnelAndReturnSuccessMessage() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");
        
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("orgId");
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        Map<String, String> expectedMessage = new HashMap<>();
        expectedMessage.put("successMessage", Constants.PERSONNEL_CREATED);
        assertEquals(new ResponseEntity<>(expectedMessage, HttpStatus.CREATED), result);
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictMessageWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");
        
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("orgId");
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        Map<String, String> expectedMessage = new HashMap<>();
        expectedMessage.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        assertEquals(new ResponseEntity<>(expectedMessage, HttpStatus.CONFLICT), result);
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");
        
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("123");
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(personnelDto, result);
        verify(personnelService).updatePersonnel(personnelDto);
        verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnel() {
        String personnelId = "123";

        personnelController.deletePersonnel(personnelId);

        verify(personnelService).deletePersonnel(personnelId);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");
        
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        when(personnelService.findbyValue(any(String.class), any(String.class), any(String.class), any(String.class), anyInt(), anyInt()))
                .thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "name", "districtId", "jobTitle");

        assertEquals(personnelDisplay, result);
        verify(personnelService).findbyValue(any(String.class), eq("name"), eq("districtId"), eq("jobTitle"), eq(0), eq(10));
    }
}
