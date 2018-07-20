package com.gepardec.esb.prototype.services.app.rest.client.filter;

import com.gepardec.esb.prototype.services.app.configuration.KeycloakConfiguration;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.util.AnnotationLiteral;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

/**
 * This class retrieves an OAuth token and appends it on the
 * Authentication header.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
public class AppendOAuthFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        final KeycloakConfiguration.KeycloakAuth keycloakAuth = BeanProvider.getContextualReference(KeycloakConfiguration.KeycloakAuth.class);
        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + keycloakAuth.getToken());
    }
}
