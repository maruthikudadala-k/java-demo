
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

        when(personnelDBRepository.findByOrganizationId(organizationId, PageRequest.of(offSet, limit)))
                .thenReturn(Optional.of(Collections.emptyList()));
        when(mongoTemplate.aggregate(any(), any(), any()))
                .thenReturn(mockAggregationResult(personnelDisplay));

        // Act
        PersonnelDisplay result = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertTrue(result.getPersonnelDisplayObject().isEmpty());
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

        when(personnelDBRepository.save(any())).thenReturn(null);

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldReturnTrueWhenUpdatePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("personnelId")
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Engineer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .supervisorId("supervisorId")
                .organizationId("org123")
                .build();

        when(personnelDBRepository.findById(dto.getId())).thenReturn(Optional.of(new Personnel()));
        when(personnelDBRepository.save(any())).thenReturn(null);

        // Act
        Boolean result = personnelService.updatePersonnel(dto);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "personnelId";
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .build();

        when(mongoTemplate.aggregate(any(), any(), any()))
                .thenReturn(mockAggregationResult(PersonnelDisplay.builder()
                        .personnelDisplayObject(Collections.singletonList(personnelDto))
                        .build()));

        // Act
        PersonnelDto result = personnelService.findById(id);

        // Assert
        assertTrue(result != null && result.getId().equals(id));
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnel() {
        // Arrange
        String id = "personnelId";
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(new Personnel()));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        Mockito.verify(personnelDBRepository).deleteById(id);
    }

    private <T> AggregationResults<T> mockAggregationResult(T result) {
        AggregationResults<T> aggregationResults = Mockito.mock(AggregationResults.class);
        when(aggregationResults.getMappedResults()).thenReturn(Collections.singletonList(result));
        return aggregationResults;
    }
}
