package com.gepardec.esb.prototype.services.client.annotation;

import javax.inject.Qualifier;
import java.lang.annotation.*;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/09/18
 */
@Inherited
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface OAuthToken {
}
