package com.gepardec.esb.prototype.integration.services.db.configuration;

import com.gepardec.esb.prototype.integration.services.db.annotation.Logging;
import com.gepardec.esb.prototype.integration.services.db.annotation.OAuthToken;
import com.google.api.client.auth.oauth2.ClientCredentialsTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.metrics.annotation.Counted;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;

/**
 * This class produces all Keycloak related resources.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
@ApplicationScoped
public class KeycloakConfiguration {

    @Inject
    @ConfigProperty(name = "keycloak.token-url")
    private String keycloakTokenUrl;
    @Inject
    @ConfigProperty(name = "keycloak.client.id")
    private String keycloakClientId;
    @Inject
    @ConfigProperty(name = "keycloak.client.secret")
    private String keycloakClientSecret;

    private ClientCredentialsTokenRequest tokenRequest;

    @PostConstruct
    public void postConstruct() {
        tokenRequest = new ClientCredentialsTokenRequest(new NetHttpTransport(),
                                                         new JacksonFactory(),
                                                         new GenericUrl(keycloakTokenUrl));
        tokenRequest.setClientAuthentication(new ClientParametersAuthentication(keycloakClientId, keycloakClientSecret));
    }

    @Produces
    @OAuthToken
    @Dependent
    @Counted(name = "retrieved-oauth-tokens", monotonic = true)
    @Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_SECURITY, skipResult = true)
    @Retry(delay = 100L, maxRetries = 5, retryOn = {TokenResponseException.class, IOException.class})
    String obtainOauthToken() throws IOException {
        return tokenRequest.execute().getAccessToken();
    }
}
