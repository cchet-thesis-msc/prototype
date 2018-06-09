package com.gepardec.esb.prototype.services.app.rest.impl;

import com.gepardec.esb.prototype.services.app.interceptor.Logging;
import com.gepardec.esb.prototype.services.app.rest.api.ReportRestService;
import com.gepardec.esb.prototype.services.app.rest.client.api.integration.database.ReportRestServiceApi;
import com.gepardec.esb.prototype.services.app.rest.model.ReportModelDto;
import org.dozer.Mapper;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * This class implements the rest operations specified by the implementing interface.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationScoped
@Logging(mdcConfig = Logging.MDCConfig.GROUP_REST_API)
public class ReportRestServiceImpl implements ReportRestService {

    @Inject
    private Mapper mapper;

    @Inject
    private ReportRestServiceApi reportRestServiceClient;

    @Override
    public ReportModelDto generate(Long id) {
        return null;
    }

    @Override
    public ReportModelDto testRetry() {
        return mapper.map(reportRestServiceClient.generate1(-1L), ReportModelDto.class);
    }
}
