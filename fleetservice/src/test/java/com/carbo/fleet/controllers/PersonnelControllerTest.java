
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

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        when(personnelService.findAll(organizationId, offSet, limit)).thenReturn(personnelDisplay);

        // When
        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        // Then
        assertEquals(personnelDisplay, result);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnel() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "personnelId";
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.findById(id)).thenReturn(personnelDto);

        // When
        PersonnelDto result = personnelController.getPersonnel(request, id);

        // Then
        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("1");
        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        // When
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void shouldReturnConflictResponseWhenPersonnelAlreadyExists() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = new PersonnelDto();
        when(personnelService.savePersonnel(personnelDto)).thenReturn(false);

        // When
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void shouldUpdatePersonnelAndReturnPersonnelDto() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        PersonnelDto personnelDto = new PersonnelDto();
        personnelDto.setId("1");
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        // When
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Then
        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldDeletePersonnel() {
        // Given
        String id = "personnelId";

        // When
        personnelController.deletePersonnel(id);

        // Then
        Mockito.verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(() -> organizationId);
        int offSet = 0;
        int limit = 10;
        String personnelName = "John";
        String districtId = "dist1";
        String jobTitle = "Engineer";
        PersonnelDisplay personnelDisplay = new PersonnelDisplay();
        when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit)).thenReturn(personnelDisplay);

        // When
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Then
        assertEquals(personnelDisplay, result);
    }
}
