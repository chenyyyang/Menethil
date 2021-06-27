package cn.wow.blizzard.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassLoadingInterceptorsDeposit {

    static final Logger logger = LoggerFactory.getLogger(ClassLoadingInterceptorsDeposit.class);

    public static void doBeforeConstructor(String className, String methodName,String parameterTypes){
        logger.info("doBeforeConstructor:"+className+"-->"+methodName+"-->"+parameterTypes);
    }

    public static void doBefore(String className, String methodName,String parameterTypes){
        logger.info("doBefore:"+className+"-->"+methodName+"-->"+parameterTypes);
    }

    public static void doAfter(String className, String methodName,String parameterTypes){
        logger.info("doAfter:"+className+"-->"+methodName+"-->"+parameterTypes);

    }
}
