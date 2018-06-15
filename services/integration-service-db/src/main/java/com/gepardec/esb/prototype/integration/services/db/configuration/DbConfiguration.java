package com.gepardec.esb.prototype.integration.services.db.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/15/18
 */
@ApplicationScoped
public class DbConfiguration {

    @PersistenceContext(unitName = "dbUnit")
    private EntityManager em;

    @Produces
    @Dependent
    EntityManager createEntityManager() {
        return em;
    }

}
