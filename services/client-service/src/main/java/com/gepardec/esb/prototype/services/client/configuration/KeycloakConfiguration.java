package com.gepardec.esb.prototype.services.client.configuration;

import com.gepardec.esb.prototype.services.client.annotation.Logging;
import com.google.api.client.auth.oauth2.ClientCredentialsTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Counted;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.time.temporal.ChronoUnit;

/**
 * This class produces all Keycloak related resources.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/19/18
 */
@ApplicationScoped
public class KeycloakConfiguration {


    @Inject
    @ConfigProperty(name = "keycloak.token-url", defaultValue = "")
    private String keycloakTokenUrl;
    @Inject
    @ConfigProperty(name = "keycloak.client.id", defaultValue = "")
    private String keycloakClientId;
    @Inject
    @ConfigProperty(name = "keycloak.client.secret", defaultValue = "")
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
    @RequestScoped
    @Counted(name = "retrieved-oauth-tokens", monotonic = true)
    @Logging(mdcConfig = Logging.MDCConfig.GROUP_TEST_REST_SECURITY, skipResult = true)
    @Retry(delay = 100L, maxRetries = 5, retryOn = {TokenResponseException.class, IOException.class})
    @Timeout(value = 5L, unit = ChronoUnit.SECONDS)
    KeycloakAuth obtainOauthToken() throws IOException {
        return new KeycloakAuth(tokenRequest.execute().getAccessToken());
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeycloakAuth implements Serializable {

        private String token;
    }
}
