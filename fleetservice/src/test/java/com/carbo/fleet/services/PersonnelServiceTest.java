
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
import org.mockito.junit.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
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
        // Given
        String organizationId = "org1";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        // When
        when(personnelService.lookUpPersonnel(null, "", "", "", organizationId, offSet, limit)).thenReturn(expectedDisplay);
        PersonnelDisplay result = personnelService.findAll(organizationId, offSet, limit);

        // Then
        assertEquals(expectedDisplay, result);
    }

    @Test
    public void shouldSavePersonnelAndReturnTrue() {
        // Given
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Manager")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org1")
                .build();
        
        Personnel newPersonnel = Personnel.builder()
                .crewId(dto.getCrewId())
                .employeeId(dto.getEmployeeId())
                .firstName(dto.getFirstName())
                .districtId(dto.getDistrictId())
                .jobTitle(dto.getJobTitle())
                .fleetId(dto.getFleetId())
                .secondName(dto.getSecondName())
                .supervisor(dto.getSupervisor())
                .organizationId(dto.getOrganizationId())
                .build();
        
        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(newPersonnel);

        // When
        Boolean result = personnelService.savePersonnel(dto);

        // Then
        assertTrue(result);
        verify(personnelDBRepository, times(2)).save(any(Personnel.class));
    }

    @Test
    public void shouldUpdatePersonnelAndReturnTrue() {
        // Given
        PersonnelDto dto = PersonnelDto.builder()
                .id("personnelId")
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Manager")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(true)
                .supervisorId("supervisorId")
                .organizationId("org1")
                .build();
        
        Personnel existingPersonnel = Personnel.builder()
                .id(dto.getId())
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("OldName")
                .districtId("dist1")
                .jobTitle("OldJob")
                .fleetId("fleet1")
                .secondName("OldLastName")
                .supervisor(true)
                .supervisorId("supervisorId")
                .organizationId("org1")
                .build();
        
        when(personnelDBRepository.findById(dto.getId())).thenReturn(Optional.of(existingPersonnel));

        // When
        Boolean result = personnelService.updatePersonnel(dto);

        // Then
        assertTrue(result);
        verify(personnelDBRepository).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Given
        String id = "personnelId";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .build();
        
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .totalCount(1L)
                .build();
        
        when(personnelService.lookUpPersonnel(anyList(), anyString(), anyString(), anyString(), isNull(), anyInt(), anyInt())).thenReturn(personnelDisplay);

        // When
        PersonnelDto result = personnelService.findById(id);

        // Then
        assertEquals(expectedDto, result);
    }

    @Test
    public void shouldDeletePersonnel() {
        // Given
        String id = "personnelId";
        Personnel personnel = Personnel.builder().id(id).build();
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // When
        personnelService.deletePersonnel(id);

        // Then
        verify(personnelDBRepository).deleteById(id);
    }
}
