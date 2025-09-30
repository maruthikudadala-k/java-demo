
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
    public void shouldReturnCrewDtoWhenFindByIdIsCalled() {
        String crewId = "1";
        CrewDto crewDto = CrewDto.builder().id(crewId).name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").fleetId("fleet1").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.singletonList(crewDto)).build();

        when(crewDbRepository.findById(any(), eq(crewId))).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(any(), any(), any(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(crewId);

        assertNotNull(result);
        assertEquals(crewId, result.getId());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        String organizationId = "org1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(0).build();

        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(any(), any(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        String organizationId = "org1";
        String fleetName = "Fleet A";
        String fleetId = "fleet1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(0).build();
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(any(), eq(fleetId), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldSaveCrewWhenSaveCrewIsCalled() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").organizationId("org1").fleetId("fleet1").build();
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
    public void shouldReturnTrueWhenUpdateCrewIsCalled() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").organizationId("org1").fleetId("fleet1").build();
        Crew existingCrew = new Crew();
        existingCrew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(existingCrew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(existingCrew);

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String crewId = "1";
        Crew crew = new Crew();
        crew.setId(crewId);

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(crewId);

        verify(crewDbRepository, times(1)).deleteById(crewId);
    }
}
