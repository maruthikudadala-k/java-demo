
package com.carbo.fleet.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllerUtilTest {
    
    @Mock
    private HttpServletRequest request;

    @Mock
    private OAuth2Authentication oauth2Authentication;

    @Test
    public void shouldReturnOrganizationIdWhenPrincipalIsPresent() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        details.put("organizationType", "typeA");
        details.put("userName", "userA");

        when(request.getUserPrincipal()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getUserAuthentication()).thenReturn(mock(Principal.class));
        when(oauth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);

        String organizationId = ControllerUtil.getOrganizationId(request);

        assertEquals("org123", organizationId);
        verify(request).getUserPrincipal();
    }

    @Test
    public void shouldReturnOrganizationTypeWhenPrincipalIsPresent() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        details.put("organizationType", "typeA");
        details.put("userName", "userA");

        when(request.getUserPrincipal()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getUserAuthentication()).thenReturn(mock(Principal.class));
        when(oauth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);

        String organizationType = ControllerUtil.getOrganizationType(request);

        assertEquals("typeA", organizationType);
        verify(request).getUserPrincipal();
    }

    @Test
    public void shouldReturnUserNameWhenPrincipalIsPresent() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        details.put("organizationType", "typeA");
        details.put("userName", "userA");

        when(request.getUserPrincipal()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getUserAuthentication()).thenReturn(mock(Principal.class));
        when(oauth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);

        String userName = ControllerUtil.getUserName(request);

        assertEquals("userA", userName);
        verify(request).getUserPrincipal();
    }
}
