package com.gepardec.esb.prototype.integration.services.db.rest.api;

import com.gepardec.esb.prototype.integration.services.db.rest.model.CustomerDto;
import io.swagger.annotations.*;
import org.apache.http.HttpStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@Path("/customer")
@Api(value = "CustomerRest", description = "The rest interface for managing Customers")
public interface CustomerRestService {

    @Path("/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Gets a customer by its id", response = CustomerDto.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.SC_OK, message = "Customer could be found for given id", response = CustomerDto.class),
            @ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Customer could not be found for given id"),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error loading customer by id")
    })
    Response get(@NotNull @Min(0) @PathParam("id") Long id);

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List all customers", response = CustomerDto.class)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.SC_OK, message = "All Customers could be loaded", response = CustomerDto.class, responseContainer = "List"),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error loading customers")
    })
    List<CustomerDto> list();
}
