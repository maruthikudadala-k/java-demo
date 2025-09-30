
package com.carbo.fleet.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ControllerUtilTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private OAuth2Authentication oauth2Authentication;

    private MockHttpServletRequest mockRequest = new MockHttpServletRequest();

    @Test
    public void shouldReturnOrganizationIdWhenRequestIsProvided() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        details.put("organizationType", "typeA");
        details.put("userName", "user1");

        doReturn(oauth2Authentication).when(request).getUserPrincipal();
        doReturn(details).when(oauth2Authentication).getUserAuthentication().getDetails();

        String organizationId = ControllerUtil.getOrganizationId(request);
        assertEquals("org123", organizationId);
    }

    @Test
    public void shouldReturnOrganizationTypeWhenRequestIsProvided() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        details.put("organizationType", "typeA");
        details.put("userName", "user1");

        doReturn(oauth2Authentication).when(request).getUserPrincipal();
        doReturn(details).when(oauth2Authentication).getUserAuthentication().getDetails();

        String organizationType = ControllerUtil.getOrganizationType(request);
        assertEquals("typeA", organizationType);
    }

    @Test
    public void shouldReturnUserNameWhenRequestIsProvided() {
        Map<String, Object> details = new HashMap<>();
        details.put("organizationId", "org123");
        details.put("organizationType", "typeA");
        details.put("userName", "user1");

        doReturn(oauth2Authentication).when(request).getUserPrincipal();
        doReturn(details).when(oauth2Authentication).getUserAuthentication().getDetails();

        String userName = ControllerUtil.getUserName(request);
        assertEquals("user1", userName);
    }
}
