
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
    void shouldReturnCrewDtoWhenCrewExistsById() {
        String crewId = "crew1";
        CrewDto crewDto = CrewDto.builder().id(crewId).name("John Doe").jobPattern("Job").shiftStart("08:00").startDate("01/01/2023").fleetId("fleet1").build();
        List<CrewDto> crewDtos = new ArrayList<>();
        crewDtos.add(crewDto);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewDtos).totalCount(1).build();

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(Collections.singletonList(crewId), null, null, 0, 10)).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(crewId);

        assertNotNull(result);
        assertEquals(crewId, result.getId());
        verify(crewDbRepository).findById(crewId);
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenFindingAll() {
        String organizationId = "org1";
        Long totalCount = 5L;
        when(crewDbRepository.count()).thenReturn(totalCount);
        when(crewService.lookUpCrew(null, null, organizationId, 0, 10)).thenReturn(CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(totalCount).build());

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);

        assertNotNull(result);
        assertEquals(totalCount, result.getTotalCount());
        verify(crewDbRepository).count();
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenFindingAllByFleet() {
        String organizationId = "org1";
        String fleetName = "Fleet A";
        String fleetId = "fleet1";
        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(new Fleet());
        when(crewService.lookUpCrew(null, fleetId, organizationId, 0, 10)).thenReturn(CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(0).build());

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        verify(mongoTemplate).findOne(any(), eq(Fleet.class));
    }

    @Test
    void shouldSaveCrewWhenCrewDtoIsValid() {
        CrewDto crewDto = CrewDto.builder().id("crew1").name("John Doe").jobPattern("Job").shiftStart("08:00").startDate("01/01/2023").organizationId("org1").fleetId("fleet1").build();
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
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    void shouldReturnTrueWhenUpdatingCrewAndCrewExists() {
        CrewDto crewDto = CrewDto.builder().id("crew1").name("John Doe").jobPattern("Job").shiftStart("08:00").startDate("01/01/2023").organizationId("org1").fleetId("fleet1").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(crew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
        verify(crewDbRepository).findById(crewDto.getId());
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    void shouldDeleteCrewWhenCrewExists() {
        String crewId = "crew1";
        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));

        crewService.deleteCrew(crewId);

        verify(crewDbRepository).deleteById(crewId);
    }
}
