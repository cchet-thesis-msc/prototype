package com.gepardec.esb.prototype.integration.services.db.rest.impl;

import com.gepardec.esb.prototype.integration.services.db.annotation.Logging;
import com.gepardec.esb.prototype.integration.services.db.rest.api.AdminRestService;
import com.gepardec.esb.prototype.integration.services.db.service.impl.DbInitializer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/15/18
 */
@ApplicationScoped
@Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_API)
public class AdminRestServiceimpl implements AdminRestService {

    @Inject
    private DbInitializer dbInitializer;

    @Override
    public void init() {
        clear();
        dbInitializer.initialize();
    }

    @Override
    public void clear() {
        dbInitializer.clear();
    }
}
