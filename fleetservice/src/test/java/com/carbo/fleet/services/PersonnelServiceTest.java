
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.domain.PageRequest;

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
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .organizationId("org123")
                .build();
        
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(personnelDto))
                .totalCount(1L)
                .build();
        
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.MongoTemplate.AggregationResults<PersonnelDto>(Collections.singletonList(personnelDto), null));
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(TotalCountObject.class)))
                .thenReturn(new org.springframework.data.mongodb.core.MongoTemplate.AggregationResults<TotalCountObject>(Collections.singletonList(new TotalCountObject(1L)), null));

        // Act
        PersonnelDisplay result = personnelService.findAll(organizationId, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDisplay.getTotalCount(), result.getTotalCount());
    }

    @Test
    void shouldReturnTrueWhenSavePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crewId")
                .employeeId("employeeId")
                .firstName("John")
                .districtId("districtId")
                .jobTitle("Developer")
                .fleetId("fleetId")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("orgId")
                .build();
        
        Personnel savedPersonnel = Personnel.builder()
                .id("1")
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

        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(savedPersonnel);

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
                .id("1")
                .crewId("crewId")
                .employeeId("employeeId")
                .firstName("John")
                .districtId("districtId")
                .jobTitle("Developer")
                .fleetId("fleetId")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("orgId")
                .build();
        
        Personnel existingPersonnel = Personnel.builder()
                .id(dto.getId())
                .crewId("oldCrewId")
                .employeeId("oldEmployeeId")
                .firstName("OldFirstName")
                .districtId("oldDistrictId")
                .jobTitle("OldJobTitle")
                .fleetId("oldFleetId")
                .secondName("OldSecondName")
                .supervisor(false)
                .organizationId("oldOrgId")
                .build();
        
        when(personnelDBRepository.findById(dto.getId())).thenReturn(Optional.of(existingPersonnel));
        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(existingPersonnel);

        // Act
        Boolean result = personnelService.updatePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "1";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .organizationId("org123")
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
        assertEquals(expectedDto.getFirstName(), result.getFirstName());
    }

    @Test
    void shouldDeletePersonnelWhenDeletePersonnel() {
        // Arrange
        String id = "1";
        Personnel personnel = Personnel.builder().id(id).build();
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository, times(1)).deleteById(id);
    }
}
