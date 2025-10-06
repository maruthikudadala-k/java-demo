
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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
    public void shouldReturnCrewDtoWhenFoundById() {
        String crewId = "123";
        CrewDto crewDto = CrewDto.builder()
                .id(crewId)
                .name("John Doe")
                .jobPattern("Pattern1")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .organizationId("org1")
                .fleetId("fleet1")
                .build();

        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.singletonList(crewDto))
                .totalCount(1)
                .build();

        when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto foundCrewDto = crewService.findById(crewId);
        assertNotNull(foundCrewDto);
        assertEquals(crewId, foundCrewDto.getId());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAll() {
        String organizationId = "org1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.emptyList())
                .totalCount(0)
                .build();

        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(isNull(), isNull(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleet() {
        String organizationId = "org1";
        String fleetName = "Fleet1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.emptyList())
                .totalCount(0)
                .build();

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(new Fleet());
        when(crewService.lookUpCrew(isNull(), anyString(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldSaveCrewWhenValidCrewDtoProvided() {
        CrewDto crewDto = CrewDto.builder()
                .id("123")
                .name("John Doe")
                .jobPattern("Pattern1")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .organizationId("org1")
                .fleetId("fleet1")
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

        Crew savedCrew = crewService.saveCrew(crewDto);
        assertNotNull(savedCrew);
        assertEquals(crewDto.getId(), savedCrew.getId());
    }

    @Test
    public void shouldReturnTrueWhenUpdateCrewIsSuccessful() {
        CrewDto crewDto = CrewDto.builder()
                .id("123")
                .name("John Doe")
                .jobPattern("Pattern1")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .organizationId("org1")
                .fleetId("fleet1")
                .build();

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(new Crew()));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(new Crew());

        boolean updated = crewService.updateCrew(crewDto);
        assertTrue(updated);
    }

    @Test
    public void shouldDeleteCrewWhenCrewExists() {
        String crewId = "123";
        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));

        crewService.deleteCrew(crewId);
        verify(crewDbRepository, times(1)).deleteById(crewId);
    }
}
