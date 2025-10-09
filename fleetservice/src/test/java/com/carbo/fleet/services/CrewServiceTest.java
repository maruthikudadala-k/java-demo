
package com.carbo.fleet.services;

import com.carbo.fleet.dto.CrewDto;
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
        String crewId = "crewId";
        CrewDto expectedCrewDto = CrewDto.builder()
                .id(crewId)
                .name("John Doe")
                .jobPattern("Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();

        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.singletonList(expectedCrewDto))
                .totalCount(1)
                .build();

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(expectedCrewDto));
        when(crewService.lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto actualCrewDto = crewService.findById(crewId);

        assertNotNull(actualCrewDto);
        assertEquals(expectedCrewDto.getId(), actualCrewDto.getId());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAll() {
        String organizationId = "orgId";
        CrewDisplayObject expectedCrewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(10)
                .build();

        when(crewDbRepository.count()).thenReturn(10L);
        when(crewService.lookUpCrew(isNull(), isNull(), eq(organizationId), anyInt(), anyInt())).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewService.findAll(organizationId, 0, 10);

        assertNotNull(actualCrewDisplayObject);
        assertEquals(expectedCrewDisplayObject.getTotalCount(), actualCrewDisplayObject.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAllByFleet() {
        String organizationId = "orgId";
        String fleetName = "FleetName";
        String fleetId = "fleetId";

        CrewDisplayObject expectedCrewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(5)
                .build();

        Fleet fleet = new Fleet();
        fleet.setId(fleetId);

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(isNull(), eq(fleetId), eq(organizationId), anyInt(), anyInt())).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertNotNull(actualCrewDisplayObject);
        assertEquals(expectedCrewDisplayObject.getTotalCount(), actualCrewDisplayObject.getTotalCount());
    }

    @Test
    public void shouldSaveCrewSuccessfully() {
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("John Doe")
                .jobPattern("Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();

        when(crewDbRepository.save(any())).thenReturn(new Crew());

        Crew result = crewService.saveCrew(crewDto);

        assertNotNull(result);
        verify(crewDbRepository, times(1)).save(any());
    }

    @Test
    public void shouldUpdateCrewSuccessfully() {
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("John Doe")
                .jobPattern("Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(new Crew()));
        when(crewDbRepository.save(any())).thenReturn(new Crew());

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
        verify(crewDbRepository, times(1)).save(any());
    }

    @Test
    public void shouldDeleteCrewSuccessfully() {
        String crewId = "crewId";
        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));

        crewService.deleteCrew(crewId);

        verify(crewDbRepository, times(1)).deleteById(crewId);
    }
}
