
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
import org.springframework.data.domain.PageRequest;
import org.springframework.dao.DuplicateKeyException;

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
        // Arrange
        String organizationId = "orgId";
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class))).thenReturn(new ArrayList<>());
        
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(TotalCountObject.class)))
                .thenReturn(Collections.singletonList(new TotalCountObject(0L)));
        
        // Act
        PersonnelDisplay result = personnelService.findAll(organizationId, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDisplay.getTotalCount(), result.getTotalCount());
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crewId")
                .employeeId("employeeId")
                .firstName("First")
                .districtId("districtId")
                .jobTitle("Job Title")
                .fleetId("fleetId")
                .secondName("Second")
                .supervisor(false)
                .organizationId("orgId")
                .build();
        
        Personnel personnel = Personnel.builder()
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

        when(personnelDBRepository.save(any())).thenReturn(personnel);
        
        // Act
        Boolean result = personnelService.savePersonnel(dto);
        
        // Assert
        assertTrue(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnFalseWhenDuplicateKeyExceptionThrownOnSavePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crewId")
                .employeeId("employeeId")
                .firstName("First")
                .districtId("districtId")
                .jobTitle("Job Title")
                .fleetId("fleetId")
                .secondName("Second")
                .supervisor(false)
                .organizationId("orgId")
                .build();
        
        doThrow(new DuplicateKeyException("Duplicate key")).when(personnelDBRepository).save(any());
        
        // Act
        Boolean result = personnelService.savePersonnel(dto);
        
        // Assert
        assertFalse(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnTrueWhenUpdatePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("personId")
                .crewId("crewId")
                .employeeId("employeeId")
                .firstName("First")
                .districtId("districtId")
                .jobTitle("Job Title")
                .fleetId("fleetId")
                .secondName("Second")
                .supervisor(false)
                .organizationId("orgId")
                .build();

        Personnel existingPersonnel = Personnel.builder().id(dto.getId()).build();
        when(personnelDBRepository.findById(dto.getId())).thenReturn(Optional.of(existingPersonnel));
        when(personnelDBRepository.save(any())).thenReturn(existingPersonnel);
        
        // Act
        Boolean result = personnelService.updatePersonnel(dto);
        
        // Assert
        assertTrue(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnFalseWhenUpdatePersonnelNotFound() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("personId")
                .build();

        when(personnelDBRepository.findById(dto.getId())).thenReturn(Optional.empty());
        
        // Act
        Boolean result = personnelService.updatePersonnel(dto);
        
        // Assert
        assertFalse(result);
        verify(personnelDBRepository, never()).save(any());
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "id";
        PersonnelDto expectedDto = PersonnelDto.builder().build();
        ArrayList<String> ids = new ArrayList<>();
        ids.add(id);
        
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .build();
        when(personnelService.lookUpPersonnel(eq(ids), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(personnelDisplay);
        
        // Act
        PersonnelDto result = personnelService.findById(id);
        
        // Assert
        assertEquals(expectedDto, result);
    }

    @Test
    public void shouldNotDeletePersonnelWhenNotFound() {
        // Arrange
        String id = "id";
        when(personnelDBRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act
        personnelService.deletePersonnel(id);
        
        // Assert
        verify(personnelDBRepository, never()).deleteById(any());
    }

    @Test
    public void shouldDeletePersonnelWhenFound() {
        // Arrange
        String id = "id";
        Personnel personnel = Personnel.builder().id(id).build();
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));
        
        // Act
        personnelService.deletePersonnel(id);
        
        // Assert
        verify(personnelDBRepository, times(1)).deleteById(id);
    }
}
