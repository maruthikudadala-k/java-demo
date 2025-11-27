
package com.carbo.fleet.services;

import com.carbo.fleet.dto.PersonnelDto;
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
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
        int offset = 0;
        int limit = 10;
        List<PersonnelDto> personnelList = Collections.singletonList(PersonnelDto.builder().build());
        TotalCountObject totalCountObject = new TotalCountObject(1L);
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(personnelList)
                .totalCount(totalCountObject.getTotalCount())
                .build();

        Mockito.when(mongoTemplate.aggregate(any(), anyString(), any())).thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(personnelList, null));
        Mockito.when(mongoTemplate.aggregate(any(), anyString(), any(Class.class))).thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.singletonList(totalCountObject), null));

        // Act
        PersonnelDisplay result = personnelService.findAll(organizationId, offset, limit);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPersonnelDisplayObject().size());
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Manager")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();

        Mockito.when(personnelDBRepository.save(any())).thenReturn(Personnel.builder().id("newId").build());

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldReturnTrueWhenUpdatePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("existingId")
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Manager")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .supervisorId("supervisorId")
                .organizationId("org123")
                .build();

        Mockito.when(personnelDBRepository.findById(dto.getId())).thenReturn(Optional.of(Personnel.builder().build()));
        Mockito.when(personnelDBRepository.save(any())).thenReturn(Personnel.builder().build());

        // Act
        Boolean result = personnelService.updatePersonnel(dto);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "existingId";
        List<PersonnelDto> personnelList = Collections.singletonList(PersonnelDto.builder().build());
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(personnelList)
                .build();

        Mockito.when(mongoTemplate.aggregate(any(), anyString(), any())).thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(personnelList, null));

        // Act
        PersonnelDto result = personnelService.findById(id);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void shouldDeletePersonnelWhenDeleteById() {
        // Arrange
        String id = "existingId";
        Mockito.when(personnelDBRepository.findById(id)).thenReturn(Optional.of(Personnel.builder().build()));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        Mockito.verify(personnelDBRepository).deleteById(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenFindByValue() {
        // Arrange
        String organizationId = "org123";
        String personnelName = "John";
        String districtId = "dist123";
        String jobTitle = "Manager";
        int offset = 0;
        int limit = 10;
        List<PersonnelDto> personnelList = Collections.singletonList(PersonnelDto.builder().build());
        TotalCountObject totalCountObject = new TotalCountObject(1L);
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(personnelList)
                .totalCount(totalCountObject.getTotalCount())
                .build();

        Mockito.when(mongoTemplate.aggregate(any(), anyString(), any())).thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(personnelList, null));
        Mockito.when(mongoTemplate.aggregate(any(), anyString(), any(Class.class))).thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.singletonList(totalCountObject), null));

        // Act
        PersonnelDisplay result = personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offset, limit);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPersonnelDisplayObject().size());
    }
}
