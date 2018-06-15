package com.gepardec.esb.prototype.integration.services.db.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.SortedMap;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
@Path("/metrics")
public interface MetricRestService {

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    SortedMap<String, SortedMap<String, String>> get();
}
