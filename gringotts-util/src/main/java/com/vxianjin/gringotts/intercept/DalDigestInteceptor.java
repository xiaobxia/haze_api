package com.vxianjin.gringotts.intercept;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @Author: chenkai
 * @Date: 2018/8/12 9:45
 * @Description:
 */
public class DalDigestInteceptor implements MethodInterceptor {


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String wholeClassName = method.getDeclaringClass().getName();
        String className =  method.getDeclaringClass().getSimpleName();
        String methodName = className + "." + method.getName();
        String packageName = method.getDeclaringClass().getPackage().getName();
        Object invocationResult = null;
        long startTime = System.currentTimeMillis();
        boolean hasError = false;
        try {
            invocationResult = invocation.proceed();
            StringBuffer serviceMessage;
            long spaceTime;
            return invocationResult;
        } catch (Exception e) {
            hasError = true;
            LoggerFactory.getLogger(wholeClassName).error(MessageFormat.format("{0} 处理异常，e:{1}",className,e.toString()));
            throw e;
        } finally {
            StringBuffer serviceMessage = new StringBuffer();
            serviceMessage.append("[(");
            long spaceTime = System.currentTimeMillis() - startTime;
            serviceMessage.append(packageName).append(".");
            if (hasError) {
                serviceMessage.append(methodName).append(",N,").append(spaceTime).append("ms");
            } else {
                serviceMessage.append(methodName).append(",Y,").append(spaceTime).append("ms");
            }
            serviceMessage.append(")]");
            LoggerFactory.getLogger(wholeClassName).info(serviceMessage.toString());
        }
    }
}
