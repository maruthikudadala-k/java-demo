
package com.carbo.fleet.services;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.TotalCountObject;
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
    void shouldReturnCrewDtoWhenIdIsFound() {
        String crewId = "1";
        CrewDto crewDto = CrewDto.builder().id(crewId).name("John Doe").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.singletonList(crewDto))
                .totalCount(1)
                .build();

        when(crewDbRepository.count()).thenReturn(1L);
        when(crewService.lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(crewId);

        assertNotNull(result);
        assertEquals(crewId, result.getId());
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenFindingAll() {
        String organizationId = "org1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(0)
                .build();

        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(isNull(), isNull(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);

        assertNotNull(result);
        assertTrue(result.getCrews().isEmpty());
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenFindingAllByFleet() {
        String organizationId = "org1";
        String fleetName = "Fleet A";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(0)
                .build();

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(new Fleet());
        when(crewService.lookUpCrew(isNull(), anyString(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertNotNull(result);
        assertTrue(result.getCrews().isEmpty());
    }

    @Test
    void shouldSaveCrewWhenCrewDtoIsValid() {
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("John Doe")
                .startDate("01/01/2022")
                .organizationId("org1")
                .fleetId("fleet1")
                .jobPattern("Full-Time")
                .shiftStart("Morning")
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

        Crew result = crewService.saveCrew(crewDto);

        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
    }

    @Test
    void shouldUpdateCrewWhenCrewDtoExists() {
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("John Doe")
                .startDate("01/01/2022")
                .organizationId("org1")
                .fleetId("fleet1")
                .jobPattern("Full-Time")
                .shiftStart("Morning")
                .build();

        Crew existingCrew = new Crew();
        existingCrew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(existingCrew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(existingCrew);

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
    }

    @Test
    void shouldDeleteCrewWhenExists() {
        String crewId = "1";
        Crew crew = new Crew();
        crew.setId(crewId);

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(crewId);

        verify(crewDbRepository, times(1)).deleteById(crewId);
    }
}
