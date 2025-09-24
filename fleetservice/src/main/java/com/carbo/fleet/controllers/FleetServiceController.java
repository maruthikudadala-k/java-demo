package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Job;
import com.carbo.fleet.model.OnSiteEquipment;
import com.carbo.fleet.services.FleetService;
import com.carbo.fleet.model.Fleet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.carbo.fleet.utils.ControllerUtil.getOrganizationId;
import static com.carbo.fleet.utils.ControllerUtil.getOrganizationType;

@RestController
@RequestMapping(value = "v1/fleets")
public class FleetServiceController {
    private static final Logger logger = LoggerFactory.getLogger(FleetServiceController.class);

    private final FleetService fleetService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public FleetServiceController(FleetService fleetService, MongoTemplate mongoTemplate) {
        this.fleetService = fleetService;
        this.mongoTemplate = mongoTemplate;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Fleet> getFleets(HttpServletRequest request) {
        String organizationId = getOrganizationId(request);
        String organizationType = getOrganizationType(request);
        List<Fleet> all = new ArrayList<>();
        if(organizationType.contentEquals("OPERATOR")){
            Query query = new Query();
            query.addCriteria(Criteria.where("sharedWithOrganizationId").is(organizationId));
            List<Job> jobs = mongoTemplate.find(query, Job.class);
            Set<String> fleets = jobs.stream().map(Job::getFleet).collect(Collectors.toSet());
            Set<String> organizationIds = jobs.stream().map(Job::getOrganizationId).collect(Collectors.toSet());
            
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("name").in(fleets));
            query1.addCriteria(Criteria.where("organizationId").in(organizationIds));
            all = mongoTemplate.find(query1, Fleet.class);
        }else {
            all = fleetService.getByOrganizationId(organizationId);
        }
        return all;
    }

    @RequestMapping(value = "/{fleetId}", method = RequestMethod.GET)
    public Fleet getFleet(@PathVariable("fleetId") String fleetId) {
        logger.debug("Looking up data for fleet {}", fleetId);

        Fleet fleet = fleetService.getFleet(fleetId).get();
        return fleet;
    }

    @RequestMapping(value = "/{fleetId}", method = RequestMethod.PUT)
    public void updateFleet(@PathVariable("fleetId") String fleetId, @RequestBody Fleet fleet) {
        fleetService.updateFleet(fleet);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void saveFleet(@RequestBody Fleet fleet) {
        fleetService.saveFleet(fleet);
    }

    @RequestMapping(value = "/{fleetId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFleet(@PathVariable("fleetId") String fleetId) {
        fleetService.deleteFleet(fleetId);
    }

    @RequestMapping(value = "/findDistinctByOrganizationIdAndName", method = RequestMethod.GET)
    public Optional<Fleet> findDistinctByOrganizationIdAndName(HttpServletRequest request, String name) {
        String organizationId = getOrganizationId(request);
        return fleetService.findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @GetMapping("/data")
    public ResponseEntity getFleetData(HttpServletRequest request) {
        return fleetService.getFleetData(request);
    }

    @RequestMapping(value = "/calendarFleets", method = RequestMethod.GET)
    public List<Fleet> getFleetsForCalendar(HttpServletRequest request) {
        String organizationId = getOrganizationId(request);
        String organizationType = getOrganizationType(request);
        List<Fleet> all = new ArrayList<>();
        if(organizationType.contentEquals("OPERATOR")){
            Query query = new Query();
            query.addCriteria(Criteria.where("sharedWithOrganizationId").is(organizationId));
            List<Job> jobList = mongoTemplate.find(query, Job.class);
            List<Job> jobs = new ArrayList<>();
            if(!ObjectUtils.isEmpty(jobList)){
                for (Job job : jobList){
                    if(!ObjectUtils.isEmpty(job.getProposalId())){
                        jobs.add(job);
                    }
                }
            }
            Set<String> fleets = jobs.stream().map(Job::getFleet).collect(Collectors.toSet());
            Set<String> organizationIds = jobs.stream().map(Job::getOrganizationId).collect(Collectors.toSet());

            Query query1 = new Query();
            query1.addCriteria(Criteria.where("name").in(fleets));
            query1.addCriteria(Criteria.where("organizationId").in(organizationIds));
            all = mongoTemplate.find(query1, Fleet.class);
        }else {
            all = fleetService.getByOrganizationId(organizationId);
        }
        return all;
    }
}








