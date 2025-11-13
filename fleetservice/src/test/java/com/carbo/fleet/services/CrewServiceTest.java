
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
import org.mockito.Mockito;
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
    public void shouldReturnCrewDtoWhenIdIsValid() {
        // Arrange
        String crewId = "crewId";
        CrewDto expectedCrewDto = CrewDto.builder().id(crewId).name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").fleetId("fleetId").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.singletonList(expectedCrewDto)).build();

        when(crewDbRepository.findById(anyString(), anyString())).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // Act
        CrewDto actualCrewDto = crewService.findById(crewId);

        // Assert
        assertNotNull(actualCrewDto);
        assertEquals(expectedCrewDto.getId(), actualCrewDto.getId());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAll() {
        // Arrange
        String organizationId = "orgId";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(0).build();

        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(isNull(), isNull(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject actualDisplayObject = crewService.findAll(organizationId, 0, 10);

        // Assert
        assertNotNull(actualDisplayObject);
        assertEquals(0, actualDisplayObject.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAllByFleet() {
        // Arrange
        String organizationId = "orgId";
        String fleetName = "FleetName";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(0).build();
        Fleet fleet = new Fleet();
        fleet.setId("fleetId");

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(isNull(), eq(fleet.getId()), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject actualDisplayObject = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        // Assert
        assertNotNull(actualDisplayObject);
        assertEquals(0, actualDisplayObject.getTotalCount());
    }

    @Test
    public void shouldSaveCrewWhenCrewDtoIsValid() {
        // Arrange
        CrewDto crewDto = CrewDto.builder().id("crewId").name("John Doe").startDate("01/01/2023").organizationId("orgId").fleetId("fleetId").jobPattern("Pattern").shiftStart("08:00").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        crew.setOrganizationId(crewDto.getOrganizationId());
        crew.setFleetId(crewDto.getFleetId());
        crew.setJobPattern(crewDto.getJobPattern());
        crew.setShiftStart(crewDto.getShiftStart());

        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        // Act
        Crew savedCrew = crewService.saveCrew(crewDto);

        // Assert
        assertNotNull(savedCrew);
        assertEquals(crewDto.getId(), savedCrew.getId());
    }

    @Test
    public void shouldUpdateCrewWhenCrewDtoIsValid() {
        // Arrange
        CrewDto crewDto = CrewDto.builder().id("crewId").name("John Doe").startDate("01/01/2023").organizationId("orgId").fleetId("fleetId").jobPattern("Pattern").shiftStart("08:00").build();
        Crew existingCrew = new Crew();
        existingCrew.setId(crewDto.getId());
        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(existingCrew));

        // Act
        Boolean result = crewService.updateCrew(crewDto);

        // Assert
        assertTrue(result);
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldDeleteCrewWhenCrewExists() {
        // Arrange
        String crewId = "crewId";
        Crew crew = new Crew();
        crew.setId(crewId);
        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(crew));

        // Act
        crewService.deleteCrew(crewId);

        // Assert
        verify(crewDbRepository).deleteById(crewId);
    }
}
