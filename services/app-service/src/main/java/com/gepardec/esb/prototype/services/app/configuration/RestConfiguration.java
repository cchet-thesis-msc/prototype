package com.gepardec.esb.prototype.services.app.configuration;

import com.gepardec.esb.prototype.services.app.rest.provider.JXRSEcxeptionMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * This class configures the JAX-RS endpoint.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationPath("/rest-api")
@ApplicationScoped
public class RestConfiguration extends Application {

//    private static final Set<Class<?>> clazzes = new HashSet<Class<?>>(){{
//       add(JXRSEcxeptionMapper.class);
//    }};
//
//    @Override
//    public Set<Class<?>> getClasses() {
//        return clazzes;
//    }
}