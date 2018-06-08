package com.gepardec.esb.prototype.services.app.util;

import org.eclipse.microprofile.faulttolerance.Retry;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ClientErrorException;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@ApplicationScoped
public class RetryInvoker {

    @Retry(delay = 1L, delayUnit = ChronoUnit.SECONDS, maxRetries = 5, retryOn = {ClientErrorException.class})
    public <T> T invokeRestClient(Callable<T> callable) throws Throwable {
        return callable.call();
    }
}
