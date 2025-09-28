
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonnelServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private PersonnelDBRepository personnelDBRepository;

    @InjectMocks
    private PersonnelService personnelService;

    @Test
    void shouldReturnPersonnelDisplayWhenFindAll() {
        // Arrange
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();
        
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class))).thenReturn(new AggregationResults<>(Collections.emptyList(), null));
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(TotalCountObject.class))).thenReturn(new AggregationResults<>(Collections.emptyList(), null));

        // Act
        PersonnelDisplay result = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDisplay.getTotalCount(), result.getTotalCount());
        verify(mongoTemplate, times(1)).aggregate(any(), eq("personnel"), eq(TotalCountObject.class));
    }

    @Test
    void shouldReturnTrueWhenSavePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Developer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();
        
        Personnel createdPersonnel = Personnel.builder()
                .id("newId")
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

        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(createdPersonnel);

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    void shouldReturnTrueWhenUpdatePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("existingId")
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Developer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .supervisorId("supervisorId")
                .organizationId("org123")
                .build();

        Personnel existingPersonnel = Personnel.builder()
                .id("existingId")
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("OldName")
                .districtId("dist123")
                .jobTitle("OldJobTitle")
                .fleetId("fleet123")
                .secondName("OldSecondName")
                .supervisor(false)
                .organizationId("org123")
                .build();

        when(personnelDBRepository.findById(dto.getId())).thenReturn(Optional.of(existingPersonnel));
        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(existingPersonnel);

        // Act
        Boolean result = personnelService.updatePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository, times(1)).findById(dto.getId());
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "existingId";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .build();

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .totalCount(1L)
                .build();
        
        when(personnelService.lookUpPersonnel(any(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(personnelDisplay);

        // Act
        PersonnelDto result = personnelService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        verify(personnelService, times(1)).lookUpPersonnel(any(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void shouldDeletePersonnelWhenIdExists() {
        // Arrange
        String id = "existingId";
        Personnel existingPersonnel = Personnel.builder().id(id).build();

        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(existingPersonnel));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository, times(1)).deleteById(id);
    }
}
