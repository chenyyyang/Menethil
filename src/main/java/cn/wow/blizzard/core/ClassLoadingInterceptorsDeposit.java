package cn.wow.blizzard.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ClassLoadingInterceptorsDeposit {

    static final Logger logger = LoggerFactory.getLogger(ClassLoadingInterceptorsDeposit.class);

    public static List<Interceptor> InterceptorsDeposit = new ArrayList<>(1);

    public static void doBeforeConstructor(String className, String methodName, String parameterTypes) {
        logger.debug("doBeforeConstructor:" + className + "-->" + methodName + "-->" + parameterTypes);
        for (Interceptor interceptor : InterceptorsDeposit) {
            try {
                interceptor.doBeforeConstructor(className, methodName, parameterTypes);
            } catch (Throwable throwable) {
                logger.error("doBeforeConstructor error:" + interceptor.shortName(), throwable);
            }
        }
    }

    public static void doBefore(String className, String methodName, String parameterTypes) {
        logger.debug("doBefore:" + className + "-->" + methodName + "-->" + parameterTypes);
        for (Interceptor interceptor : InterceptorsDeposit) {
            try {
                interceptor.doBefore(className, methodName, parameterTypes);
            } catch (Throwable throwable) {
                logger.error("doBefore error:" + interceptor.shortName(), throwable);
            }
        }
    }

    public static void doAfter(String className, String methodName, String parameterTypes) {
        logger.debug("doAfter:" + className + "-->" + methodName + "-->" + parameterTypes);
        for (Interceptor interceptor : InterceptorsDeposit) {
            try {
                interceptor.doAfter(className, methodName, parameterTypes);
            } catch (Throwable throwable) {
                logger.error("doAfter error:" + interceptor.shortName(), throwable);
            }
        }
    }
}
