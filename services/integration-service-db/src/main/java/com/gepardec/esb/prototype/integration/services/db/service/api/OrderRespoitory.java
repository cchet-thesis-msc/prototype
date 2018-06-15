package com.gepardec.esb.prototype.integration.services.db.service.api;

import com.gepardec.esb.prototype.integration.services.db.rest.model.OrderDto;
import com.gepardec.esb.prototype.integration.services.db.service.mapper.OrderInOutMapper;
import com.gepardec.esb.prototype.integration.services.db.service.model.Order;
import org.apache.deltaspike.data.api.*;
import org.apache.deltaspike.data.api.mapping.MappingConfig;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import java.util.List;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/15/18
 */
@Repository(forEntity = Order.class)
@MappingConfig(OrderInOutMapper.class)
public interface OrderRespoitory extends EntityRepository<OrderDto, Long> {

    @Query(named = Order.QUERY_FIND_FOR_CUSTOMER)
    @Counted(name = "findOrdersForCustomerId", monotonic = true)
    @Timed(name = "duration-findOrdersForCustomerId", unit = MetricUnits.MILLISECONDS)
    List<OrderDto> findOrdersForCustomerId(@QueryParam("id") Long id);

    @Query(named = Order.QUERY_DELETE_ALL_ORDER)
    @Modifying
    @Counted(name = "deleteAll", monotonic = true)
    @Timed(name = "duration-deleteAll", unit = MetricUnits.MILLISECONDS)
    void deleteAll();
}
