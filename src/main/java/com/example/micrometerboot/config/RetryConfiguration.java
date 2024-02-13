package com.example.micrometerboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;

@Slf4j
@EnableRetry
@Configuration
public class RetryConfiguration {

    private static final int OPTIMISTIC_LOCKING_MAX_RETRIES = 3;

    private static final RetryListener RETRY_LISTENER = new RetryListenerSupport() {
        @Override
        public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
            if (throwable instanceof ObjectOptimisticLockingFailureException) {
                log.warn("Retryable error occurred on " + context.getAttribute(RetryContext.NAME) + " - retry no. " + context.getRetryCount() + ": " + throwable.getMessage());
            }
        }
    };

    @Bean
    public RetryOperationsInterceptor optimisticLockingAutoRetryInterceptor() {
        RetryTemplate template = new RetryTemplate();
        template.registerListener(RETRY_LISTENER);
        template.setRetryPolicy(new SimpleRetryPolicy(OPTIMISTIC_LOCKING_MAX_RETRIES, Map.of(ObjectOptimisticLockingFailureException.class, Boolean.TRUE)));
        return RetryInterceptorBuilder.stateless().retryOperations(template).build();
    }

}
