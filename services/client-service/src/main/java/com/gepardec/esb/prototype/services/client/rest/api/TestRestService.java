package com.gepardec.esb.prototype.services.client.rest.api;

import org.quartz.SchedulerException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/14/18
 */
@Path("/test")
public interface TestRestService {

    @Path("/start")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    String start(@NotNull @Min(0) @Max(10) @QueryParam("count") Integer executorCount,
                 @NotNull @Size(min = 1) @QueryParam("group") String group) throws SchedulerException;

    @Path("/stop")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    String stop(@NotNull @Size(min = 1) @QueryParam("group") String group) throws SchedulerException;

    @Path("/restart/{count}")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    String restart(@NotNull @Min(0) @Max(10) @QueryParam("count") Integer executorCount,
                   @NotNull @Size(min = 1) @QueryParam("group") String group) throws SchedulerException;
}
