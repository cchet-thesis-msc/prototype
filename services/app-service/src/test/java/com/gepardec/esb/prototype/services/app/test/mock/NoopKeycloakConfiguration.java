package com.gepardec.esb.prototype.services.app.test.mock;

import com.gepardec.esb.prototype.services.app.annotation.OAuthToken;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
@ApplicationScoped
public class NoopKeycloakConfiguration {

    @Produces
    @OAuthToken
    @Dependent
    String createToken() {
        return "token";
    }
}
