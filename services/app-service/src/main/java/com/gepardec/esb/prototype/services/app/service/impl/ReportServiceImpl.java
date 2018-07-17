package com.gepardec.esb.prototype.services.app.service.impl;

import com.gepardec.esb.prototype.services.app.annotation.Logging;
import com.gepardec.esb.prototype.services.app.rest.client.api.integration.database.CustomerRestApi;
import com.gepardec.esb.prototype.services.app.rest.client.api.integration.database.OrderRestApi;
import com.gepardec.esb.prototype.services.app.rest.client.model.integration.database.DbCustomer;
import com.gepardec.esb.prototype.services.app.rest.client.model.integration.database.DbOrder;
import com.gepardec.esb.prototype.services.app.service.api.ReportService;
import com.gepardec.esb.prototype.services.app.service.model.ReportModel;
import io.opentracing.contrib.cdi.Traced;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationScoped
@Traced
@Logging(mdcConfig = Logging.MDCConfig.GROUP_SERVICE)
public class ReportServiceImpl implements ReportService {

    @Inject
    private OrderRestApi orderRestApi;
    @Inject
    private CustomerRestApi customerRestApi;
    @Inject
    private Logger log;

    @Override
    public ReportModel generateReportForCustomer(Long id) {
        final DbCustomer customer = customerRestApi.get1(id);
        final List<DbOrder> orders = orderRestApi.list2(id);

        return new ReportModel(String.format("%s, %s", customer.getLastName(), customer.getFirstName()),
                               (long) orders.size(),
                               orders.stream()
                                     .flatMap((order) -> order.getItems()
                                                              .stream())
                                     .mapToDouble((item) -> item.getPrice()
                                                                .doubleValue())
                                     .sum());
    }
}
