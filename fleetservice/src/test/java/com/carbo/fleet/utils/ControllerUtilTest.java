
package com.carbo.fleet.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ControllerUtilTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private OAuth2Authentication oAuth2Authentication;

    @Mock
    private Principal principal;

    @Test
    public void shouldReturnOrganizationIdWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org-123");

        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal instanceof OAuth2Authentication).thenReturn(true);
        when((OAuth2Authentication) principal).getUserAuthentication().getDetails().thenReturn(details);

        String organizationId = ControllerUtil.getOrganizationId(request);
        assertEquals("org-123", organizationId);
    }

    @Test
    public void shouldReturnOrganizationTypeWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationType", "type-abc");

        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal instanceof OAuth2Authentication).thenReturn(true);
        when((OAuth2Authentication) principal).getUserAuthentication().getDetails().thenReturn(details);

        String organizationType = ControllerUtil.getOrganizationType(request);
        assertEquals("type-abc", organizationType);
    }

    @Test
    public void shouldReturnUserNameWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("userName", "john.doe");

        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal instanceof OAuth2Authentication).thenReturn(true);
        when((OAuth2Authentication) principal).getUserAuthentication().getDetails().thenReturn(details);

        String userName = ControllerUtil.getUserName(request);
        assertEquals("john.doe", userName);
    }
}
