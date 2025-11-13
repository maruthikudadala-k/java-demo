
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.domain.PageRequest;

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
        // Arrange
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();
        
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.emptyList(), null));
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(TotalCountObject.class)))
                .thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.singletonList(new TotalCountObject(0L)), null));
        
        // Act
        PersonnelDisplay actualDisplay = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertNotNull(actualDisplay);
        assertEquals(expectedDisplay.getTotalCount(), actualDisplay.getTotalCount());
        verify(mongoTemplate, times(1)).aggregate(any(), eq("personnel"), eq(TotalCountObject.class));
    }

    @Test
    public void shouldSavePersonnelSuccessfully() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Developer")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org1")
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
    public void shouldReturnFalseWhenDuplicateKeyExceptionOccursOnSave() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Developer")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org1")
                .build();
        
        when(personnelDBRepository.save(any(Personnel.class))).thenThrow(new org.springframework.dao.DuplicateKeyException("Duplicate key"));

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertFalse(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "someId";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Developer")
                .employeeId("emp1")
                .supervisor(true)
                .districtId("dist1")
                .fleetId("fleet1")
                .crewId("crew1")
                .organizationId("org1")
                .build();
        
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.singletonList(expectedDto), null));

        // Act
        PersonnelDto actualDto = personnelService.findById(id);

        // Assert
        assertNotNull(actualDto);
        assertEquals(expectedDto.getId(), actualDto.getId());
        verify(mongoTemplate, times(1)).aggregate(any(), eq("personnel"), eq(PersonnelDto.class));
    }

    @Test
    public void shouldDeletePersonnelSuccessfully() {
        // Arrange
        String id = "someId";
        Personnel personnel = Personnel.builder().id(id).build();
        
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository, times(1)).deleteById(id);
    }
}
