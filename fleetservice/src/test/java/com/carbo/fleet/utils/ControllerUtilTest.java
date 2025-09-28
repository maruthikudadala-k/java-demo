
package com.carbo.fleet.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.mock.web.MockHttpServletRequest;

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
    public void shouldReturnOrganizationIdWhenRequestIsProvided() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal instanceof OAuth2Authentication).thenReturn(true);
        when(oAuth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);
        
        String organizationId = ControllerUtil.getOrganizationId(request);
        
        assertEquals("org123", organizationId);
    }

    @Test
    public void shouldReturnOrganizationTypeWhenRequestIsProvided() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationType", "typeA");
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal instanceof OAuth2Authentication).thenReturn(true);
        when(oAuth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);
        
        String organizationType = ControllerUtil.getOrganizationType(request);
        
        assertEquals("typeA", organizationType);
    }

    @Test
    public void shouldReturnUserNameWhenRequestIsProvided() {
        Map<String, Object> details = new HashMap<>();
        details.put("userName", "john.doe");
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal instanceof OAuth2Authentication).thenReturn(true);
        when(oAuth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);
        
        String userName = ControllerUtil.getUserName(request);
        
        assertEquals("john.doe", userName);
    }
}
