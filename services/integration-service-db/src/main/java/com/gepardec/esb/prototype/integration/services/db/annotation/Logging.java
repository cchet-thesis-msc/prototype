package com.gepardec.esb.prototype.integration.services.db.annotation;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * This annotation declares a type or a method to be logged in a generic manner, with the mdc context defined.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Logging {

    String GROUP = "trace.group";

    /**
     * Predefined mdc contexts appended to the predefined once for tracing for instance.
     * Don't forget to add the mdc context in the log formatter of your logging framework.
     */
    enum MDCConfig {
        DEFAULT("", ""),
        GROUP_REST_API(GROUP, "REST-API"),
        GROUP_REST_CLIENT(GROUP, "REST-CLIENT"),
        GROUP_REST_SECURITY(GROUP, "REST-SECURITY"),
        GROUP_SERVICE(GROUP, "SERVICE");

        public final String key;
        public final String value;


        MDCConfig(String key,
                  String value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * @return The intended MDCConfig instance, if {@link MDCConfig#DEFAULT} no mdc logic will be applied.
     */
    @Nonbinding
    MDCConfig mdcConfig() default MDCConfig.DEFAULT;

    @Nonbinding
    boolean skipResult() default false;
}
