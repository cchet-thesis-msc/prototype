package com.gepardec.esb.prototype.integration.services.db.rest.impl;

import com.gepardec.esb.prototype.integration.services.db.annotation.Logging;
import com.gepardec.esb.prototype.integration.services.db.rest.api.CustomerRestService;
import com.gepardec.esb.prototype.integration.services.db.rest.model.CustomerDto;
import org.eclipse.microprofile.metrics.annotation.Counted;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/10/18
 */
@ApplicationScoped
@Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_API)
public class CustomerRestServiceImpl implements CustomerRestService {

    @Override
    @Counted(name = "get", monotonic = true)
    public CustomerDto get(Long id) {
        return null;
    }

    @Override
    @Counted(name = "list", monotonic = true)
    public List<CustomerDto> list() {
        return null;
    }
}
