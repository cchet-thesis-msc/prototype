package com.gepardec.esb.prototype.services.app.rest.api;

import com.gepardec.esb.prototype.services.app.rest.model.ReportModelDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * This interface specifies the rest api available to clients an documents them for swagger as well.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@Path("/report")
@Api(value = "ReportRestService", description = "The api for generating reports for customers")
public interface ReportRestService {

    @Path("/generate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Generates the report for the given customer", response = ReportModelDto.class)
    ReportModelDto generate(@QueryParam("id") @NotNull @Min(0) @Max(Long.MAX_VALUE) Long id);

}
