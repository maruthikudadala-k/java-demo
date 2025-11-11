
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
    public void shouldReturnCrewDtoWhenFindByIdIsCalled() {
        // Arrange
        String crewId = "crewId";
        CrewDto crewDto = new CrewDto();
        crewDto.setId(crewId);
        crewDto.setName("John Doe");
        crewDto.setJobPattern("Pilot");
        crewDto.setShiftStart("08:00");
        crewDto.setStartDate("01/01/2023");
        crewDto.setFleetId("fleetId");
        crewDto.setOrganizationId("orgId");

        List<CrewDto> crewList = new ArrayList<>();
        crewList.add(crewDto);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewList).totalCount(1).build();

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(Collections.singletonList(crewId), null, null, 0, 10)).thenReturn(crewDisplayObject);

        // Act
        CrewDto result = crewService.findById(crewId);

        // Assert
        assertNotNull(result);
        assertEquals(crewId, result.getId());
        verify(crewDbRepository).findById(crewId);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        // Arrange
        String organizationId = "orgId";
        int offSet = 0;
        int limit = 10;
        when(crewDbRepository.count()).thenReturn(100L);
        when(crewService.lookUpCrew(null, null, organizationId, offSet, limit)).thenReturn(new CrewDisplayObject());

        // Act
        CrewDisplayObject result = crewService.findAll(organizationId, offSet, limit);

        // Assert
        assertNotNull(result);
        verify(crewDbRepository).count();
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        // Arrange
        String organizationId = "orgId";
        String fleetName = "Fleet A";
        int offSet = 0;
        int limit = 10;

        Fleet fleet = new Fleet();
        fleet.setId("fleetId");
        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(null, fleet.getId(), organizationId, offSet, limit)).thenReturn(new CrewDisplayObject());

        // Act
        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        // Assert
        assertNotNull(result);
        verify(mongoTemplate).findOne(any(), eq(Fleet.class));
    }

    @Test
    public void shouldSaveCrewWhenSaveCrewIsCalled() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crewId");
        crewDto.setName("John Doe");
        crewDto.setStartDate("01/01/2023");
        crewDto.setOrganizationId("orgId");
        crewDto.setFleetId("fleetId");
        crewDto.setJobPattern("Pilot");
        crewDto.setShiftStart("08:00");

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
        Crew result = crewService.saveCrew(crewDto);

        // Assert
        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldReturnTrueWhenUpdateCrewIsCalledAndCrewExists() {
        // Arrange
        CrewDto crewDto = new CrewDto();
        crewDto.setId("crewId");
        crewDto.setName("John Doe");
        crewDto.setStartDate("01/01/2023");
        crewDto.setOrganizationId("orgId");
        crewDto.setFleetId("fleetId");
        crewDto.setJobPattern("Pilot");
        crewDto.setShiftStart("08:00");

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(new Crew()));

        // Act
        Boolean result = crewService.updateCrew(crewDto);

        // Assert
        assertTrue(result);
        verify(crewDbRepository).findById(crewDto.getId());
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalledAndCrewExists() {
        // Arrange
        String crewId = "crewId";
        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));

        // Act
        crewService.deleteCrew(crewId);

        // Assert
        verify(crewDbRepository).deleteById(crewId);
    }
}
