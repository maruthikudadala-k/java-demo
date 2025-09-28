
package com.carbo.fleet.services;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.Personnel;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.model.TotalCountObject;
import com.carbo.fleet.repository.PersonnelDBRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonnelServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private PersonnelDBRepository personnelDBRepository;

    @InjectMocks
    private PersonnelService personnelService;

    @Test
    public void shouldReturnPersonnelDisplayWhenFindAll() {
        // Setup
        String organizationId = "org1";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.lookUpPersonnel(null, "", "", "", organizationId, offSet, limit)).thenReturn(personnelDisplay);

        // Execute
        PersonnelDisplay result = personnelService.findAll(organizationId, offSet, limit);

        // Verify
        assertNotNull(result);
        assertEquals(personnelDisplay, result);
        verify(personnelService).lookUpPersonnel(null, "", "", "", organizationId, offSet, limit);
    }

    @Test
    public void shouldSavePersonnelSuccessfully() {
        // Setup
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Engineer")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(false)
                .organizationId("org1")
                .build();

        Personnel createdPersonnel = Personnel.builder()
                .id("1")
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Engineer")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(false)
                .organizationId("org1")
                .build();

        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(createdPersonnel);

        // Execute
        Boolean result = personnelService.savePersonnel(dto);

        // Verify
        assertTrue(result);
        verify(personnelDBRepository).save(any(Personnel.class));
    }

    @Test
    public void shouldUpdatePersonnelSuccessfully() {
        // Setup
        PersonnelDto dto = PersonnelDto.builder()
                .id("1")
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Engineer")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(false)
                .supervisorId(null)
                .organizationId("org1")
                .build();

        Personnel existingPersonnel = Personnel.builder()
                .id("1")
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Engineer")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(false)
                .organizationId("org1")
                .build();

        when(personnelDBRepository.findById("1")).thenReturn(Optional.of(existingPersonnel));

        // Execute
        Boolean result = personnelService.updatePersonnel(dto);

        // Verify
        assertTrue(result);
        verify(personnelDBRepository).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Setup
        String id = "1";
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("1")
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp1")
                .districtId("dist1")
                .supervisor(false)
                .organizationId("org1")
                .fleetId("fleet1")
                .crewId("crew1")
                .build();

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(personnelDto))
                .totalCount(1L)
                .build();

        when(personnelService.lookUpPersonnel(Collections.singletonList(id), "", "", "", null, 0, 10)).thenReturn(personnelDisplay);

        // Execute
        PersonnelDto result = personnelService.findById(id);

        // Verify
        assertNotNull(result);
        assertEquals(personnelDto, result);
    }

    @Test
    public void shouldDeletePersonnelSuccessfully() {
        // Setup
        String id = "1";
        Personnel personnel = Personnel.builder()
                .id(id)
                .build();

        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // Execute
        personnelService.deletePersonnel(id);

        // Verify
        verify(personnelDBRepository).deleteById(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenFindByValue() {
        // Setup
        String organizationId = "org1";
        String personnelName = "John";
        String districtId = "dist1";
        String jobTitle = "Engineer";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.lookUpPersonnel(null, personnelName, districtId, jobTitle, organizationId, offSet, limit)).thenReturn(personnelDisplay);

        // Execute
        PersonnelDisplay result = personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);

        // Verify
        assertNotNull(result);
        assertEquals(personnelDisplay, result);
        verify(personnelService).lookUpPersonnel(null, personnelName, districtId, jobTitle, organizationId, offSet, limit);
    }
}
