
package com.carbo.fleet.services;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.repository.CrewDbRepository;
import com.carbo.fleet.repository.FleetMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrewServiceTest {

    @Mock
    private CrewDbRepository crewDbRepository;

    @Mock
    private FleetMongoDbRepository fleetMongoDbRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CrewService crewService;

    @Test
    public void shouldReturnCrewDtoWhenFoundById() {
        // Arrange
        String crewId = "1";
        CrewDto crewDto = CrewDto.builder()
                .id(crewId)
                .name("John Doe")
                .jobPattern("Full-time")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .fleetId("fleet1")
                .organizationId("org1")
                .build();

        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(List.of(crewDto))
                .totalCount(1)
                .build();

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(any(), any(), any(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // Act
        CrewDto result = crewService.findById(crewId);

        // Assert
        assertNotNull(result);
        assertEquals(crewId, result.getId());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAll() {
        // Arrange
        String organizationId = "org1";
        Long totalCount = 10L;
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(totalCount)
                .build();

        when(crewDbRepository.count()).thenReturn(totalCount);
        when(crewService.lookUpCrew(any(), any(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(totalCount, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleet() {
        // Arrange
        String organizationId = "org1";
        String fleetName = "Fleet A";
        Pageable pageable = Pageable.ofSize(10);
        String fleetId = "fleet1";
        Long totalCount = 10L;
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(totalCount)
                .build();
        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(new Fleet());
        when(crewService.lookUpCrew(any(), eq(fleetId), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(totalCount, result.getTotalCount());
    }

    @Test
    public void shouldSaveCrewWhenCrewDtoIsValid() {
        // Arrange
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("John Doe")
                .jobPattern("Full-time")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .fleetId("fleet1")
                .organizationId("org1")
                .build();

        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        crew.setOrganizationId(crewDto.getOrganizationId());
        crew.setFleetId(crewDto.getFleetId());
        crew.setJobPattern(crewDto.getJobPattern());
        crew.setShiftStart(crewDto.getShiftStart());

        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        // Act
        Crew result = crewService.saveCrew(crewDto);

        // Assert
        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
    }

    @Test
    public void shouldReturnFalseWhenUpdateCrewNotFound() {
        // Arrange
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("John Doe")
                .jobPattern("Full-time")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .fleetId("fleet1")
                .organizationId("org1")
                .build();

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.empty());

        // Act
        Boolean result = crewService.updateCrew(crewDto);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldDeleteCrewWhenFound() {
        // Arrange
        String crewId = "1";
        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));

        // Act
        crewService.deleteCrew(crewId);

        // Assert
        verify(crewDbRepository, times(1)).deleteById(crewId);
    }
}
