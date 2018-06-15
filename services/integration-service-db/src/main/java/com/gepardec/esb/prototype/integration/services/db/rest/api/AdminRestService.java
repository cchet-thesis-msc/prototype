package com.gepardec.esb.prototype.integration.services.db.rest.api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@Path("/admin")
public interface AdminRestService {

    @Path("/init")
    @POST
    void init();


    @Path("/clear")
    @POST
    void clear();
}
