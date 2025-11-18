
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "principal"); // Mock Principal

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().build();
        when(personnelService.findAll(any(String.class), anyInt(), anyInt())).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        assertNotNull(result);
        verify(personnelService, times(1)).findAll(any(String.class), anyInt(), anyInt());
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelById() {
        String id = "123";
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.findById(id)).thenReturn(personnelDto);

        PersonnelDto result = personnelController.getPersonnel(new MockHttpServletRequest(), id);

        assertNotNull(result);
        verify(personnelService, times(1)).findById(id);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "principal"); // Mock Principal

        PersonnelDto personnelDto = PersonnelDto.builder().build();
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(Constants.PERSONNEL_CREATED, ((HashMap) result.getBody()).get("successMessage"));
        verify(personnelService, times(1)).savePersonnel(any(PersonnelDto.class));
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "principal"); // Mock Principal

        PersonnelDto personnelDto = PersonnelDto.builder().build();
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(Constants.PERSONNEL_ALREADY_EXISTS, ((HashMap) result.getBody()).get("errorMessage"));
        verify(personnelService, times(1)).savePersonnel(any(PersonnelDto.class));
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "principal"); // Mock Principal

        PersonnelDto personnelDto = PersonnelDto.builder().id("123").build();
        when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        when(personnelService.findById(any(String.class))).thenReturn(personnelDto);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertNotNull(result);
        verify(personnelService, times(1)).updatePersonnel(any(PersonnelDto.class));
        verify(personnelService, times(1)).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnel() {
        String id = "123";
        doNothing().when(personnelService).deletePersonnel(id);

        personnelController.deletePersonnel(id);

        verify(personnelService, times(1)).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "principal"); // Mock Principal

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().build();
        when(personnelService.findbyValue(any(String.class), any(String.class), any(String.class), any(String.class), anyInt(), anyInt()))
                .thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "name", "districtId", "jobTitle");

        assertNotNull(result);
        verify(personnelService, times(1)).findbyValue(any(String.class), any(String.class), any(String.class), any(String.class), anyInt(), anyInt());
    }
}
