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

// ===== Imported from: com.carbo.fleet.model.SyncRequest =====
package com.carbo.fleet.model;

import java.util.List;
import java.util.Set;

public class SyncRequest {
    private List<Fleet> update;

    private Set<String> remove;

    private Set<String> get;

    public List<Fleet> getUpdate() {
        return this.update;
    }

    public void setUpdate(List<Fleet> update) {
        this.update = update;
    }

    public Set<String> getRemove() {
        return this.remove;
    }

    public void setRemove(Set<String> remove) {
        this.remove = remove;
    }

    public Set<String> getGet() {
        return this.get;
    }

    public void setGet(Set<String> get) {
        this.get = get;
    }

}

// ===== Imported from: com.carbo.fleet.model.SyncResponse =====
package com.carbo.fleet.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SyncResponse {
    private List<Fleet> get;

    private Map<String, Long> updated;

    private Set<String> removed;

    public Map<String, Long> getUpdated() {
        return this.updated;
    }

    public void setUpdated(Map<String, Long> updated) {
        this.updated = updated;
    }

    public List<Fleet> getGet() {
        return this.get;
    }

    public void setGet(List<Fleet> get) {
        this.get = get;
    }

    public Set<String> getRemoved() {
        return this.removed;
    }

    public void setRemoved(Set<String> removed) {
        this.removed = removed;
    }
}

// ===== Imported from: com.carbo.fleet.services.FleetService =====
package com.carbo.fleet.services;

import com.carbo.fleet.events.model.FleetDetails;
import com.carbo.fleet.model.Error;
import com.carbo.fleet.model.Job;
import com.carbo.fleet.model.OnSiteEquipment;
import com.carbo.fleet.model.PumpTypeEnum;
import com.carbo.fleet.repository.FleetMongoDbRepository;
import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.repository.JobMongoDbRepository;
import com.carbo.fleet.repository.OnSiteEquipmentMongoDbRepository;
import com.carbo.fleet.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.carbo.fleet.utils.ControllerUtil.getOrganizationId;

@Service
public class FleetService {
    private final FleetMongoDbRepository fleetRepository;

    private final OnSiteEquipmentMongoDbRepository onSiteEquipmentMongoDbRepository;

    private final JobMongoDbRepository jobMongoDbRepository;

    @Autowired
    public FleetService(FleetMongoDbRepository fleetRepository, OnSiteEquipmentMongoDbRepository onSiteEquipmentMongoDbRepository,
                        JobMongoDbRepository jobMongoDbRepository) {
        this.fleetRepository = fleetRepository;
        this.onSiteEquipmentMongoDbRepository = onSiteEquipmentMongoDbRepository;
        this.jobMongoDbRepository = jobMongoDbRepository;
    }

    public List<Fleet> getAll() {
        return fleetRepository.findAll();
    }

    public List<Fleet> getByOrganizationId(String organizationId) {
        return fleetRepository.findByOrganizationId(organizationId);
    }

    public Optional<Fleet> getFleet(String fleetId) {
        return fleetRepository.findById(fleetId);
    }

    public Fleet saveFleet(Fleet fleet) {
        return fleetRepository.save(fleet);
    }

    public void updateFleet(Fleet fleet) {
        fleetRepository.save(fleet);
    }

    public void deleteFleet(String fleetId) {
        fleetRepository.deleteById(fleetId);
    }

    public Optional<Fleet> findDistinctByOrganizationIdAndName(String organizationId, String name) {
        return fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name);
    }


//    String organizationId = getOrganizationId(request);
//    // Create an aggregation pipeline to retrieve the proposal and related information.
//    Aggregation aggregation =
//            Aggregation.match(Criteria.where("sharedWithOrganizationId").is(organizationId)),
//            Aggregation.java_springboot-master().and(ConvertOperators.ToString.toString("$_id")).as(PROPOSAL_ID_STRING).and("version").as("version")
//                       .and("role").as("role").and("awaiting").as("awaiting").and("proposalAction").as("proposalAction").and("proposalStatus")
//                       .as("proposalStatus").and("showProposalName").as("showProposalName").and("opportunity").as("opportunity")
//                       .and("companyId").as("companyId").and("priceBookId").as("priceBookId").and("priceBy").as("priceBy")
//                       .and("wellEquipments").as("wellEquipments").and(ORGANIZATION_ID).as(ORGANIZATION_ID).and("auditDetails")
//                       .as("auditDetails"),
//            LookupOperation.newLookup().from("proposal-basic-information-v2").localField(PROPOSAL_ID_STRING).foreignField(PROPOSAL_ID)
//                           .as("basicInformation"),
//            LookupOperation.newLookup().from("proposal-design-v2").localField(PROPOSAL_ID_STRING).foreignField(PROPOSAL_ID).as("design"),
//            LookupOperation.newLookup().from("proposal-fluids-v2").localField(PROPOSAL_ID_STRING).foreignField(PROPOSAL_ID).as("fluids"),
//            LookupOperation.newLookup().from("proposal-proppants-v2").localField(PROPOSAL_ID_STRING).foreignField(PROPOSAL_ID)
//                           .as("proppants"),
//            LookupOperation.newLookup().from("proposal-well-v2").localField(PROPOSAL_ID_STRING).foreignField(PROPOSAL_ID).as("wells"));
//    List<Proposal> proposal = null;
//    // Check if the provided proposal ID is not empty.
//            if (!StringUtils.isEmpty(id)) {
//        // Execute the aggregation and retrieve the results.
//        proposal = mongoTemplate.aggregate(aggregation, "proposal-v2", Proposal.class).getMappedResults();
//    }
//

    public ResponseEntity getFleetData(HttpServletRequest request) {
        try {
            String organizationId = getOrganizationId(request);
            Map<String,Map<String, Map<PumpTypeEnum, Integer>>> fleetDataforAllOrganizations = new HashMap<>();
            List<Job> jobList = jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress");

            Set<String> filterFleetName = jobList.stream()
                    .map(Job::getFleet)
                    .collect(Collectors.toSet());

            Set<String> organizationIds = jobList.stream().map(Job::getOrganizationId).collect(Collectors.toSet());

            Map<String, FleetDetails> fleets = new HashMap<>();
            for(Job job : jobList){
                FleetDetails fleetDataFromJobs = new FleetDetails();
                fleetDataFromJobs.setName(job.getFleet());
                fleetDataFromJobs.setOrganizationId(job.getOrganizationId());
                fleets.put(job.getId(),fleetDataFromJobs);
            }

            List<Fleet> fleetLists = fleetRepository.findByOrganizationIdInAndNameIn(organizationIds, filterFleetName);

            List<Fleet> filteredFleetLists = new ArrayList<>();

            for (Fleet fleet : fleetLists) {
                // Check all combinations of fleet name and organization ID
                for (FleetDetails allFleetData : fleets.values()) {
                    if (allFleetData.getName().equals(fleet.getName())
                            && allFleetData.getOrganizationId().contains(fleet.getOrganizationId())) {
                        filteredFleetLists.add(fleet);
                        break;  // Break once a matching combination is found for the current fleet
                    }
                }
            }

            Set<String> fleetIds = filteredFleetLists.stream().map(Fleet::getId).collect(Collectors.toSet());

            List<OnSiteEquipment> data = onSiteEquipmentMongoDbRepository.findByFleetIdIn(fleetIds);
            for(String ogrId: organizationIds) {
                // Iterate through each fleet

                Map<String, Map<PumpTypeEnum, Integer>> fleetData = new HashMap<>();

                for (Fleet fleet : fleetLists) {
                    if(fleet.getOrganizationId().equals(ogrId)) {
                        String fleetId = !ObjectUtils.isEmpty(fleet.getId()) ? fleet.getId() : "";
                        String fleetName = fleet.getName();

                        // Initialize counts for the three categories
                        int dualPumpCount = 0;
                        int ePumpCount = 0;
                        int dieselPump = 0;

                        // Iterate through the onSiteEquipment entries
                        for (OnSiteEquipment onSiteEquipment : data) {
                            if (onSiteEquipment.getFleetId().equalsIgnoreCase(fleetId)) {
                                if (onSiteEquipment.getType().equalsIgnoreCase("pumps") && onSiteEquipment.getDuelFuel()) {
                                    dualPumpCount++;
                                }
                                if (onSiteEquipment.getType().equalsIgnoreCase("pumps") && !onSiteEquipment.getDuelFuel()) {
                                    dieselPump++;
                                }
                                if (onSiteEquipment.getType().equalsIgnoreCase("epumps")) {
                                    ePumpCount++;
                                }
                            }
                        }

                        if (fleetData.containsKey(fleetName)) {
                            Map<PumpTypeEnum, Integer> existingFleetCounts = fleetData.get(fleetName);
                            existingFleetCounts.put(PumpTypeEnum.DUAL, existingFleetCounts.get(PumpTypeEnum.DUAL) + dualPumpCount);
                            existingFleetCounts.put(PumpTypeEnum.ELECTRIC, existingFleetCounts.get(PumpTypeEnum.ELECTRIC) + ePumpCount);
                            existingFleetCounts.put(PumpTypeEnum.DIESEL, existingFleetCounts.get(PumpTypeEnum.DIESEL) + dieselPump);
                        } else {
                            Map<PumpTypeEnum, Integer> fleetCounts = new HashMap<>();
                            fleetCounts.put(PumpTypeEnum.DUAL, dualPumpCount);
                            fleetCounts.put(PumpTypeEnum.ELECTRIC, ePumpCount);
                            fleetCounts.put(PumpTypeEnum.DIESEL, dieselPump);

                            // Add the fleet data to the main fleetData map with fleetName as the key
                            fleetData.put(fleetName, fleetCounts);
                        }
                    }
                }
                fleetDataforAllOrganizations.put(ogrId,fleetData);
            }
            return ResponseEntity.ok(fleetDataforAllOrganizations);
        } catch (Exception e) {
            Error error = Error.builder()
                    .errorCode(Constants.UNABLE_TO_FETCH_DATA_CODE)
                    .errorMessage(Constants.UNABLE_TO_FETCH_DATA_MESSAGE).build();
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
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

// ===== Current file: src\main\java\com\carbo\fleet\controllers\SyncController.java =====
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.SyncRequest;
import com.carbo.fleet.model.SyncResponse;
import com.carbo.fleet.services.FleetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

import static com.carbo.fleet.utils.ControllerUtil.getOrganizationId;

@RestController
@RequestMapping(value = "v1/sync")
public class SyncController {

    private final FleetService fleetService;
    private static final Logger logger = LoggerFactory.getLogger(SyncController.class);

    @Autowired
    public SyncController(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public Map<String, Long> view(HttpServletRequest request) {
        Map<String, Long> result = new HashMap<>();
        String organizationId = getOrganizationId(request);
        List<Fleet> all = fleetService.getByOrganizationId(organizationId);
        all.forEach(each -> result.put(each.getId(), each.getTs()));
        return result;
        // return result
    }

    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    public SyncResponse sync(HttpServletRequest request, @RequestBody SyncRequest sync) {
        SyncResponse response = new SyncResponse();
        if (sync.getRemove() != null && !sync.getRemove().isEmpty()) {
            Set<String> removed = new HashSet<>();
            for (String id : sync.getRemove()) {
                this.fleetService.deleteFleet(id);
                removed.add(id);
            }
            response.setRemoved(removed);
        }

        List<Fleet> gets = new ArrayList<>();

        String organizationId = getOrganizationId(request);
        if (sync.getUpdate() != null && !sync.getUpdate().isEmpty()) {
            Map<String, Long> updated = new HashMap<>();
            for (Fleet fleet : sync.getUpdate()) {
                if (fleet.getOrganizationId() != null) {
                    if (!fleet.getOrganizationId().equals(organizationId)) {
                        continue;
                    }
                }
                else {
                    fleet.setOrganizationId(organizationId);
                }
                if (fleet.getTs() > 0) {
                    // update
                    Fleet dbFleet = this.fleetService.getFleet(fleet.getId()).get();
                    if (dbFleet.getTs() > fleet.getTs()) {
                        // db object is newer than the version sent from the client
                        gets.add(dbFleet);
                    }
                    else {
                        this.fleetService.updateFleet(fleet);
                        updated.put(fleet.getId(), fleet.getTs());
                    }
                }
                else {
                    // insert
                    fleet.setTs(System.currentTimeMillis());
                    Fleet saved = this.fleetService.saveFleet(fleet);
                    updated.put(saved.getId(), saved.getCreated());
                }
            }
            response.setUpdated(updated);
        }

        if (sync.getGet() != null && !sync.getGet().isEmpty()) {
            for (String id : sync.getGet()) {
                Optional<Fleet> obj = this.fleetService.getFleet(id);
                if (obj.isPresent()) {
                    gets.add(obj.get());
                }
            }

            if (!gets.isEmpty()) {
                response.setGet(gets);
            }
        }

        return response;
    }
}

