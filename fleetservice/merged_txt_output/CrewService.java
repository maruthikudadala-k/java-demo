// ===== Imported from: com.carbo.fleet.dto.CrewDto =====
package com.carbo.fleet.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class CrewDto {
    public String id;

    @NotEmpty
    public String name;

    @NotEmpty
    public String jobPattern;

    @NotEmpty
    public String shiftStart;

    @NotEmpty
    public String startDate;

    public String organizationId;

    public String fleetName;

    public String districtName;

    @NotEmpty
    public String fleetId;

    public Long totalCount;
}



// ===== Imported from: com.carbo.fleet.model.Crew =====
package com.carbo.fleet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Data
@Document(collection = "crews")
@JsonInclude(JsonInclude.Include.NON_NULL)
@CompoundIndex(def = "{'organizationId': 1, 'fleetId':1, 'name': 1}", name = "organizationId_fleetId_name_index", unique = true)
public class Crew {
    @Id
    private String id;

    @Field("name")
    @Size(max = 100, message = "name can not be more the 100 characters")
    String name;

    @Field("jobPattern")
    private String jobPattern;

    @Field("shiftStart")
    private String shiftStart;

    @Field("startDate")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate startDate;

    @Field("organizationId")
    private String organizationId;

    @Field("fleetId")
    private String fleetId;

    @Field("created")
    private Long created = new Date().getTime();

    @Field("modified")
    private Long modified  = new Date().getTime();
}

// ===== Imported from: com.carbo.fleet.model.CrewDisplayObject =====
package com.carbo.fleet.model;

import com.carbo.fleet.dto.CrewDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CrewDisplayObject {
    private List<CrewDto> crews;
    private long totalCount;
}

// ===== Imported from: com.carbo.fleet.model.Fleet =====
package com.carbo.fleet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Document(collection = "fleets")
@CompoundIndex(def = "{'organizationId': 1, 'name': 1}", name = "organization_name_index", unique = true)
public class Fleet {
    @Id
    private String id;

    @Field("name")
    @NotEmpty(message = "name can not be empty")
    @Size(max = 100, message = "name can not be more than 100 characters.")
    String name;

    @Field("districtId")
    @NotEmpty(message = "district ID can not be empty")
    @Size(max = 100, message = "district ID can not be more than 100 characters.")
    String districtId;

    @Field("fleetType")
    private String fleetType;

    @Field("created")
    private Long created = new Date().getTime();

    @Field("modified")
    private Long modified  = new Date().getTime();

    @Field("ts")
    private Long ts;

    @Field("organizationId")
    private String organizationId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCreated() {
        return created;
    }

    public String getFleetType() {
        return fleetType;
    }

    public void setFleetType(String fleetType) {
        this.fleetType = fleetType;
    }
}

// ===== Imported from: com.carbo.fleet.model.TotalCountObject =====
package com.carbo.fleet.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TotalCountObject {
    private Long totalCount;

    public TotalCountObject(Long totalCount) {
        this.totalCount = totalCount;
    }

    public TotalCountObject() {
    }
}

// ===== Imported from: com.carbo.fleet.repository.CrewDbRepository =====
package com.carbo.fleet.repository;

import com.carbo.fleet.model.Crew;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewDbRepository extends MongoRepository<Crew, String> {
    Optional<Crew> findById(String organizationId, String id);

    Optional<List<Crew>> findByOrganizationIdAndFleetId(String organiztionId, String fleet, Pageable Pageable);

    Optional<List<Crew>> findByOrganizationId(String organizationId, Pageable pageable);
}

// ===== Imported from: com.carbo.fleet.repository.FleetMongoDbRepository =====
package com.carbo.fleet.repository;

import com.carbo.fleet.model.Fleet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FleetMongoDbRepository extends MongoRepository<Fleet, String> {
    List<Fleet> findByOrganizationId(String organizationId);

    Optional<Fleet> findDistinctByOrganizationIdAndName(String organizationId, String name);
//    long countByTypeAndFleet(String equipmentType, String fleet);
//
//    long countByTypeInAndFleet(List<String> equipmentTypes, String fleet);
//
//        Fleet findByFleetId(String fleetId);

    List<Fleet> findByOrganizationIdAndIdIn(String organizationId, Set<String> fleetIds);

    List<Fleet> findByOrganizationIdInAndNameIn(Set<String> organizationIds, Set<String> filterFleetName);
}




// ===== Current file: src\main\java\com\carbo\fleet\services\CrewService.java =====
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

        ProjectionOperation project = Aggregation.project()
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

