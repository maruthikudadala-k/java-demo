
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void shouldReturnCrewDtoWhenFindById() {
        String crewId = "1";
        CrewDto crewDto = CrewDto.builder().id(crewId).name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").fleetId("Fleet1").build();
        List<CrewDto> crewDtoList = new ArrayList<>();
        crewDtoList.add(crewDto);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewDtoList).build();

        when(crewDbRepository.findById(any())).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(any(), any(), any(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(crewId);

        assertEquals(crewDto.getId(), result.getId());
        verify(crewService).lookUpCrew(any(), any(), any(), anyInt(), anyInt());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAll() {
        String organizationId = "Org1";
        int offSet = 0;
        int limit = 10;
        Long totalCount = 1L;
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(totalCount).build();

        when(crewDbRepository.count()).thenReturn(totalCount);
        when(crewService.lookUpCrew(any(), any(), eq(organizationId), eq(offSet), eq(limit))).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, offSet, limit);

        assertEquals(totalCount, result.getTotalCount());
        verify(crewDbRepository).count();
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleet() {
        String organizationId = "Org1";
        String fleetName = "Fleet1";
        int offSet = 0;
        int limit = 10;
        List<CrewDto> crewList = new ArrayList<>();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewList).build();
        Fleet fleet = new Fleet();
        fleet.setId("FleetId1");

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(any(), any(), eq(organizationId), eq(offSet), eq(limit))).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        assertEquals(crewList, result.getCrews());
        verify(mongoTemplate).findOne(any(), eq(Fleet.class));
    }

    @Test
    public void shouldSaveCrewWhenSaveCrew() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").startDate("01/01/2023").organizationId("Org1").fleetId("Fleet1").jobPattern("Pattern").shiftStart("08:00").build();
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

        assertEquals(crew.getId(), result.getId());
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldUpdateCrewWhenUpdateCrew() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").startDate("01/01/2023").organizationId("Org1").fleetId("Fleet1").jobPattern("Pattern").shiftStart("08:00").build();
        Crew existingCrew = new Crew();
        existingCrew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(existingCrew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(existingCrew);

        Boolean result = crewService.updateCrew(crewDto);

        assertEquals(true, result);
        verify(crewDbRepository).findById(crewDto.getId());
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrew() {
        String crewId = "1";
        Crew existingCrew = new Crew();
        existingCrew.setId(crewId);

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(existingCrew));

        crewService.deleteCrew(crewId);

        verify(crewDbRepository).deleteById(crewId);
    }
}
