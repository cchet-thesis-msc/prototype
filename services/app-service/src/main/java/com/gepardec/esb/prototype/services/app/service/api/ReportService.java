package com.gepardec.esb.prototype.services.app.service.api;

import com.gepardec.esb.prototype.services.app.service.model.ReportModel;

/**
 * This interface specifies the service operations of this microservice.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
public interface ReportService {

    ReportModel generateReportForCustomer(Long id);
}
