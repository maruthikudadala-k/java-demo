
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
    public void shouldReturnCrewDtoWhenFindByIdIsCalled() {
        // Given
        String id = "1";
        CrewDto crewDto = CrewDto.builder().id(id).name("John Doe").jobPattern("Pattern A").shiftStart("08:00").startDate("01/01/2023").fleetId("FLEET1").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.singletonList(crewDto)).build();

        when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(any(), any(), any(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // When
        CrewDto result = crewService.findById(id);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(crewDbRepository).findById(id);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        // Given
        String organizationId = "ORG1";
        int offSet = 0;
        int limit = 10;
        Long totalCount = 5L;
        when(crewDbRepository.count()).thenReturn(totalCount);
        when(crewService.lookUpCrew(null, null, organizationId, offSet, limit)).thenReturn(new CrewDisplayObject());

        // When
        CrewDisplayObject result = crewService.findAll(organizationId, offSet, limit);

        // Then
        assertNotNull(result);
        verify(crewDbRepository).count();
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        // Given
        String organizationId = "ORG1";
        String fleetName = "Fleet A";
        int offSet = 0;
        int limit = 10;
        Fleet fleet = new Fleet();
        fleet.setId("FLEET1");
        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(null, fleet.getId(), organizationId, offSet, limit)).thenReturn(new CrewDisplayObject());

        // When
        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        // Then
        assertNotNull(result);
        verify(mongoTemplate).findOne(any(), eq(Fleet.class));
    }

    @Test
    public void shouldSaveCrewWhenSaveCrewIsCalled() {
        // Given
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").startDate("01/01/2023").organizationId("ORG1").fleetId("FLEET1").jobPattern("Pattern A").shiftStart("08:00").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        // When
        Crew result = crewService.saveCrew(crewDto);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldReturnTrueWhenUpdateCrewIsCalledAndCrewExists() {
        // Given
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").startDate("01/01/2023").organizationId("ORG1").fleetId("FLEET1").jobPattern("Pattern A").shiftStart("08:00").build();
        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(new Crew()));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(new Crew());

        // When
        Boolean result = crewService.updateCrew(crewDto);

        // Then
        assertTrue(result);
        verify(crewDbRepository).findById(crewDto.getId());
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalledAndCrewExists() {
        // Given
        String id = "1";
        when(crewDbRepository.findById(id)).thenReturn(Optional.of(new Crew()));

        // When
        crewService.deleteCrew(id);

        // Then
        verify(crewDbRepository).deleteById(id);
    }
}
