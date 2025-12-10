
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");

        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        when(personnelService.findAll(any(String.class), eq(0), eq(10))).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        assertEquals(personnelDisplay, result);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnelById() {
        String id = "123";
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.findById(eq(id))).thenReturn(personnelDto);

        PersonnelDto result = personnelController.getPersonnel(new MockHttpServletRequest(), id);

        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("orgId");
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        Map<String, String> expectedMessage = new HashMap<>();
        expectedMessage.put("successMessage", Constants.PERSONNEL_CREATED);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    public void shouldReturnConflictResponseWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId("orgId");
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        Map<String, String> expectedMessage = new HashMap<>();
        expectedMessage.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnelDto() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("123");
        when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        when(personnelService.findById(eq(personnelDto.getId()))).thenReturn(personnelDto);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldDeletePersonnelById() {
        String id = "123";
        personnelController.deletePersonnel(id);
        Mockito.verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        when(personnelService.findbyValue(any(String.class), any(String.class), any(String.class), any(String.class), eq(0), eq(10))).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "");

        assertEquals(personnelDisplay, result);
    }
}
