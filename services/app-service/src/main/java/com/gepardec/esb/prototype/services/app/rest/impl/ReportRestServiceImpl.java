package com.gepardec.esb.prototype.services.app.rest.impl;

import com.gepardec.esb.prototype.services.app.interceptor.Logging;
import com.gepardec.esb.prototype.services.app.rest.api.ReportRestService;
import com.gepardec.esb.prototype.services.app.rest.model.ReportModelDto;
import org.dozer.Mapper;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import java.time.temporal.ChronoUnit;

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

    @Override
    @Retry(delay = 1L, delayUnit = ChronoUnit.SECONDS, maxRetries = 5, retryOn = ClientErrorException.class)
    public ReportModelDto generate(Long id) {
        return null;
    }
}
