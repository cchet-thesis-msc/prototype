package com.gepardec.esb.prototype.services.app.rest.client.filter;

import com.gepardec.esb.prototype.services.app.annotation.OAuthToken;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
public class AppendOAuthFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        final String token = BeanProvider.getDependent(String.class, new AnnotationLiteral<OAuthToken>() {}).get();
        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
