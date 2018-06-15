package com.gepardec.esb.prototype.services.app.rest.impl;

import com.gepardec.esb.prototype.services.app.annotation.Logging;
import com.gepardec.esb.prototype.services.app.rest.api.ReportRestService;
import com.gepardec.esb.prototype.services.app.rest.client.api.integration.database.CustomerRestApi;
import com.gepardec.esb.prototype.services.app.rest.client.api.integration.database.OrderRestApi;
import com.gepardec.esb.prototype.services.app.rest.client.model.integration.database.DbCustomer;
import com.gepardec.esb.prototype.services.app.rest.client.model.integration.database.DbOrder;
import com.gepardec.esb.prototype.services.app.rest.model.ReportModelDto;
import org.dozer.Mapper;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

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
    private CustomerRestApi customerRestApi;
    @Inject
    private OrderRestApi orderRestApi;

    @Override
    @Counted(name = "report-downloads", monotonic = true)
    @Timed(name = "duration-report-downloads", unit = MetricUnits.MILLISECONDS)
    public ReportModelDto generate(Long id) {
        final DbCustomer customer = customerRestApi.get1(id);
        final List<DbOrder> orders = orderRestApi.list2(id);

        return new ReportModelDto(customer.getFirstName() + ", " + customer.getLastName(),
                                  Long.valueOf(orders.size()),
                                  orders.stream().flatMap((order) -> order.getItems().stream()).mapToDouble((item) -> item.getPrice().doubleValue()).sum());
    }
}
