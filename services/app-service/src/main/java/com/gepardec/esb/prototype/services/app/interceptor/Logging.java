package com.gepardec.esb.prototype.services.app.interceptor;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Logging {
}
