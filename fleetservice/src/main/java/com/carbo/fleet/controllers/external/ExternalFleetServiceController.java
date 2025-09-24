package com.carbo.fleet.controllers.external;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.User;
import com.carbo.fleet.model.ServiceAccount;
import com.carbo.fleet.services.FleetService;
import com.carbo.fleet.services.UserService;
import com.carbo.fleet.services.ServiceAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.carbo.fleet.utils.ControllerUtil.getOrganizationType;
import static com.carbo.fleet.utils.ControllerUtil.getUserName;

@RestController
@RequestMapping(value = "v1/fleets/external")
public class ExternalFleetServiceController {
    private static final Logger logger = LoggerFactory.getLogger(ExternalFleetServiceController.class);

    private final FleetService fleetService;
    private final UserService userService;
    private final ServiceAccountService serviceAccountService;

    @Autowired
    public ExternalFleetServiceController(FleetService fleetService, UserService userService, ServiceAccountService serviceAccountService) {
        this.fleetService = fleetService;
        this.userService = userService;
        this.serviceAccountService = serviceAccountService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Fleet> getFleets(HttpServletRequest request) {
        String organizationType = getOrganizationType(request);
        List<Fleet> result = new ArrayList<>();
        if (organizationType.contentEquals("PARTNER")) {
            String userName = getUserName(request);
            Optional<User> userInfo = userService.getUserByUserName(userName);
            String userId = userInfo.get().getId();
            Optional<ServiceAccount> serviceAccount = serviceAccountService.get(userId);
            if (serviceAccount.isPresent()) {
                List<String> organizationIds = serviceAccount.get().getOrganizationIds();
                for (String id : organizationIds) {
                    List<Fleet> temp = fleetService.getByOrganizationId(id);
                    for (Fleet fleet : temp) {
                        result.add(fleet);
                    }
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/{fleetId}", method = RequestMethod.GET)
    public Fleet getFleet(HttpServletRequest request, @PathVariable("fleetId") String fleetId) {
        String organizationType = getOrganizationType(request);
        Fleet result = null;
        if (organizationType.contentEquals("PARTNER")) {
            String userName = getUserName(request);
            Optional<User> userInfo = userService.getUserByUserName(userName);
            String userId = userInfo.get().getId();
            Optional<ServiceAccount> serviceAccount = serviceAccountService.get(userId);
            if (serviceAccount.isPresent()) {
                List<String> organizationIds = serviceAccount.get().getOrganizationIds();
                result = fleetService.getFleet(fleetId).get();
                if(organizationIds.contains(result.getOrganizationId())){
                    return result;
                } else {
                    throw new AccessDeniedException("This account do not have access to see ");
                }
            }
        }
        return result;
    }
}
