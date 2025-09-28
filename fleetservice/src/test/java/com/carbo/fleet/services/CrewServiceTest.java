
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrewServiceTest {

    @Mock
    private CrewDbRepository crewDbRepository;

    @Mock
    private FleetMongoDbRepository fleetMongoDbRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CrewService crewService;

    @Test
    void shouldReturnCrewDtoWhenIdExists() {
        String crewId = "1";
        CrewDto expectedCrewDto = CrewDto.builder().id(crewId).name("John Doe").jobPattern("Pattern1").shiftStart("08:00").startDate("01/01/2022").fleetId("Fleet1").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.singletonList(expectedCrewDto)).build();

        when(crewDbRepository.count()).thenReturn(1L);
        when(crewService.lookUpCrew(Collections.singletonList(crewId), null, null, 0, 10)).thenReturn(crewDisplayObject);

        CrewDto actualCrewDto = crewService.findById(crewId);

        assertNotNull(actualCrewDto);
        assertEquals(expectedCrewDto, actualCrewDto);
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenFindingAll() {
        String organizationId = "Org1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(0).build();

        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(null, null, organizationId, 0, 10)).thenReturn(crewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewService.findAll(organizationId, 0, 10);

        assertNotNull(actualCrewDisplayObject);
        assertEquals(0, actualCrewDisplayObject.getTotalCount());
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenFindingAllByFleet() {
        String organizationId = "Org1";
        String fleetName = "Fleet1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(0).build();

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(new Fleet());
        when(crewService.lookUpCrew(null, anyString(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertNotNull(actualCrewDisplayObject);
        assertEquals(0, actualCrewDisplayObject.getTotalCount());
    }

    @Test
    void shouldSaveCrewWhenCrewDtoIsValid() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Pattern1").shiftStart("08:00").startDate("01/01/2022").fleetId("Fleet1").organizationId("Org1").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        crew.setOrganizationId(crewDto.getOrganizationId());
        crew.setFleetId(crewDto.getFleetId());
        crew.setJobPattern(crewDto.getJobPattern());
        crew.setShiftStart(crewDto.getShiftStart());

        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Crew actualCrew = crewService.saveCrew(crewDto);

        assertNotNull(actualCrew);
        assertEquals(crewDto.getId(), actualCrew.getId());
    }

    @Test
    void shouldReturnTrueWhenUpdatingCrewIfExists() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Pattern1").shiftStart("08:00").startDate("01/01/2022").fleetId("Fleet1").organizationId("Org1").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(crew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
    }

    @Test
    void shouldDeleteCrewWhenCrewExists() {
        String crewId = "1";
        Crew crew = new Crew();
        crew.setId(crewId);

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(crew));
        doNothing().when(crewDbRepository).deleteById(crewId);

        crewService.deleteCrew(crewId);

        verify(crewDbRepository, times(1)).deleteById(crewId);
    }

    @Test
    void shouldNotDeleteCrewWhenCrewDoesNotExist() {
        String crewId = "1";

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.empty());

        crewService.deleteCrew(crewId);

        verify(crewDbRepository, never()).deleteById(crewId);
    }
}
