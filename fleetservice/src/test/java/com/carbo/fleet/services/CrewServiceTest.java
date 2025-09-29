
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
    public void shouldReturnCrewDtoWhenFindByIdIsCalled() {
        // Given
        String crewId = "test-crew-id";
        CrewDto crewDto = CrewDto.builder().id(crewId).name("Test Name").jobPattern("Pattern").shiftStart("09:00").startDate("01/01/2023").fleetId("fleet-1").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.singletonList(crewDto)).build();

        when(crewDbRepository.findById(anyString(), anyString())).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // When
        CrewDto result = crewService.findById(crewId);

        // Then
        assertNotNull(result);
        assertEquals(crewId, result.getId());
        verify(crewService).lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        // Given
        String organizationId = "test-organization";
        int offSet = 0;
        int limit = 10;

        when(crewDbRepository.count()).thenReturn(5L);
        when(crewService.lookUpCrew(isNull(), isNull(), eq(organizationId), eq(offSet), eq(limit)))
                .thenReturn(CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(5).build());

        // When
        CrewDisplayObject result = crewService.findAll(organizationId, offSet, limit);

        // Then
        assertNotNull(result);
        assertEquals(5, result.getTotalCount());
        verify(crewService).lookUpCrew(isNull(), isNull(), eq(organizationId), eq(offSet), eq(limit));
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        // Given
        String organizationId = "test-organization";
        String fleetName = "FleetName";
        int offSet = 0;
        int limit = 10;

        Fleet fleet = new Fleet();
        fleet.setId("fleet-id");

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(isNull(), eq(fleet.getId()), eq(organizationId), eq(offSet), eq(limit)))
                .thenReturn(CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(5).build());

        // When
        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        // Then
        assertNotNull(result);
        assertEquals(5, result.getTotalCount());
        verify(crewService).lookUpCrew(isNull(), eq(fleet.getId()), eq(organizationId), eq(offSet), eq(limit));
    }

    @Test
    public void shouldSaveCrewWhenSaveCrewIsCalled() {
        // Given
        CrewDto crewDto = CrewDto.builder().id("crew-id").name("Test Crew").startDate("01/01/2023").organizationId("org-id").fleetId("fleet-id").jobPattern("Pattern").shiftStart("09:00").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        crew.setOrganizationId(crewDto.getOrganizationId());
        crew.setFleetId(crewDto.getFleetId());
        crew.setJobPattern(crewDto.getJobPattern());
        crew.setShiftStart(crewDto.getShiftStart());

        when(crewDbRepository.save(crew)).thenReturn(crew);

        // When
        Crew result = crewService.saveCrew(crewDto);

        // Then
        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldReturnTrueWhenUpdateCrewIsCalledAndCrewExists() {
        // Given
        CrewDto crewDto = CrewDto.builder().id("crew-id").name("Test Crew").startDate("01/01/2023").organizationId("org-id").fleetId("fleet-id").jobPattern("Pattern").shiftStart("09:00").build();
        Optional<Crew> existingCrew = Optional.of(new Crew());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(existingCrew);

        // When
        Boolean result = crewService.updateCrew(crewDto);

        // Then
        assertTrue(result);
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        // Given
        String crewId = "crew-id";
        Optional<Crew> crew = Optional.of(new Crew());
        
        when(crewDbRepository.findById(crewId)).thenReturn(crew);

        // When
        crewService.deleteCrew(crewId);

        // Then
        verify(crewDbRepository).deleteById(crewId);
    }
}
