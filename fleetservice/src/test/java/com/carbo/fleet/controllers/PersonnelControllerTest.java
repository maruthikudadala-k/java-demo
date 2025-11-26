
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnAllPersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(personnelService.findAll(anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnel(request, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldReturnPersonnelById() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("John").secondName("Doe").build();

        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        PersonnelDto result = personnelController.getPersonnel(request, "1");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getSecondName());
    }

    @Test
    public void shouldCreatePersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        PersonnelDto personnelDto = PersonnelDto.builder().firstName("Jane").secondName("Doe").build();

        Mockito.when(personnelService.savePersonnel(any(PersonnelDto.class))).thenReturn(true);

        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void shouldUpdatePersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        PersonnelDto personnelDto = PersonnelDto.builder().id("1").firstName("Jane").secondName("Doe").build();

        Mockito.when(personnelService.updatePersonnel(any(PersonnelDto.class))).thenReturn(true);
        Mockito.when(personnelService.findById(anyString())).thenReturn(personnelDto);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getSecondName());
    }

    @Test
    public void shouldDeletePersonnel() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        personnelController.deletePersonnel("1");

        Mockito.verify(personnelService, Mockito.times(1)).deletePersonnel("1");
    }

    @Test
    public void shouldReturnAllPersonnelByFilter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(() -> "testUser");
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(personnelService.findbyValue(anyString(), anyString(), anyString(), anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, 0, 10, "", "", "");

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }
}
