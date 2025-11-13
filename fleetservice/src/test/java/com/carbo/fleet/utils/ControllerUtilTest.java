
package com.carbo.fleet.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerUtilTest {

    @Mock
    private OAuth2Authentication oAuth2Authentication;

    @Test
    void shouldReturnOrganizationIdWhenRequestIsValid() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        when(oAuth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);
        Principal principal = oAuth2Authentication;
        request.setUserPrincipal(principal);
        
        String organizationId = ControllerUtil.getOrganizationId(request);
        
        assertEquals("org123", organizationId);
    }

    @Test
    void shouldReturnOrganizationTypeWhenRequestIsValid() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> details = new HashMap<>();
        details.put("organizationType", "typeA");
        when(oAuth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);
        Principal principal = oAuth2Authentication;
        request.setUserPrincipal(principal);
        
        String organizationType = ControllerUtil.getOrganizationType(request);
        
        assertEquals("typeA", organizationType);
    }

    @Test
    void shouldReturnUserNameWhenRequestIsValid() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> details = new HashMap<>();
        details.put("userName", "john.doe");
        when(oAuth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);
        Principal principal = oAuth2Authentication;
        request.setUserPrincipal(principal);
        
        String userName = ControllerUtil.getUserName(request);
        
        assertEquals("john.doe", userName);
    }
}
