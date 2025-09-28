
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnelCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(personnelService.findAll(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(expectedDisplay);

        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);

        assertNotNull(actualDisplay);
        assertEquals(expectedDisplay, actualDisplay);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelCalled() {
        String personnelId = "1";
        PersonnelDto expectedDto = new PersonnelDto();
        expectedDto.setId(personnelId);

        Mockito.when(personnelService.findById(personnelId)).thenReturn(expectedDto);

        PersonnelDto actualDto = personnelController.getPersonnel(new MockHttpServletRequest(), personnelId);

        assertNotNull(actualDto);
        assertEquals(expectedDto.getId(), actualDto.getId());
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setFirstName("John");
        personnelDto.setSecondName("Doe");
        personnelDto.setJobTitle("Engineer");
        personnelDto.setEmployeeId("E123");
        personnelDto.setSupervisor(true);
        personnelDto.setDistrictId("D1");
        personnelDto.setFleetId("F1");
        personnelDto.setCrewId("C1");

        Mockito.when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("1");

        Mockito.when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        Mockito.when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        PersonnelDto actualDto = personnelController.updatePersonnel(request, personnelDto);

        assertNotNull(actualDto);
        assertEquals(personnelDto.getId(), actualDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelCalled() {
        String personnelId = "1";

        personnelController.deletePersonnel(personnelId);

        Mockito.verify(personnelService, Mockito.times(1)).deletePersonnel(personnelId);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "user");
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(personnelService.findbyValue(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(expectedDisplay);

        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "", "");

        assertNotNull(actualDisplay);
        assertEquals(expectedDisplay, actualDisplay);
    }
}
