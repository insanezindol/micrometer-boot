package com.example.micrometerboot.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class MDCLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        UUID uniqueId = UUID.randomUUID();
        MDC.put("X-API-ID", uniqueId.toString());
        log.trace("Request IP address is {}", servletRequest.getRemoteAddr());
        log.trace("Request content type is {}", servletRequest.getContentType());
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
        filterChain.doFilter(servletRequest, responseWrapper);
        responseWrapper.setHeader("X-API-ID", uniqueId.toString());
        responseWrapper.copyBodyToResponse();
        log.trace("Response header is set with uuid {}", responseWrapper.getHeader("X-API-ID"));
    }

}