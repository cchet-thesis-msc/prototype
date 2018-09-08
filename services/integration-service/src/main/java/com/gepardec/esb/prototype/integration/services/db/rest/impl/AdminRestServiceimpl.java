package com.gepardec.esb.prototype.integration.services.db.rest.impl;

import com.gepardec.esb.prototype.integration.services.db.annotation.Logging;
import com.gepardec.esb.prototype.integration.services.db.rest.api.AdminRestService;
import com.gepardec.esb.prototype.integration.services.db.service.impl.DbInitializer;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/15/18
 */
@ApplicationScoped
@Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_API)
public class AdminRestServiceimpl implements AdminRestService {

    @Inject
    private DbInitializer dbInitializer;

    private AtomicBoolean running = new AtomicBoolean(false);

    @Override
    @Counted(name = "init", monotonic = true)
    @Timed(name = "duration-init", unit = MetricUnits.SECONDS)
    public Response init(final Integer customerCount,
                         final Integer customerOrderCount) {
        if (running.compareAndSet(false, true)) {
            try {
                clear();
                dbInitializer.initialize(customerCount,
                                         customerOrderCount);
                return Response.ok().entity("Initialized.").build();
            } finally {
                running.set(false);
            }
        }

        return Response.status(Response.Status.CONFLICT).entity("Already initializing").build();
    }

    @Override
    public void clear() {
        dbInitializer.clear();
    }
}
