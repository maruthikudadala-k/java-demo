
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

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setAttribute("organizationId", organizationId);
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(personnelService.findAll(anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        assertEquals(personnelDisplay, result);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelIsCalled() {
        String personnelId = "personnel123";
        PersonnelDto personnelDto = PersonnelDto.builder().id(personnelId).firstName("John").build();

        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        PersonnelDto result = personnelController.getPersonnel(new MockHttpServletRequest(), personnelId);

        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("EMP123")
                .supervisor(true)
                .districtId("DIST123")
                .fleetId("FLEET123")
                .crewId("CREW123")
                .build();
        request.setAttribute("organizationId", "org123");

        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Constants.PERSONNEL_CREATED, ((java.util.Map<String, String>) response.getBody()).get("successMessage"));
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").build();
        request.setAttribute("organizationId", "org123");

        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(Constants.PERSONNEL_ALREADY_EXISTS, ((java.util.Map<String, String>) response.getBody()).get("errorMessage"));
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().id("personnel123").firstName("John").build();
        request.setAttribute("organizationId", "org123");

        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        String personnelId = "personnel123";

        personnelController.deletePersonnel(personnelId);

        Mockito.verify(personnelService, Mockito.times(1)).deletePersonnel(personnelId);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("organizationId", "org123");
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(personnelService.findbyValue(anyString(), anyString(), anyString(), anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "John", "DIST123", "Engineer");

        assertEquals(personnelDisplay, result);
    }
}
