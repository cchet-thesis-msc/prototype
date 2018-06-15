package com.gepardec.esb.prototype.integration.services.db.rest.impl;

import com.gepardec.esb.prototype.integration.services.db.annotation.Logging;
import com.gepardec.esb.prototype.integration.services.db.rest.api.CustomerRestService;
import com.gepardec.esb.prototype.integration.services.db.rest.model.CustomerDto;
import com.gepardec.esb.prototype.integration.services.db.service.api.CustomerRespoitory;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@ApplicationScoped
@Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_API)
public class CustomerRestServiceImpl implements CustomerRestService {

    @Inject
    private CustomerRespoitory customerRespoitory;

    @Override
    @Counted(name = "get", monotonic = true)
    @Timed(name = "duration-get", unit = MetricUnits.SECONDS)
    @Transactional(value = Transactional.TxType.NEVER)
    public Response get(Long id) {
        final CustomerDto dto = customerRespoitory.findBy(id);
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(String.format("Customer not found for id '%d'", id)).build();
        }
        return Response.ok().entity(dto).build();
    }

    @Override
    @Counted(name = "list", monotonic = true)
    @Timed(name = "duration-list", unit = MetricUnits.SECONDS)
    @Transactional(value = Transactional.TxType.NEVER)
    public List<CustomerDto> list() {
        return customerRespoitory.findAll();
    }
}
