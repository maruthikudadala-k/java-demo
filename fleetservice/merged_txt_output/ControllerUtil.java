// ===== Current file: src\main\java\com\carbo\fleet\utils\ControllerUtil.java =====
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

