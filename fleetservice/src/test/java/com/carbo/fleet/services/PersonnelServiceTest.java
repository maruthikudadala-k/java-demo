
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

        when(personnelDBRepository.findByOrganizationId(organizationId, PageRequest.of(offSet, limit))).thenReturn(Optional.empty());
        when(mongoTemplate.aggregate(any(), anyString(), eq(PersonnelDto.class))).thenReturn(new AggregateOpResult<>(Collections.emptyList()));
        when(mongoTemplate.aggregate(any(), anyString(), eq(TotalCountObject.class))).thenReturn(new AggregateOpResult<>(Collections.singletonList(new TotalCountObject(0L))));

        // Act
        PersonnelDisplay result = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPersonnelDisplayObject().size());
        assertEquals(0L, result.getTotalCount());
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnel() {
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
                .id("personnelId")
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
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "personnelId";
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .build();
        
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(personnelDto))
                .totalCount(1L)
                .build();

        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnelDto));
        when(mongoTemplate.aggregate(any(), anyString(), eq(PersonnelDto.class))).thenReturn(new AggregateOpResult<>(Collections.singletonList(personnelDto)));

        // Act
        PersonnelDto result = personnelService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenExists() {
        // Arrange
        String id = "personnelId";
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
        String organizationId = "org123";
        String personnelName = "John";
        String districtId = "dist123";
        String jobTitle = "Developer";
        int offSet = 0;
        int limit = 10;
        
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(mongoTemplate.aggregate(any(), anyString(), eq(PersonnelDto.class))).thenReturn(new AggregateOpResult<>(Collections.emptyList()));
        when(mongoTemplate.aggregate(any(), anyString(), eq(TotalCountObject.class))).thenReturn(new AggregateOpResult<>(Collections.singletonList(new TotalCountObject(0L))));

        // Act
        PersonnelDisplay result = personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPersonnelDisplayObject().size());
        assertEquals(0L, result.getTotalCount());
    }
}
