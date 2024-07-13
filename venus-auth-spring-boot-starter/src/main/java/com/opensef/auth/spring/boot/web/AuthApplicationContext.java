package com.opensef.auth.spring.boot.web;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * spring上下文工具类
 */
@Component
public class AuthApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (AuthApplicationContext.applicationContext == null) {
            AuthApplicationContext.applicationContext = applicationContext;
        }
    }

    /**
     * 获取applicationContext
     *
     * @return {@link ApplicationContext}
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean
     *
     * @param name bean名称
     * @return Bean对象
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 通过class获取Bean
     *
     * @param clazz class bean
     * @param <T>   泛型对象
     * @return Bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext.getBeanNamesForType(clazz).length == 0) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name  bean名称
     * @param clazz class bean
     * @param <T>   泛型
     * @return Bean对象
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 获取当前线程的HttpServletRequest对象
     *
     * @return HttpServletRequest对象
     * @since 1.0
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return requestAttributes.getRequest();
        }
        return null;
    }

    /**
     * 获取当前线程的HttpServletResponse对象
     *
     * @return HttpServletResponse对象
     */
    public static HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return requestAttributes.getResponse();
        }
        return null;
    }

    /**
     * 获取请求的URI
     *
     * @return URI
     */
    public static String getRequestUri() {
        return getRequestUri(getHttpServletRequest());
    }

    /**
     * 获取请求的URI
     *
     * @return URI
     */
    public static String getRequestUri(HttpServletRequest request) {
        if (null == request) {
            return "";
        }
        return request.getRequestURI();
    }

    /**
     * 获取请求Attribute
     *
     * @param attributeName 属性名
     * @return 属性值
     */
    public static Object getRequestAttribute(String attributeName) {
        return getHttpServletRequest().getAttribute(attributeName);
    }

    /**
     * 获取请求Header
     *
     * @param headerName Header名称
     * @return Header
     */
    public static String getRequestHeader(String headerName) {
        return getHttpServletRequest().getHeader(headerName);
    }

}
