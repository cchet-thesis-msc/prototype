package com.gepardec.esb.prototype.services.client.annotation;

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
        GROUP_TEST(GROUP, "ESB-TEST"),
        GROUP_TEST_REST_CLIENT(GROUP, "ESB-TEST-REST-CLIENT"),
        GROUP_TEST_REST_SECURITY(GROUP, "ESB-TEST-REST-SECURITY"),
        DEFAULT(GROUP_TEST.key, GROUP_TEST.value);

        public final String key;
        public final String value;


        MDCConfig(String key,
                  String value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * @return The intended MDCConfig instance, if {@link MDCConfig#GROUP_TEST} no mdc logic will be applied.
     */
    @Nonbinding
    MDCConfig mdcConfig() default MDCConfig.GROUP_TEST;

    @Nonbinding
    boolean skipResult() default false;
}
