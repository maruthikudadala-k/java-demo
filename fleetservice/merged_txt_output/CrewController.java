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

// ===== Imported from: com.carbo.fleet.services.CrewService =====
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

// ===== Imported from: com.carbo.fleet.utils.Constants =====
package com.carbo.fleet.utils;

public class Constants {

    public static final String ADMIN = "ADMIN";
    public static final String CARBO_ADMIN = "CARBO_ADMIN";
    public static final String BACK_OFFICE =  "BACK_OFFICE";
    public static final String USER = "USER";
    public static final String READ_ONLY = "READ_ONLY";
    public static final String OPERATION = "OPERATION";
    public static final String SALES_USER = "SALES_USER";
    public static final String MOVE_ONSITE_EQUIPMENT = "MOVE_ONSITE_EQUIPMENT";
    public static final String APP = "APP";
    public static final String SUPER_SALES_USER = "SUPER_SALES_USER";
    public static final String MS_ORGANIZATION = "MS_ORGANIZATION";
    public static final String MS_ADMIN = "MS_ADMIN";
    public static final String MS_WELL = "MS_WELL";
    public static final String MS_PAD = "MS_PAD";
    public static final String MS_OPERATOR = "MS_OPERATOR";
    public static final String MS_DISTRICT = "MS_DISTRICT";
    public static final String MS_FLEET = "MS_FLEET";
    public static final String MS_VENDOR = "MS_VENDOR";
    public static final String MS_EMAIL = "MS_EMAIL";
    public static final String MS_MISC_DATA = "MS_MISC_DATA";
    public static final String MS_SERVICE_COMPANY = "MS_SERVICE_COMPANY";
    public static final String MS_JOB = "MS_JOB";
    public static final String MS_PUMP_ISSUE = "MS_PUMP_ISSUE";
    public static final String MS_ACTIVITY_LOG = "MS_ACTIVITY_LOG";
    public static final String MS_FIELD_TICKET = "MS_FIELD_TICKET";
    public static final String MS_ONSITE_EQUIPMENT = "MS_ONSITE_EQUIPMENT";
    public static final String MS_CHANGE_LOG = "MS_CHANGE_LOG";
    public static final String MS_PROPPANT_DELIVERY = "MS_PROPPANT_DELIVERY";
    public static final String MS_CHEMICAL_DELIVERY = "MS_CHEMICAL_DELIVERY";
    public static final String MS_PROPPANT_STAGE = "MS_PROPPANT_STAGE";
    public static final String MS_CHEMICAL_STAGE = "MS_CHEMICAL_STAGE";
    public static final String MS_WS = "MS_WS";
    public static final String MS_PUMP_SCHEDULE = "MS_PUMP_SCHEDULE";
    public static final String MS_WELL_INFO = "MS_WELL_INFO";
    public static final String MS_CHECKLIST = "MS_CHECKLIST";
    public static final String MS_WORKOVER = "MS_WORKOVER";
    public static final String MS_MAINTENANCE = "MS_MAINTENANCE";
    public static final String MS_CONSUMABLE = "MS_CONSUMABLE";
    public static final String MS_OPERATION_OVERVIEW = "MS_OPERATION_OVERVIEW";
    public static final String ORGANIZATION = "ORGANIZATION";
    public static final String USER_MANAGEMENT = "USER_MANAGEMENT";

    public static final String PRICEBOOK = "PRICEBOOK";
    public static final String PROCUREMENT = "PROCUREMENT";

    public static final String CREW_SCHEDULING = "CREW_SCHEDULING";

    public static final String FIELDCOORDINATOR = "FIELDCOORDINATOR";

    public static final String SALES_FIELD_USER = "SALES_FIELD_USER";

    public static final String SERVICEMANAGER = "SERVICEMANAGER";
    public static final String UNABLE_TO_FETCH_DATA_CODE ="UNABLE_TO_FETCH_DATA" ;
    public static final String UNABLE_TO_FETCH_DATA_MESSAGE ="unable to fetch data" ;
    public static final String CREW_ALREADY_EXISTS = "Crew already exists";
    public static final String CREW_UPDATION_FAILED = "Unable to update Crew";
    public static final String PERSONNEL_ALREADY_EXISTS = "Personnel already exists";
    public static final String UNABLE_TO_UPDATE_PERSONNEL = "Unable to update Personnel";
    public static final String PERSONNEL_CREATED = "personnel_created";
}

// ===== Imported from: com.carbo.fleet.utils.ControllerUtil.getOrganizationId =====
package com.carbo.fleet.utils;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

public class ControllerUtil {
    public static String getOrganizationId(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return ((Map) ((OAuth2Authentication) principal).getUserAuthentication().getDetails()).get("organizationId").toString();
    }
    public static String getOrganizationType(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return ((Map) ((OAuth2Authentication) principal).getUserAuthentication().getDetails()).get("organizationType").toString();
    }
    public static String getUserName(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return ((Map) ((OAuth2Authentication) principal).getUserAuthentication().getDetails()).get("userName").toString();
    }
}

// ===== Current file: src\main\java\com\carbo\fleet\controllers\CrewController.java =====
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.services.CrewService;
import com.carbo.fleet.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.carbo.fleet.utils.ControllerUtil.getOrganizationId;

@RestController
@RequestMapping("/v1/crew")
public class CrewController {

    CrewService crewService;

    @Autowired
    public CrewController(CrewService crewService) {
        this.crewService = crewService;
    }

    @GetMapping("/")
    public CrewDisplayObject getAllCrew(HttpServletRequest request,
                                                    @RequestParam(name = "offSet", defaultValue = "0", required = false) int offSet,
                                                    @RequestParam(name = "limit", defaultValue = "10", required = false) int limit) {
        String organizationId = getOrganizationId(request);
        return crewService.findAll(organizationId, offSet, limit);

    }

    @GetMapping("/{id}")
    public CrewDto getCrew(HttpServletRequest request, @PathVariable String id) {
        return crewService.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createCrew(HttpServletRequest request, @Valid @RequestBody CrewDto crew) {
        String organizationId = getOrganizationId(request);
        crew.setOrganizationId(organizationId);
        Crew crewNew = crewService.saveCrew(crew);
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", Constants.CREW_ALREADY_EXISTS);
        return crewNew != null ? ResponseEntity.status(HttpStatus.CREATED).body(crewNew)
                : ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @PutMapping("/")
    public CrewDto updatePersonnel(HttpServletRequest request, @RequestBody CrewDto crew) {
        String organizationId = getOrganizationId(request);
        crew.setOrganizationId(organizationId);
        Boolean crewStatus = crewService.updateCrew(crew);
        Map<String, String> message = new HashMap<>();
        return crewService.findById(crew.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCrew(@PathVariable String id) {
        crewService.deleteCrew(id);
    }

    @GetMapping("/getByFleet")
    public CrewDisplayObject getAllCrewByFleet(HttpServletRequest request,
                                                    @RequestParam(name = "offSet", defaultValue = "0", required = false) int offSet,
                                                    @RequestParam(name = "limit", defaultValue = "10", required = false) int limit,
                                                    @RequestParam("fleetName") String fleetName) {
        String organizationId = getOrganizationId(request);
        return crewService.findAllByFleet(organizationId, fleetName, offSet, limit);
    }
}
