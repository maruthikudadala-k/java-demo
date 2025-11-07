
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
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelDBRepository.findByOrganizationId(organizationId, Pageable.unpaged())).thenReturn(Optional.of(Collections.emptyList()));
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class))).thenReturn(new AggregationResults<>(Collections.emptyList(), new TotalCountObject(0L)));

        // Act
        PersonnelDisplay result = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPersonnelDisplayObject()).isEqualTo(Collections.emptyList());
        assertThat(result.getTotalCount()).isEqualTo(0L);
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Engineer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();

        Personnel createdPersonnel = Personnel.builder()
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

        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(createdPersonnel);

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertThat(result).isTrue();
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnTrueWhenUpdatePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("1")
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Engineer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();

        Personnel existingPersonnel = Personnel.builder()
                .id(dto.getId())
                .crewId("oldCrewId")
                .employeeId("oldEmpId")
                .firstName("OldName")
                .districtId("oldDistId")
                .jobTitle("OldJob")
                .fleetId("oldFleetId")
                .secondName("OldSecond")
                .supervisor(false)
                .organizationId("org123")
                .build();

        when(personnelDBRepository.findById(dto.getId())).thenReturn(Optional.of(existingPersonnel));

        // Act
        Boolean result = personnelService.updatePersonnel(dto);

        // Assert
        assertThat(result).isTrue();
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "1";
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .organizationId("org123")
                .build();

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(personnelDto))
                .totalCount(1L)
                .build();

        when(personnelService.lookUpPersonnel(anyList(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(personnelDisplay);

        // Act
        PersonnelDto result = personnelService.findById(id);

        // Assert
        assertThat(result).isEqualTo(personnelDto);
    }

    @Test
    public void shouldDeletePersonnelWhenDeleteById() {
        // Arrange
        String id = "1";
        Personnel existingPersonnel = Personnel.builder()
                .id(id)
                .firstName("John")
                .build();

        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(existingPersonnel));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenFindByValue() {
        // Arrange
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

        when(personnelService.lookUpPersonnel(null, personnelName, districtId, jobTitle, organizationId, offSet, limit)).thenReturn(personnelDisplay);

        // Act
        PersonnelDisplay result = personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPersonnelDisplayObject()).isEqualTo(Collections.emptyList());
        assertThat(result.getTotalCount()).isEqualTo(0L);
    }
}
