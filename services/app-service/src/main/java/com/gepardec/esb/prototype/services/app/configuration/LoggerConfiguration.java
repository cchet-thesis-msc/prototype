package com.gepardec.esb.prototype.services.app.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
/**
 * This produces and configures the logger.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationScoped
public class LoggerConfiguration {

    @Produces
    @Default
    @Dependent
    Logger createLogger(final InjectionPoint ip) {
        if (ip.getBean() != null) {
            return LoggerFactory.getLogger(ip.getBean().getBeanClass());
        } else if (ip.getMember() != null) {
            return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
        } else {
            return LoggerFactory.getLogger("default");
        }
    }
}
