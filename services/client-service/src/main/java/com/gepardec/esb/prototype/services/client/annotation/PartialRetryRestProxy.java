package com.gepardec.esb.prototype.services.client.annotation;

import org.apache.deltaspike.partialbean.api.PartialBeanBinding;

import java.lang.annotation.*;

/**
 * Qualifier marking a class implementing {@link java.lang.reflect.InvocationHandler} as the method handler
 * for a interface or abstract class annotated with this qualifier.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@Inherited
@PartialBeanBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PartialRetryRestProxy {

    /**
     * @return the plain interface class containing the JAX-RS annotations. Necessary because JAX-RS annotations are not @Inherited and therefore
     * client proxy generation fails because we cannot retrieve the original type.
     */
    Class<?> interfaceClass() default Void.class;
}
