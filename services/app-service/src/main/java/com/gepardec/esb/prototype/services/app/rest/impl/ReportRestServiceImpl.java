package com.gepardec.esb.prototype.services.app.rest.impl;

import com.gepardec.esb.prototype.services.app.configuration.RestClientConfiguration;
import com.gepardec.esb.prototype.services.app.interceptor.Logging;
import com.gepardec.esb.prototype.services.app.rest.api.ReportRestService;
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
@Logging
public class ReportRestServiceImpl implements ReportRestService {

    @Inject
    private Mapper mapper;
    @Inject
    private Logger log;

    @Inject
    @RestClientConfiguration.RestClient
    private ReportRestService reportRestServiceClient;

    @Override
    public ReportModelDto generate(Long id) {
        return null;
    }

    @Override
    public ReportModelDto testRetry() {
        return reportRestServiceClient.generate(-1L);
    }
}
