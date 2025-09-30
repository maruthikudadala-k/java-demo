package com.carbo.fleet.services;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.TotalCountObject;
import com.carbo.fleet.repository.CrewDbRepository;
import com.carbo.fleet.repository.FleetMongoDbRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CrewService {

    public static final Logger logger = LoggerFactory.getLogger(CrewService.class);
    CrewDbRepository crewDbRepository;
    FleetMongoDbRepository fleetMongoDbRepository;
    MongoTemplate mongoTemplate;

    @Autowired
    public CrewService(CrewDbRepository crewDbRepository, FleetMongoDbRepository fleetMongoDbRepository, MongoTemplate mongoTemplate) {
        this.crewDbRepository = crewDbRepository;
        this.fleetMongoDbRepository = fleetMongoDbRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public CrewDto findById(String id) {
        ArrayList<String> list = new ArrayList<>();
        list.add(id);
        CrewDisplayObject crews = lookUpCrew(list, null, null, 0, 10);
        return crews.getCrews().stream().findFirst().orElse(null);
    }

    public CrewDisplayObject findAll(String organizationId, int offSet, int limit) {
        Long totalCount = crewDbRepository.count();
        return lookUpCrew(null, null, organizationId, offSet, limit);

    }

    public CrewDisplayObject findAllByFleet(String organizationId, String fleetName, int offSet, int limit) {
        Pageable pageable = PageRequest.of(offSet / limit, limit);
        List<CrewDto> filteredCrews = new ArrayList<>();

        Query fleetQuery = new Query();
        fleetQuery.addCriteria(Criteria.where("organizationId").is(organizationId)
                .and("name").regex(Pattern.compile(Pattern.quote(fleetName) + ".*", Pattern.CASE_INSENSITIVE)));

        Optional<Fleet> fleetData = Optional.ofNullable(mongoTemplate.findOne(fleetQuery, Fleet.class));
        String fleetId = fleetData.isPresent() ? fleetData.get().getId() : "";
        return lookUpCrew(null, fleetId, organizationId, offSet, limit);
    }

    public Crew saveCrew(CrewDto crewDto) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            Crew crew = new Crew();
            crew.setId(crewDto.getId());
            crew.setName(crewDto.getName());
            crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), formatter));
            crew.setOrganizationId(crewDto.getOrganizationId());
            crew.setFleetId(crewDto.getFleetId());
            crew.setJobPattern(crewDto.getJobPattern());
            crew.setShiftStart(crewDto.getShiftStart());
            return crewDbRepository.save(crew);
        } catch (DuplicateKeyException e) {
            logger.error("duplicate key Value inside Crew" +e.getMessage());
            return null;
        }
    }

    public Boolean updateCrew(CrewDto crewDto) {
        try {
            Optional<Crew> crewData = crewDbRepository.findById(crewDto.getId());
            if (crewData.isPresent()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                Crew crew = new Crew();
                crew.setId(crewDto.getId());
                crew.setName(crewDto.getName());
                crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), formatter));
                crew.setOrganizationId(crewDto.getOrganizationId());
                crew.setFleetId(crewDto.getFleetId());
                crew.setJobPattern(crewDto.getJobPattern());
                crew.setShiftStart(crewDto.getShiftStart());
                crewDbRepository.save(crew);
                return true;
            }
        } catch (Exception e) {
            logger.error("Exception raised while updating personnel" + e.getMessage());
        }
        return false;
    }

    public void deleteCrew(String id) {
        Optional<Crew> crew = crewDbRepository.findById(id);
        if (crew.isPresent()) {
            crewDbRepository.deleteById(id);
        }
    }

    public CrewDisplayObject lookUpCrew(List<String> crewId, String fleetId, String organizationId, int offSet, int limit) {

        LookupOperation lookupFleet = LookupOperation.newLookup()
                .from("fleets")
                .localField("fleetId")
                .foreignField("_id")
                .as("fleet");

        UnwindOperation unwindFleet = Aggregation.unwind("fleet", true);

        LookupOperation lookupDistrict = LookupOperation.newLookup()
                .from("districts")
                .localField("fleet.districtId")
                .foreignField("_id")
                .as("district");

        UnwindOperation unwindDistrict = Aggregation.unwind("district", true);

        ProjectionOperation project = Aggregation.java_springboot-master()
                .and("fleet.name").as("fleetName")
                .and("district.name").as("districtName")
                .and(DateOperators.dateOf("startDate")
                        .toString("%m/%d/%Y")
                        .withTimezone(DateOperators.Timezone.valueOf("Asia/Kolkata")))
                .as("startDate")
                .andInclude("name", "jobPattern", "shiftStart", "organizationId", "fleetId");


        SkipOperation skip = Aggregation.skip((long) offSet);
        LimitOperation limitValue = Aggregation.limit(limit);
        MatchOperation match;
        if (crewId != null) {
            match = Aggregation.match(Criteria.where("_id").in(crewId));
        } else if (fleetId != null) {
            match = Aggregation.match(Criteria.where("fleetId").is(fleetId));
        } else {
            match = Aggregation.match(Criteria.where("organizationId").is(organizationId));
        }
        Aggregation aggregation = Aggregation.newAggregation(
                match,
                lookupFleet,
                unwindFleet,
                lookupDistrict,
                unwindDistrict,
                project,
                skip,
                limitValue
        );
        Aggregation countAggregation = Aggregation.newAggregation(
                match,
                Aggregation.count().as("totalCount")
        );
        TotalCountObject totalCountObject = mongoTemplate.aggregate(countAggregation, "crews", TotalCountObject.class).getUniqueMappedResult();

        return CrewDisplayObject.builder()
                .crews(mongoTemplate.aggregate(aggregation, "crews", CrewDto.class).getMappedResults())
                .totalCount(totalCountObject != null ? totalCountObject.getTotalCount() : 0)
                .build();
    }

}
