
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        String organizationId = "org123";
        Mockito.when(personnelService.findAll(eq(organizationId), eq(0), eq(10)))
                .thenReturn(new PersonnelDisplay());

        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        assertNotNull(result);
        Mockito.verify(personnelService).findAll(eq(organizationId), eq(0), eq(10));
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelById() {
        String id = "personnelId";
        Mockito.when(personnelService.findById(id))
                .thenReturn(new PersonnelDto());

        PersonnelDto result = personnelController.getPersonnel(new MockHttpServletRequest(), id);

        assertNotNull(result);
        Mockito.verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelWhenPostPersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setFirstName("John");
        personnelDto.setSecondName("Doe");
        personnelDto.setJobTitle("Engineer");
        personnelDto.setEmployeeId("emp123");
        personnelDto.setSupervisor(true);
        personnelDto.setDistrictId("dist123");
        personnelDto.setFleetId("fleet123");
        personnelDto.setCrewId("crew123");

        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class)))
                .thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof java.util.Map);
        Mockito.verify(personnelService).savePersonnel(any(PersonnelDto.class));
    }

    @Test
    public void shouldUpdatePersonnelWhenPutPersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("personnelId");
        personnelDto.setFirstName("John");
        personnelDto.setSecondName("Doe");

        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class)))
                .thenReturn(true);
        Mockito.when(personnelService.findById(personnelDto.getId()))
                .thenReturn(personnelDto);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertNotNull(result);
        Mockito.verify(personnelService).updatePersonnel(any(PersonnelDto.class));
        Mockito.verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeleteById() {
        String id = "personnelId";

        personnelController.deletePersonnel(id);

        Mockito.verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        String organizationId = "org123";
        Mockito.when(personnelService.findbyValue(eq(organizationId), any(String.class), any(String.class), any(String.class), eq(0), eq(10)))
                .thenReturn(new PersonnelDisplay());

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "");

        assertNotNull(result);
        Mockito.verify(personnelService).findbyValue(eq(organizationId), any(String.class), any(String.class), any(String.class), eq(0), eq(10));
    }
}
