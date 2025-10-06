
package com.carbo.fleet.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.mock.web.MockHttpServletRequest;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllerUtilTest {

    @Mock
    private OAuth2Authentication oauth2Authentication;

    @Mock
    private Principal principal;

    @InjectMocks
    private MockHttpServletRequest request;

    @Test
    public void shouldReturnOrganizationIdWhenValidRequest() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "12345");
        when(oauth2Authentication.getUserAuthentication()).thenReturn(principal);
        when(principal.getDetails()).thenReturn(details);
        when(request.getUserPrincipal()).thenReturn(oauth2Authentication);

        String organizationId = ControllerUtil.getOrganizationId(request);

        assertEquals("12345", organizationId);
    }

    @Test
    public void shouldReturnOrganizationTypeWhenValidRequest() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationType", "typeA");
        when(oauth2Authentication.getUserAuthentication()).thenReturn(principal);
        when(principal.getDetails()).thenReturn(details);
        when(request.getUserPrincipal()).thenReturn(oauth2Authentication);

        String organizationType = ControllerUtil.getOrganizationType(request);

        assertEquals("typeA", organizationType);
    }

    @Test
    public void shouldReturnUserNameWhenValidRequest() {
        Map<String, Object> details = new HashMap<>();
        details.put("userName", "john.doe");
        when(oauth2Authentication.getUserAuthentication()).thenReturn(principal);
        when(principal.getDetails()).thenReturn(details);
        when(request.getUserPrincipal()).thenReturn(oauth2Authentication);

        String userName = ControllerUtil.getUserName(request);

        assertEquals("john.doe", userName);
    }
}
