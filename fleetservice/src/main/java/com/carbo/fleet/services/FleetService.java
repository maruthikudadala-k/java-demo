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


