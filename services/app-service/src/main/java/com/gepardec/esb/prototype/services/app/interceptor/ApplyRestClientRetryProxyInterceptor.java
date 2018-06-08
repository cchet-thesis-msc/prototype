package com.gepardec.esb.prototype.services.app.interceptor;

import com.gepardec.esb.prototype.services.app.util.RestClientRetryProxy;
import com.gepardec.esb.prototype.services.app.util.RetryInvoker;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.Callable;

/**
 * This interceptors applies an proxy to the produce instance which is expected to be a rest client
 * which the {@link RetryInvoker#invokeRestClient(Callable)} declares the rest client retry behavior.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
@Priority(Interceptor.Priority.APPLICATION)
@Interceptor
@ApplyRestClientRetryProxy
public class ApplyRestClientRetryProxyInterceptor {

    @Inject
    private RetryInvoker retryInvoker;

    @AroundInvoke
    public Object aroundInvoke(final InvocationContext ic) throws Throwable {
        Object produceInst = ic.proceed();
        try {
            return Proxy.newProxyInstance(RestClientRetryProxy.class.getClassLoader(),
                                          produceInst.getClass().getInterfaces(),
                                          new RestClientRetryProxy(produceInst, retryInvoker));
        } catch (UndeclaredThrowableException e) {
            throw e.getUndeclaredThrowable();
        }
    }
}
