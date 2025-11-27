
package com.carbo.fleet.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ControllerUtilTest {

    @Mock
    private OAuth2Authentication oauth2Authentication;

    @Mock
    private Principal principal;

    @InjectMocks
    private MockHttpServletRequest request;

    @Test
    public void shouldReturnOrganizationIdWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        when(principal.getUserAuthentication()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getDetails()).thenReturn(details);
        request.setUserPrincipal(principal);

        String organizationId = ControllerUtil.getOrganizationId(request);

        assertEquals("org123", organizationId);
    }

    @Test
    public void shouldReturnOrganizationTypeWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationType", "typeA");
        when(principal.getUserAuthentication()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getDetails()).thenReturn(details);
        request.setUserPrincipal(principal);

        String organizationType = ControllerUtil.getOrganizationType(request);

        assertEquals("typeA", organizationType);
    }

    @Test
    public void shouldReturnUserNameWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("userName", "user1");
        when(principal.getUserAuthentication()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getDetails()).thenReturn(details);
        request.setUserPrincipal(principal);

        String userName = ControllerUtil.getUserName(request);

        assertEquals("user1", userName);
    }
}
