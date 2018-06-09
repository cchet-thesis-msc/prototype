package com.gepardec.esb.prototype.services.app.rest.client.filter;

import com.gepardec.esb.prototype.services.app.annotation.OAuthToken;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
@ApplicationScoped
public class AppendOAuthFilter implements ClientRequestFilter {

    @Inject
    @OAuthToken
    private Instance<String> tokenInstance;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenInstance.get());
    }
}
