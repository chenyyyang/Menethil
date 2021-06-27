package cn.wow.blizzard.stacktrace;

import cn.wow.blizzard.core.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StackTraceInterceptor implements Interceptor {

    static final Logger logger = LoggerFactory.getLogger(StackTraceInterceptor.class);


    @Override
    public String shortName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void doBeforeConstructor(String className, String methodName, String parameterTypes) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String collectStackStr  = extracted(methodName, stackTrace);
        logger.info(collectStackStr);

    }



    @Override
    public void doBefore(String className, String methodName, String parameterTypes,String classloaderName) {

    }

    @Override
    public void doAfter(String className, String methodName, String parameterTypes,String classloaderName) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String collectStackStr = extracted(methodName, stackTrace);
        StackTraceExecutor.persist(className, methodName,collectStackStr ,classloaderName);
    }

    private String extracted(String methodName, StackTraceElement[] stackTrace) {
        List<StackTraceElement> stackTraceArr = new ArrayList<>(stackTrace.length);
        boolean flag = false;
        for (int i = 0; i < stackTrace.length; i++) {
            String _methodName = stackTrace[i].getMethodName();
            if (methodName.equalsIgnoreCase(_methodName)) {
                flag =true;
            }
            if (flag) {
                stackTraceArr.add(stackTrace[i]);
            }
        }
        String collectStackStr = stackTraceArr.stream().map(elem -> formatLine(elem)).collect(Collectors.joining("\n"));
        return collectStackStr;
    }

    private String formatLine(StackTraceElement elem) {
        return elem.getFileName()+":"+elem.getLineNumber() +"  "
                +elem.getClassName() +"  ::" + elem.getMethodName();
    }

}
