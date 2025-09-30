
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
import java.util.Collections;
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
    public void shouldReturnCrewDtoWhenIdExists() {
        // Arrange
        String crewId = "crewId";
        CrewDto crewDto = CrewDto.builder()
                .id(crewId)
                .name("John Doe")
                .jobPattern("Full Time")
                .shiftStart("Morning")
                .startDate("01/01/2022")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();

        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.singletonList(crewDto))
                .totalCount(1)
                .build();

        when(crewDbRepository.findById(any(), any())).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(any(), any(), any(), any(), any())).thenReturn(crewDisplayObject);

        // Act
        CrewDto result = crewService.findById(crewId);

        // Assert
        assertNotNull(result);
        assertEquals(crewId, result.getId());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAll() {
        // Arrange
        String organizationId = "orgId";
        int offSet = 0;
        int limit = 10;

        when(crewDbRepository.count()).thenReturn(1L);
        when(crewService.lookUpCrew(any(), any(), eq(organizationId), eq(offSet), eq(limit)))
                .thenReturn(CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(1).build());

        // Act
        CrewDisplayObject result = crewService.findAll(organizationId, offSet, limit);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAllByFleet() {
        // Arrange
        String organizationId = "orgId";
        String fleetName = "Fleet A";
        int offSet = 0;
        int limit = 10;

        Fleet fleet = new Fleet();
        fleet.setId("fleetId");
        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(any(), any(), eq(organizationId), eq(offSet), eq(limit)))
                .thenReturn(CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(1).build());

        // Act
        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
    }

    @Test
    public void shouldSaveCrewWhenCrewDtoIsValid() {
        // Arrange
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("John Doe")
                .jobPattern("Full Time")
                .shiftStart("Morning")
                .startDate("01/01/2022")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());

        when(crewDbRepository.save(any())).thenReturn(crew);

        // Act
        Crew result = crewService.saveCrew(crewDto);

        // Assert
        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
    }

    @Test
    public void shouldReturnTrueWhenUpdatingCrewSuccessfully() {
        // Arrange
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("John Doe")
                .jobPattern("Full Time")
                .shiftStart("Morning")
                .startDate("01/01/2022")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();

        when(crewDbRepository.findById(any())).thenReturn(Optional.of(new Crew()));
        when(crewDbRepository.save(any())).thenReturn(new Crew());

        // Act
        Boolean result = crewService.updateCrew(crewDto);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldDeleteCrewWhenExists() {
        // Arrange
        String crewId = "crewId";
        Crew crew = new Crew();
        crew.setId(crewId);
        
        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(crew));

        // Act
        crewService.deleteCrew(crewId);

        // Assert
        verify(crewDbRepository, times(1)).deleteById(crewId);
    }
}
