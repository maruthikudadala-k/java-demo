
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
    public void shouldReturnCrewDtoWhenFound() {
        // Arrange
        String crewId = "crewId";
        CrewDto crewDto = CrewDto.builder()
                .id(crewId)
                .name("John Doe")
                .jobPattern("Pattern A")
                .shiftStart("09:00")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();
        
        List<CrewDto> crewDtoList = new ArrayList<>();
        crewDtoList.add(crewDto);
        
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(crewDtoList)
                .totalCount(1)
                .build();
        
        when(crewDbRepository.count()).thenReturn(1L);
        when(crewService.lookUpCrew(Collections.singletonList(crewId), null, null, 0, 10))
                .thenReturn(crewDisplayObject);

        // Act
        CrewDto result = crewService.findById(crewId);

        // Assert
        assertNotNull(result);
        assertEquals(crewId, result.getId());
        verify(crewService, times(1)).lookUpCrew(Collections.singletonList(crewId), null, null, 0, 10);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAll() {
        // Arrange
        String organizationId = "orgId";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.emptyList())
                .totalCount(0)
                .build();
        
        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(null, null, organizationId, offSet, limit))
                .thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject result = crewService.findAll(organizationId, offSet, limit);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        verify(crewService, times(1)).lookUpCrew(null, null, organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAllByFleet() {
        // Arrange
        String organizationId = "orgId";
        String fleetName = "Fleet A";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.emptyList())
                .totalCount(0)
                .build();
        
        Fleet fleet = new Fleet();
        fleet.setId("fleetId");
        
        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(null, fleet.getId(), organizationId, offSet, limit))
                .thenReturn(crewDisplayObject);

        // Act
        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        verify(crewService, times(1)).lookUpCrew(null, fleet.getId(), organizationId, offSet, limit);
    }

    @Test
    public void shouldSaveCrewWhenValidCrewDtoIsProvided() {
        // Arrange
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("John Doe")
                .jobPattern("Pattern A")
                .shiftStart("09:00")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
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

        // Act
        Crew result = crewService.saveCrew(crewDto);

        // Assert
        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
        verify(crewDbRepository, times(1)).save(any(Crew.class));
    }

    @Test
    public void shouldReturnTrueWhenUpdatingExistingCrew() {
        // Arrange
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("John Doe")
                .jobPattern("Pattern A")
                .shiftStart("09:00")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();
        
        Crew crew = new Crew();
        crew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(crew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        // Act
        Boolean result = crewService.updateCrew(crewDto);

        // Assert
        assertTrue(result);
        verify(crewDbRepository, times(1)).findById(crewDto.getId());
        verify(crewDbRepository, times(1)).save(any(Crew.class));
    }

    @Test
    public void shouldDeleteCrewWhenFound() {
        // Arrange
        String crewId = "crewId";
        Crew crew = new Crew();
        crew.setId(crewId);

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(crew));

        // Act
        crewService.deleteCrew(crewId);

        // Assert
        verify(crewDbRepository, times(1)).deleteById(crewId);
    }
}
