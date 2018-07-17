package com.gepardec.esb.prototype.integration.services.db.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * This class configures the JAX-RS endpoint.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationPath("/rest-api")
@ApplicationScoped
public class RestConfiguration extends Application {
}