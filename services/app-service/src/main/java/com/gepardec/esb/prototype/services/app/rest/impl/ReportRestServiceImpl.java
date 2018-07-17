package com.gepardec.esb.prototype.services.app.rest.impl;

import com.gepardec.esb.prototype.services.app.annotation.Logging;
import com.gepardec.esb.prototype.services.app.rest.api.ReportRestService;
import com.gepardec.esb.prototype.services.app.rest.model.ReportModelDto;
import com.gepardec.esb.prototype.services.app.service.api.ReportService;
import org.dozer.Mapper;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationScoped
@Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_API)
public class ReportRestServiceImpl implements ReportRestService {

    @Inject
    private ReportService service;
    @Inject
    private Mapper mapper;

    @Override
    @Counted(name = "report-downloads", monotonic = true)
    @Timed(name = "duration-report-downloads", unit = MetricUnits.SECONDS)
    public ReportModelDto generate(Long id) {
        return mapper.map(service.generateReportForCustomer(id), ReportModelDto.class);
    }
}
