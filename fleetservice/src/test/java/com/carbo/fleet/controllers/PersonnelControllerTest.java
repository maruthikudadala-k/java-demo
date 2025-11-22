
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.Valid;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDisplay expectedDisplay = new PersonnelDisplay();
        
        Mockito.when(personnelService.findAll(eq(organizationId), any(Integer.class), any(Integer.class)))
                .thenReturn(expectedDisplay);

        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, 0, 10);

        assertEquals(expectedDisplay, actualDisplay);
        Mockito.verify(personnelService).findAll(eq(organizationId), eq(0), eq(10));
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelIsCalled() {
        String id = "personnelId";
        PersonnelDto expectedDto = new PersonnelDto();
        
        Mockito.when(personnelService.findById(eq(id)))
                .thenReturn(expectedDto);

        PersonnelDto actualDto = personnelController.getPersonnel(new MockHttpServletRequest(), id);

        assertEquals(expectedDto, actualDto);
        Mockito.verify(personnelService).findById(eq(id));
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class)))
                .thenReturn(true);
        
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof java.util.Map);
        Mockito.verify(personnelService).savePersonnel(any(PersonnelDto.class));
    }

    @Test
    public void shouldReturnConflictWhenPersonnelAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setOrganizationId(organizationId);
        
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class)))
                .thenReturn(false);
        
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(409, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof java.util.Map);
        Mockito.verify(personnelService).savePersonnel(any(PersonnelDto.class));
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("personnelId");
        personnelDto.setOrganizationId(organizationId);

        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class)))
                .thenReturn(true);
        Mockito.when(personnelService.findById(eq(personnelDto.getId())))
                .thenReturn(personnelDto);

        PersonnelDto actualDto = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(personnelDto, actualDto);
        Mockito.verify(personnelService).updatePersonnel(any(PersonnelDto.class));
        Mockito.verify(personnelService).findById(eq(personnelDto.getId()));
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        String id = "personnelId";

        personnelController.deletePersonnel(id);

        Mockito.verify(personnelService).deletePersonnel(eq(id));
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDisplay expectedDisplay = new PersonnelDisplay();
        
        Mockito.when(personnelService.findbyValue(eq(organizationId), any(String.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(expectedDisplay);

        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "", "");

        assertEquals(expectedDisplay, actualDisplay);
        Mockito.verify(personnelService).findbyValue(eq(organizationId), any(String.class), any(String.class), any(String.class), eq(0), eq(10));
    }
}
