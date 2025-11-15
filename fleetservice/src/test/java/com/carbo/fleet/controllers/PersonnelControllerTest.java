
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnel() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(Mockito.mock(Principal.class));
        String organizationId = "org123";
        Mockito.when(personnelService.findAll(eq(organizationId), eq(0), eq(10)))
                .thenReturn(PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build());

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        // Assert
        assertEquals(0, result.getTotalCount());
        assertEquals(0, result.getPersonnelDisplayObject().size());
    }

    @Test
    public void shouldReturnPersonnelDtoWhenGetPersonnelById() {
        // Arrange
        String id = "personnelId";
        PersonnelDto personnelDto = PersonnelDto.builder().id(id).firstName("John").build();
        Mockito.when(personnelService.findById(eq(id))).thenReturn(personnelDto);
        MockHttpServletRequest request = new MockHttpServletRequest();

        // Act
        PersonnelDto result = personnelController.getPersonnel(request, id);

        // Assert
        assertEquals(id, result.getId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    public void shouldCreatePersonnelAndReturnCreatedResponse() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").build();
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Constants.PERSONNEL_CREATED, ((Map<String, String>) response.getBody()).get("successMessage"));
    }

    @Test
    public void shouldReturnConflictResponseWhenPersonnelAlreadyExists() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").build();
        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(false);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(Constants.PERSONNEL_ALREADY_EXISTS, ((Map<String, String>) response.getBody()).get("errorMessage"));
    }

    @Test
    public void shouldUpdatePersonnelAndReturnUpdatedPersonnelDto() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().id("personnelId").firstName("John").build();
        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        Mockito.when(personnelService.findById(eq("personnelId"))).thenReturn(personnelDto);

        // Act
        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertEquals("personnelId", result.getId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    public void shouldDeletePersonnelById() {
        // Arrange
        String id = "personnelId";

        // Act
        personnelController.deletePersonnel(id);

        // Assert
        Mockito.verify(personnelService, Mockito.times(1)).deletePersonnel(eq(id));
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenGetAllPersonnelByFilter() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        Mockito.when(personnelService.findbyValue(eq(organizationId), eq("John"), eq("districtId"), eq("jobTitle"), eq(0), eq(10)))
                .thenReturn(PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).totalCount(0L).build());

        // Act
        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "John", "districtId", "jobTitle");

        // Assert
        assertEquals(0, result.getTotalCount());
        assertEquals(0, result.getPersonnelDisplayObject().size());
    }
}
