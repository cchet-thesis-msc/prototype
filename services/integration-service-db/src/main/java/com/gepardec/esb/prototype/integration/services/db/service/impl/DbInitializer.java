package com.gepardec.esb.prototype.integration.services.db.service.impl;

import com.gepardec.esb.prototype.integration.services.db.rest.model.CustomerDto;
import com.gepardec.esb.prototype.integration.services.db.rest.model.ItemDto;
import com.gepardec.esb.prototype.integration.services.db.rest.model.OrderDto;
import com.gepardec.esb.prototype.integration.services.db.service.api.CustomerRespoitory;
import com.gepardec.esb.prototype.integration.services.db.service.api.OrderRespoitory;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/15/18
 */
@ApplicationScoped
public class DbInitializer implements Serializable {

    @Inject
    private CustomerRespoitory customerRespoitory;
    @Inject
    private OrderRespoitory orderRespoitory;

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    @Counted(name = "clear", monotonic = true)
    @Timed(name = "duration-clear", unit = MetricUnits.MILLISECONDS)
    public void clear() {
        orderRespoitory.deleteAll();
        orderRespoitory.flush();

        customerRespoitory.deleteAll();
        customerRespoitory.flush();
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    @Counted(name = "initialize", monotonic = true)
    @Timed(name = "duration-initialize", unit = MetricUnits.MILLISECONDS)
    public void initialize() {
        List<CustomerDto> customers = new LinkedList<>();
        if (customerRespoitory.findAll().size() == 0) {
            for (int i = 1; i <= 10; i++) {
                final CustomerDto customer = new CustomerDto();
                customer.setEmail(String.format("thomas-%d@gmail.com", i));
                customer.setFirstName(String.format("thomas-%d", i));
                customer.setLastName(String.format("herzog-%d", i));
                customers.add(customer);
            }
            customers.forEach(customerRespoitory::save);
            customerRespoitory.flush();
        }

        if (orderRespoitory.findAll().size() == 0) {
            customers = customerRespoitory.findAll();
            List<OrderDto> orders = new LinkedList<>();
            for (CustomerDto customer : customers) {
                for (int i = 1; i <= 10; i++) {
                    final List<ItemDto> itemDtos = new LinkedList<>();
                    for (int j = 1; i <= 10; i++) {
                        final ItemDto item = new ItemDto();
                        item.setName(String.format("Item-%d", j));
                        item.setCount(10L);
                        item.setPrice(10.0);
                        itemDtos.add(item);
                    }
                    final OrderDto order = new OrderDto();
                    order.setCustomerId(customer.getId());
                    order.setItems(itemDtos);
                    order.setDeliveredAt(LocalDateTime.now());

                    orderRespoitory.save(order);
                }
                orderRespoitory.flush();
            }
        }
    }
}
