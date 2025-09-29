
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        String id = "crewId";
        CrewDto expectedCrewDto = CrewDto.builder()
                .id(id)
                .name("John Doe")
                .jobPattern("JobPattern")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();
        
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.singletonList(expectedCrewDto))
                .totalCount(1)
                .build();
        
        when(crewDbRepository.count()).thenReturn(1L);
        when(crewService.lookUpCrew(any(), any(), any(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(id);

        assertEquals(expectedCrewDto, result);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        String organizationId = "orgId";
        CrewDisplayObject expectedCrewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(0)
                .build();

        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(any(), any(), Mockito.eq(organizationId), anyInt(), anyInt())).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);

        assertEquals(expectedCrewDisplayObject, result);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        String organizationId = "orgId";
        String fleetName = "FleetName";
        String fleetId = "fleetId";
        CrewDisplayObject expectedCrewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(0)
                .build();
        
        when(crewDbRepository.count()).thenReturn(0L);
        when(fleetMongoDbRepository.findDistinctByOrganizationIdAndName(organizationId, fleetName))
                .thenReturn(Optional.of(Fleet.builder().id(fleetId).build()));
        when(crewService.lookUpCrew(any(), Mockito.eq(fleetId), Mockito.eq(organizationId), anyInt(), anyInt()))
                .thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertEquals(expectedCrewDisplayObject, result);
    }

    @Test
    public void shouldSaveCrewWhenSaveCrewIsCalled() {
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("John Doe")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .jobPattern("JobPattern")
                .shiftStart("08:00")
                .build();

        Crew savedCrew = new Crew();
        savedCrew.setId(crewDto.getId());
        savedCrew.setName(crewDto.getName());
        savedCrew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        savedCrew.setOrganizationId(crewDto.getOrganizationId());
        savedCrew.setFleetId(crewDto.getFleetId());
        savedCrew.setJobPattern(crewDto.getJobPattern());
        savedCrew.setShiftStart(crewDto.getShiftStart());

        when(crewDbRepository.save(any(Crew.class))).thenReturn(savedCrew);

        Crew result = crewService.saveCrew(crewDto);

        assertEquals(savedCrew, result);
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("John Doe")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .jobPattern("JobPattern")
                .shiftStart("08:00")
                .build();

        Crew existingCrew = new Crew();
        existingCrew.setId(crewDto.getId());
        
        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(existingCrew));

        boolean result = crewService.updateCrew(crewDto);

        assertEquals(true, result);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        String id = "crewId";
        Crew existingCrew = new Crew();
        existingCrew.setId(id);

        when(crewDbRepository.findById(id)).thenReturn(Optional.of(existingCrew));

        crewService.deleteCrew(id);
        
        Mockito.verify(crewDbRepository).deleteById(id);
    }
}
