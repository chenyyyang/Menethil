package cn.wow.blizzard.core;

import java.lang.instrument.Instrumentation;

public class AgentMain {

    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println(agentOps);
        inst.addTransformer(new ClassLoadingEnhancer());
        System.out.println("Instrumentation:"+inst);
    }

    public static void agentmain(String agentOps, Instrumentation inst) {
    }



}
