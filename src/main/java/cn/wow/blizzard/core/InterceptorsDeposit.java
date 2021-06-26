package cn.wow.blizzard.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterceptorsDeposit {

    static final Logger logger = LoggerFactory.getLogger(InterceptorsDeposit.class);

    public static void doBefore(String className, String methodName){
        logger.info("doBefore:"+className+methodName);
    }

    public static void doAfter(String className, String methodName){
        logger.info("doAfter:"+className+methodName);

    }
}
