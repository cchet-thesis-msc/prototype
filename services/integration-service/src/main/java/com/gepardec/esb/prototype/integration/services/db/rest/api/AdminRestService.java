package com.gepardec.esb.prototype.integration.services.db.rest.api;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@Path("/admin")
public interface AdminRestService {

    @Path("/init")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    Response init(@NotNull @Min(1) @Max(20) @QueryParam("customerCount") Integer customerCount,
                  @NotNull @Min(1) @Max(50) @QueryParam("customerOrderCount") Integer customerOrderCount);

    @Path("/clear")
    @POST
    void clear();
}
