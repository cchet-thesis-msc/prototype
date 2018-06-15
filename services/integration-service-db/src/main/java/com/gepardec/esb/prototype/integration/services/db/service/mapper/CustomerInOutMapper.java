package com.gepardec.esb.prototype.integration.services.db.service.mapper;

import com.gepardec.esb.prototype.integration.services.db.rest.model.CustomerDto;
import com.gepardec.esb.prototype.integration.services.db.service.model.Customer;
import org.apache.deltaspike.data.api.mapping.SimpleQueryInOutMapperBase;

import javax.enterprise.context.ApplicationScoped;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/15/18
 */
@ApplicationScoped
public class CustomerInOutMapper extends SimpleQueryInOutMapperBase<Customer, CustomerDto> {

    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String DELIMITER = ";";

    @Override
    protected Object getPrimaryKey(CustomerDto customerDto) {
        return (customerDto != null) ? customerDto.getId() : null;
    }

    @Override
    protected CustomerDto toDto(Customer customer) {
        if (customer != null) {
            final Map<String, String> nameParts = extractNameParts(customer.getFullName());
            final CustomerDto dto = new CustomerDto();
            dto.setId(customer.getId());
            dto.setEmail(customer.getEmail());
            dto.setFirstName(nameParts.get(FIRST_NAME));
            dto.setLastName(nameParts.get(LAST_NAME));
            dto.setCreatedAt(Date.from(customer.getCreatedAt().toInstant(ZoneOffset.UTC)));
            dto.setModifiedAt(Date.from(customer.getModifiedAt().toInstant(ZoneOffset.UTC)));
            return dto;
        }

        return null;
    }

    @Override
    protected Customer toEntity(Customer customer,
                                CustomerDto customerDto) {
        if(customerDto != null) {
            customer = (customer != null) ? customer : new Customer();
            customer.setId(customerDto.getId());
            customer.setEmail(customerDto.getEmail());
            customer.setFullName(customerDto.getFirstName() + DELIMITER + customerDto.getLastName());
            customer.setVersion(customerDto.getVersion());

            return customer;
        }

        throw new IllegalStateException("Why is orderDto null here ?");
    }

    private static Map<String, String> extractNameParts(final String full) {
        final Map<String, String> result = new HashMap<String, String>() {{
            put(FIRST_NAME, "undefined");
            put(LAST_NAME, "undefined");
        }};

        final String[] parts = full.split(DELIMITER);
        if (parts.length > 0) {
            result.put(FIRST_NAME, parts[0]);
            if (parts.length > 1) {
                result.put(LAST_NAME, parts[1]);
            }
        }

        return result;
    }
}
