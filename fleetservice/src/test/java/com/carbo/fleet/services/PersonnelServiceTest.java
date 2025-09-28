
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
                .thenReturn(new AggregationResults<>(Collections.emptyList(), expectedDisplay.getTotalCount()));

        // Act
        PersonnelDisplay actualDisplay = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertNotNull(actualDisplay);
        assertEquals(expectedDisplay.getTotalCount(), actualDisplay.getTotalCount());
        assertEquals(expectedDisplay.getPersonnelDisplayObject(), actualDisplay.getPersonnelDisplayObject());
    }

    @Test
    public void shouldSavePersonnelAndReturnTrue() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp456")
                .firstName("John")
                .secondName("Doe")
                .districtId("dist789")
                .jobTitle("Developer")
                .fleetId("fleet321")
                .supervisor(false)
                .organizationId("org123")
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

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnFalseWhenDuplicateKeyExceptionOccurs() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp456")
                .firstName("John")
                .secondName("Doe")
                .districtId("dist789")
                .jobTitle("Developer")
                .fleetId("fleet321")
                .supervisor(false)
                .organizationId("org123")
                .build();
        
        when(personnelDBRepository.save(any(Personnel.class))).thenThrow(new DuplicateKeyException("Duplicate key"));

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenUpdatePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("1")
                .crewId("crew123")
                .employeeId("emp456")
                .firstName("John")
                .secondName("Doe")
                .districtId("dist789")
                .jobTitle("Developer")
                .fleetId("fleet321")
                .supervisor(false)
                .organizationId("org123")
                .build();

        Personnel existingPersonnel = Personnel.builder().id("1").build();
        when(personnelDBRepository.findById("1")).thenReturn(Optional.of(existingPersonnel));
        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(existingPersonnel);

        // Act
        Boolean result = personnelService.updatePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnFalseWhenUpdatePersonnelNotFound() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder().id("1").build();
        when(personnelDBRepository.findById("1")).thenReturn(Optional.empty());

        // Act
        Boolean result = personnelService.updatePersonnel(dto);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "1";
        PersonnelDto expectedDto = PersonnelDto.builder().id("1").firstName("John").build();
        List<PersonnelDto> personnelList = new ArrayList<>();
        personnelList.add(expectedDto);
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder().personnelDisplayObject(personnelList).build();

        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new AggregationResults<>(personnelList, 1L));

        // Act
        PersonnelDto actualDto = personnelService.findById(id);

        // Assert
        assertNotNull(actualDto);
        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getFirstName(), actualDto.getFirstName());
    }

    @Test
    public void shouldDeletePersonnel() {
        // Arrange
        String id = "1";
        Personnel personnel = Personnel.builder().id(id).build();
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository).deleteById(id);
    }

    @Test
    public void shouldNotDeletePersonnelWhenNotFound() {
        // Arrange
        String id = "1";
        when(personnelDBRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository, never()).deleteById(id);
    }
}
