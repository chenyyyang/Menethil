package cn.wow.blizzard.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class AgentMain {

    static final Logger logger = LoggerFactory.getLogger(AgentMain.class);


    public static void premain(String agentOps, Instrumentation inst) {
        logger.info("[AgentMain]-premain-start ~~");
        inst.addTransformer(new ClassLoadingEnhancer());
        logger.info("[AgentMain]-premain-addTransformer ~~");
    }

    public static void agentmain(String agentOps, Instrumentation inst) {
    }



}
