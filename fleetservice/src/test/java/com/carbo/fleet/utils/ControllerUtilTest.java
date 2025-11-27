
package com.carbo.fleet.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.mock.web.MockHttpServletRequest;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ControllerUtilTest {

    @Mock
    private OAuth2Authentication oauth2Authentication;

    @InjectMocks
    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    public void shouldReturnOrganizationIdWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        when(oauth2Authentication.getUserAuthentication()).thenReturn(mockUserAuthentication(details));
        request.setUserPrincipal(oauth2Authentication);

        String organizationId = ControllerUtil.getOrganizationId(request);

        assertEquals("org123", organizationId);
    }

    @Test
    public void shouldReturnOrganizationTypeWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationType", "typeX");
        when(oauth2Authentication.getUserAuthentication()).thenReturn(mockUserAuthentication(details));
        request.setUserPrincipal(oauth2Authentication);

        String organizationType = ControllerUtil.getOrganizationType(request);

        assertEquals("typeX", organizationType);
    }

    @Test
    public void shouldReturnUserNameWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("userName", "john_doe");
        when(oauth2Authentication.getUserAuthentication()).thenReturn(mockUserAuthentication(details));
        request.setUserPrincipal(oauth2Authentication);

        String userName = ControllerUtil.getUserName(request);

        assertEquals("john_doe", userName);
    }

    private UserAuthentication mockUserAuthentication(Map<String, Object> details) {
        UserAuthentication userAuth = mock(UserAuthentication.class);
        when(userAuth.getDetails()).thenReturn(details);
        return userAuth;
    }

    private interface UserAuthentication {
        Object getDetails();
    }
}
