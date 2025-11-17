
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
        String organizationId = "org1";
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new AggregationResults<>(Collections.emptyList(), new TotalCountObject(0L)));

        // Act
        PersonnelDisplay result = personnelService.findAll(organizationId, 0, 10);

        // Assert
        assertEquals(expectedDisplay, result);
    }

    @Test
    public void shouldSavePersonnelSuccessfully() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("district1")
                .jobTitle("Engineer")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org1")
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
    public void shouldUpdatePersonnelSuccessfully() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("1")
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("district1")
                .jobTitle("Engineer")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(true)
                .supervisorId("supervisor1")
                .organizationId("org1")
                .build();

        Personnel existingPersonnel = Personnel.builder()
                .id(dto.getId())
                .crewId("oldCrew")
                .employeeId("oldEmp")
                .firstName("OldName")
                .districtId("oldDistrict")
                .jobTitle("OldJob")
                .fleetId("oldFleet")
                .secondName("OldSecondName")
                .supervisor(false)
                .organizationId("org1")
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
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "1";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp1")
                .supervisor(true)
                .organizationId("org1")
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
        assertEquals(expectedDto, result);
    }

    @Test
    public void shouldDeletePersonnelSuccessfully() {
        // Arrange
        String id = "1";
        Personnel personnel = Personnel.builder().id(id).build();
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenFindByValue() {
        // Arrange
        String organizationId = "org1";
        String personnelName = "John";
        String districtId = "district1";
        String jobTitle = "Engineer";
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.lookUpPersonnel(any(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay result = personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, 0, 10);

        // Assert
        assertEquals(expectedDisplay, result);
    }
}
