package com.gepardec.esb.prototype.integration.services.db.service.api;

import com.gepardec.esb.prototype.integration.services.db.rest.model.CustomerDto;
import com.gepardec.esb.prototype.integration.services.db.rest.model.OrderDto;
import com.gepardec.esb.prototype.integration.services.db.service.mapper.OrderInOutMapper;
import com.gepardec.esb.prototype.integration.services.db.service.model.Customer;
import com.gepardec.esb.prototype.integration.services.db.service.model.Order;
import org.apache.deltaspike.data.api.*;
import org.apache.deltaspike.data.api.mapping.MappingConfig;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/15/18
 */
//@ApplicationScoped
//@Repository(forEntity = Order.class)
//@MappingConfig(OrderInOutMapper.class)
public interface OrderRespoitory {//extends EntityRepository<OrderDto, Long> {
    
    @Counted(name = "findBy", monotonic = true)
    @Timed(name = "duration-findBy", unit = MetricUnits.SECONDS)
    OrderDto findBy(Long primaryKey);

    @Query(named = Order.QUERY_FIND_FOR_CUSTOMER)
    @Counted(name = "findOrdersForCustomerId", monotonic = true)
    @Timed(name = "duration-findOrdersForCustomerId", unit = MetricUnits.SECONDS)
    List<OrderDto> findOrdersForCustomerId(@QueryParam("id") Long id);

    @Query(named = Order.QUERY_DELETE_ALL_ORDER)
    @Modifying
    @Counted(name = "deleteAll", monotonic = true)
    @Timed(name = "duration-deleteAll", unit = MetricUnits.SECONDS)
    void deleteAll();


    // Needed because DeltaSpike-Data is broken
    List<OrderDto> findAll();

    OrderDto save(OrderDto order);

    void flush();
}
