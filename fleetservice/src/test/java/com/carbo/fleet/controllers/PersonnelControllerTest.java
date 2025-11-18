
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
    public void shouldReturnAllPersonnelWhenGetAllPersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");

        PersonnelDisplay expectedDisplay = new PersonnelDisplay();
        when(personnelService.findAll(anyString(), anyInt(), anyInt())).thenReturn(expectedDisplay);

        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);

        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findAll(anyString(), eq(0), eq(10));
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelIsCalled() {
        String personnelId = "123";
        PersonnelDto expectedPersonnel = new PersonnelDto();
        when(personnelService.findById(personnelId)).thenReturn(expectedPersonnel);

        PersonnelDto actualPersonnel = personnelController.getPersonnel(mock(HttpServletRequest.class), personnelId);

        assertEquals(expectedPersonnel, actualPersonnel);
        verify(personnelService).findById(personnelId);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");
        
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setFirstName("John");
        personnelDto.setSecondName("Doe");
        personnelDto.setJobTitle("Engineer");
        personnelDto.setEmployeeId("EMP123");
        personnelDto.setSupervisor(false);
        personnelDto.setDistrictId("DIST01");
        personnelDto.setFleetId("FLEET01");
        personnelDto.setCrewId("CREW01");

        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("successMessage", Constants.PERSONNEL_CREATED);
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseMessage, response.getBody());
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");

        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");

        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("123");
        when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        PersonnelDto actualPersonnel = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(personnelDto, actualPersonnel);
        verify(personnelService).updatePersonnel(personnelDto);
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        String personnelId = "123";

        personnelController.deletePersonnel(personnelId);

        verify(personnelService).deletePersonnel(personnelId);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testPrincipal");

        PersonnelDisplay expectedDisplay = new PersonnelDisplay();
        when(personnelService.findbyValue(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(expectedDisplay);

        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "John", "DIST01", "Engineer");

        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).findbyValue(anyString(), eq("John"), eq("DIST01"), eq("Engineer"), eq(0), eq(10));
    }
}
