
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllerUtilTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private OAuth2Authentication oauth2Authentication;

    @Test
    public void shouldReturnOrganizationIdWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        when(request.getUserPrincipal()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getUserAuthentication()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getDetails()).thenReturn(details);

        String organizationId = ControllerUtil.getOrganizationId(request);

        assertEquals("org123", organizationId);
        verify(request).getUserPrincipal();
        verify(oauth2Authentication).getUserAuthentication();
        verify(oauth2Authentication).getDetails();
    }

    @Test
    public void shouldReturnOrganizationTypeWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationType", "typeA");
        when(request.getUserPrincipal()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getUserAuthentication()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getDetails()).thenReturn(details);

        String organizationType = ControllerUtil.getOrganizationType(request);

        assertEquals("typeA", organizationType);
        verify(request).getUserPrincipal();
        verify(oauth2Authentication).getUserAuthentication();
        verify(oauth2Authentication).getDetails();
    }

    @Test
    public void shouldReturnUserNameWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("userName", "john.doe");
        when(request.getUserPrincipal()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getUserAuthentication()).thenReturn(oauth2Authentication);
        when(oauth2Authentication.getDetails()).thenReturn(details);

        String userName = ControllerUtil.getUserName(request);

        assertEquals("john.doe", userName);
        verify(request).getUserPrincipal();
        verify(oauth2Authentication).getUserAuthentication();
        verify(oauth2Authentication).getDetails();
    }
}
