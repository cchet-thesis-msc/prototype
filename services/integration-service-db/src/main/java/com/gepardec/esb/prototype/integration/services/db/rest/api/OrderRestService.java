package com.gepardec.esb.prototype.integration.services.db.rest.api;

import com.gepardec.esb.prototype.integration.services.db.rest.model.CustomerDto;
import com.gepardec.esb.prototype.integration.services.db.rest.model.OrderDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Path("/order")
@ApiModel(value = "OrderRest", description = "The rest interface for managing Orders")
public interface OrderRestService {

    @Path("/get/{orderNr}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Gets a Order by its orderNr")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.SC_OK, message = "Order could be found for given orderNr", response = OrderDto.class),
            @ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Order could not be found for given orderNr"),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error loading Order by orderNr")
    })
    Response get(@NotNull @Min(0) @PathParam("orderNr") Long id);

    @Path("/list/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Gets a all Orders for a given Customer id")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.SC_OK, message = "All Order could be loaded for Customer id", response = OrderDto.class, responseContainer = "List"),
            @ApiResponse(code = HttpStatus.SC_NOT_FOUND, message = "Could find Customer by id"),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error loading Orders for Customer id")
    })
    Response list(@NotNull @Min(0) @PathParam("id") Long id);
}
