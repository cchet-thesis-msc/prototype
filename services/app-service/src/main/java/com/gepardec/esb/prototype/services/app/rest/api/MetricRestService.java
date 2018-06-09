package com.gepardec.esb.prototype.services.app.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
@Path("/metrics")
public interface MetricRestService {

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, Long> get();
}
