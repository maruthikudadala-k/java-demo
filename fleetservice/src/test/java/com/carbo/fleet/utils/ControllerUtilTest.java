
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
    private OAuth2Authentication oauth2Authentication;

    @Test
    public void shouldReturnOrganizationIdWhenRequestIsValid() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        details.put("organizationType", "typeA");
        details.put("userName", "userX");

        when(oauth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);
        Principal principal = () -> "user"; // Mocking the principal
        when(oauth2Authentication.getUserAuthentication()).thenReturn(oauth2Authentication);
        request.setUserPrincipal(principal);

        String organizationId = ControllerUtil.getOrganizationId(request);
        assertEquals("org123", organizationId);
    }

    @Test
    public void shouldReturnOrganizationTypeWhenRequestIsValid() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        details.put("organizationType", "typeA");
        details.put("userName", "userX");

        when(oauth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);
        Principal principal = () -> "user"; // Mocking the principal
        when(oauth2Authentication.getUserAuthentication()).thenReturn(oauth2Authentication);
        request.setUserPrincipal(principal);

        String organizationType = ControllerUtil.getOrganizationType(request);
        assertEquals("typeA", organizationType);
    }

    @Test
    public void shouldReturnUserNameWhenRequestIsValid() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        details.put("organizationType", "typeA");
        details.put("userName", "userX");

        when(oauth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);
        Principal principal = () -> "user"; // Mocking the principal
        when(oauth2Authentication.getUserAuthentication()).thenReturn(oauth2Authentication);
        request.setUserPrincipal(principal);

        String userName = ControllerUtil.getUserName(request);
        assertEquals("userX", userName);
    }
}
