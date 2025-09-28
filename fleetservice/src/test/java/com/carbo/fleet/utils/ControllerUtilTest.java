
package com.carbo.fleet.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ControllerUtilTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private OAuth2Authentication oAuth2Authentication;

    @Test
    void shouldReturnOrganizationIdWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        
        Mockito.when(request.getUserPrincipal()).thenReturn(oAuth2Authentication);
        Mockito.when(oAuth2Authentication.getUserAuthentication()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(oAuth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);

        String organizationId = ControllerUtil.getOrganizationId(request);
        
        assertEquals("org123", organizationId);
    }

    @Test
    void shouldReturnOrganizationTypeWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationType", "typeA");

        Mockito.when(request.getUserPrincipal()).thenReturn(oAuth2Authentication);
        Mockito.when(oAuth2Authentication.getUserAuthentication()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(oAuth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);

        String organizationType = ControllerUtil.getOrganizationType(request);
        
        assertEquals("typeA", organizationType);
    }

    @Test
    void shouldReturnUserNameWhenRequestIsValid() {
        Map<String, Object> details = new HashMap<>();
        details.put("userName", "user1");

        Mockito.when(request.getUserPrincipal()).thenReturn(oAuth2Authentication);
        Mockito.when(oAuth2Authentication.getUserAuthentication()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(oAuth2Authentication.getUserAuthentication().getDetails()).thenReturn(details);

        String userName = ControllerUtil.getUserName(request);
        
        assertEquals("user1", userName);
    }
}
