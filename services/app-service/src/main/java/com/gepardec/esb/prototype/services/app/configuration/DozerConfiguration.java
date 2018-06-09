package com.gepardec.esb.prototype.services.app.configuration;

import com.gepardec.esb.prototype.services.app.annotation.Logging;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

/**
 * This class produces and configures the dozer mapper framework.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationScoped
public class DozerConfiguration {

    @Produces
    @Default
    @Dependent
    @Logging
    Mapper createMapper() {
        return new DozerBeanMapper();
    }
}
