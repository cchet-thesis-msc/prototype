package com.gepardec.esb.prototype.services.app.service.impl;

import com.gepardec.esb.prototype.services.app.interceptor.Logging;
import com.gepardec.esb.prototype.services.app.service.api.ReportService;
import com.gepardec.esb.prototype.services.app.service.model.ReportModel;
import io.opentracing.contrib.cdi.Traced;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationScoped
@Traced
@Logging
public class ReportServiceImpl implements ReportService {

    @Inject
    private Logger log;

    @Override
    public ReportModel generateReportForCustomer(Long id) {
        return null;
    }
}
