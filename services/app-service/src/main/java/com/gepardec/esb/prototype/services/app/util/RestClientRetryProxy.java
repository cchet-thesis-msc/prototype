package com.gepardec.esb.prototype.services.app.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * This class represents the proxy which delegates the calls to the given {@link RetryInvoker#invokeRestClient(Callable)}.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 06/08/18
 */
public class RestClientRetryProxy implements InvocationHandler {

    private final Object delegate;
    private final RetryInvoker retryInvoker;

    public RestClientRetryProxy(Object delegate,
                                RetryInvoker retryInvoker) {
        this.delegate = Objects.requireNonNull(delegate);
        this.retryInvoker = Objects.requireNonNull(retryInvoker);
    }

    @Override
    public Object invoke(Object proxy,
                         Method method,
                         Object[] args) throws Throwable {
        return retryInvoker.invokeRestClient(() -> {
            try {
                return method.invoke(delegate, args);
            } catch (InvocationTargetException ie) {
                throw (RuntimeException) ie.getTargetException();
            }
        });
    }

}
