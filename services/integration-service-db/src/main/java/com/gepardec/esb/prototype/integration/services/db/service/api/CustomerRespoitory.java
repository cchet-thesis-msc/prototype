package com.gepardec.esb.prototype.integration.services.db.service.api;

import com.gepardec.esb.prototype.integration.services.db.rest.model.CustomerDto;
import com.gepardec.esb.prototype.integration.services.db.service.mapper.CustomerInOutMapper;
import com.gepardec.esb.prototype.integration.services.db.service.model.Customer;
import com.gepardec.esb.prototype.integration.services.db.service.model.Order;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Modifying;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.mapping.MappingConfig;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/15/18
 */
@Repository(forEntity = Customer.class)
@MappingConfig(CustomerInOutMapper.class)
public interface CustomerRespoitory extends EntityRepository<CustomerDto, Long> {

    @Override
    @Counted(name = "findBy", monotonic = true)
    @Timed(name = "duration-findBy", unit = MetricUnits.SECONDS)
    CustomerDto findBy(Long primaryKey);

    @Query(named = Customer.QUERY_DELETE_ALL_CUSTOMER)
    @Modifying
    @Counted(name = "deleteAll", monotonic = true)
    @Timed(name = "duration-deleteAll", unit = MetricUnits.SECONDS)
    void deleteAll();
}
