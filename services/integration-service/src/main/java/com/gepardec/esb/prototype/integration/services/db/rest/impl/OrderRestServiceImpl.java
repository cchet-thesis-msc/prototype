package com.gepardec.esb.prototype.integration.services.db.rest.impl;

import com.gepardec.esb.prototype.integration.services.db.annotation.Logging;
import com.gepardec.esb.prototype.integration.services.db.rest.api.OrderRestService;
import com.gepardec.esb.prototype.integration.services.db.rest.model.CustomerDto;
import com.gepardec.esb.prototype.integration.services.db.rest.model.OrderDto;
import com.gepardec.esb.prototype.integration.services.db.service.api.CustomerRespoitory;
import com.gepardec.esb.prototype.integration.services.db.service.api.OrderRespoitory;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@ApplicationScoped
@Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_API)
public class OrderRestServiceImpl implements OrderRestService {

    @Inject
    private OrderRespoitory orderRespoitory;
    @Inject
    private CustomerRespoitory customerRespoitory;

    @Override
    @Counted(name = "get", monotonic = true)
    @Timed(name = "duration-get", unit = MetricUnits.SECONDS)
    public Response get(Long id) {
        final OrderDto dto = orderRespoitory.findBy(id);
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(dto).build();
    }

    @Override
    @Counted(name = "list", monotonic = true)
    @Timed(name = "duration-list", unit = MetricUnits.SECONDS)
    public Response list(final Long id) {
        final CustomerDto customer = customerRespoitory.findBy(id);
        if (customer == null) {
            Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(orderRespoitory.findOrdersForCustomerId(id)).build();
    }
}
