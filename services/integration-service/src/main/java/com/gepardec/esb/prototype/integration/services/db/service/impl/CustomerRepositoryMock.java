package com.gepardec.esb.prototype.integration.services.db.service.impl;

import com.gepardec.esb.prototype.integration.services.db.rest.model.CustomerDto;
import com.gepardec.esb.prototype.integration.services.db.service.api.CustomerRespoitory;
import com.gepardec.esb.prototype.integration.services.db.service.model.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class mocks CustomerRepository which is broken because Deltaspike proxy module is broken.
 * https://jira.apache.org/jira/browse/DELTASPIKE-1060
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 9/8/2018
 */
@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class CustomerRepositoryMock implements CustomerRespoitory {

    @Inject
    private EntityManager em;

    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String DELIMITER = ";";

    @Override
    public CustomerDto findBy(Long primaryKey) {
        return toDto(em.find(Customer.class, primaryKey));
    }

    @Override
    public void deleteAll() {
        em.createNamedQuery(Customer.QUERY_DELETE_ALL_CUSTOMER).executeUpdate();
    }

    @Override
    public List<CustomerDto> findAll() {
        return em.createQuery("from Customer", Customer.class).getResultList().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public CustomerDto save(CustomerDto customer) {
        Customer updatedCustomer = new Customer();
        if (customer.getId() == null) {
            em.persist(toEntity(updatedCustomer, customer));
        } else {
            updatedCustomer = em.merge(toEntity(updatedCustomer, customer));
        }
        return toDto(em.find(Customer.class, updatedCustomer.getId()));
    }

    @Override
    public void flush() {
        em.flush();
    }

    protected CustomerDto toDto(Customer customer) {
        if (customer != null) {
            final Map<String, String> nameParts = extractNameParts(customer.getFullName());
            final CustomerDto dto = new CustomerDto();
            dto.setId(customer.getId());
            dto.setVersion(customer.getVersion());
            dto.setEmail(customer.getEmail());
            dto.setFirstName(nameParts.get(FIRST_NAME));
            dto.setLastName(nameParts.get(LAST_NAME));
            dto.setCreatedAt(Date.from(customer.getCreatedAt().toInstant(ZoneOffset.UTC)));
            dto.setModifiedAt(Date.from(customer.getModifiedAt().toInstant(ZoneOffset.UTC)));
            return dto;
        }

        return null;
    }

    protected Customer toEntity(Customer customer,
                                CustomerDto customerDto) {
        if (customerDto != null) {
            customer = (customer != null) ? customer : new Customer();
            customer.setId((customer.getId() != null) ? customer.getId() : customerDto.getId());
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
