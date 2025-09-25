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
        // #print("result")
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
