
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;

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
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.findAll(organizationId, offSet, limit)).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnel(request, offSet, limit);

        assertEquals(personnelDisplay, result);
        verify(personnelService).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String id = "person123";
        PersonnelDto personnelDto = new PersonnelDto();

        when(personnelService.findById(id)).thenReturn(personnelDto);

        PersonnelDto result = personnelController.getPersonnel(request, id);

        assertEquals(personnelDto, result);
        verify(personnelService).findById(id);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();
        String organizationId = "org123";
        personnelDto.setOrganizationId(organizationId);
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(201)
                .body(Collections.singletonMap("successMessage", "personnel_created"));

        when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        ResponseEntity<Object> result = personnelController.createPersonnel(request, personnelDto);

        assertEquals(expectedResponse, result);
        verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("person123")
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();
        String organizationId = "org123";
        personnelDto.setOrganizationId(organizationId);
        when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        PersonnelDto result = personnelController.updatePersonnel(request, personnelDto);

        assertEquals(personnelDto, result);
        verify(personnelService).updatePersonnel(personnelDto);
        verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        String id = "person123";

        personnelController.deletePersonnel(id);

        verify(personnelService).deletePersonnel(id);
    }

    @Test
    public void shouldReturnFilteredPersonnelWhenGetAllPersonnelByFilterIsCalled() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        String personnelName = "John";
        String districtId = "dist123";
        String jobTitle = "Engineer";
        int offSet = 0;
        int limit = 10;

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit)).thenReturn(personnelDisplay);

        PersonnelDisplay result = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        assertEquals(personnelDisplay, result);
        verify(personnelService).findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);
    }
}
