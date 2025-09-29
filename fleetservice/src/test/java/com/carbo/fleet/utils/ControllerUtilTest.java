
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
    private OAuth2Authentication oAuth2Authentication;

    @Mock
    private Principal principal;

    @InjectMocks
    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    public void shouldReturnOrganizationIdWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org-123");
        when(oAuth2Authentication.getUserAuthentication()).thenReturn(principal);
        when(principal.getDetails()).thenReturn(details);
        request.setUserPrincipal(oAuth2Authentication);
        
        String organizationId = ControllerUtil.getOrganizationId(request);
        
        assertEquals("org-123", organizationId);
    }

    @Test
    public void shouldReturnOrganizationTypeWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationType", "type-abc");
        when(oAuth2Authentication.getUserAuthentication()).thenReturn(principal);
        when(principal.getDetails()).thenReturn(details);
        request.setUserPrincipal(oAuth2Authentication);
        
        String organizationType = ControllerUtil.getOrganizationType(request);
        
        assertEquals("type-abc", organizationType);
    }

    @Test
    public void shouldReturnUserNameWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("userName", "user123");
        when(oAuth2Authentication.getUserAuthentication()).thenReturn(principal);
        when(principal.getDetails()).thenReturn(details);
        request.setUserPrincipal(oAuth2Authentication);
        
        String userName = ControllerUtil.getUserName(request);
        
        assertEquals("user123", userName);
    }
}
