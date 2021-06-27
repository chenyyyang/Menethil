package cn.wow.blizzard.stacktrace;

import cn.wow.blizzard.core.ClassLoadingInterceptorsDeposit;
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
        List<StackTraceElement> stackTraceArr = extracted(methodName, stackTrace);
        logger.info(stackTraceArr.toString());

    }


    private List<StackTraceElement> extracted(String methodName, StackTraceElement[] stackTrace) {
        List<StackTraceElement> StackTraceArr = new ArrayList<>(stackTrace.length);
        boolean flag = false;
        for (int i = 0; i < stackTrace.length; i++) {
            String _methodName = stackTrace[i].getMethodName();
            if (methodName.equalsIgnoreCase(_methodName)) {
                flag =true;
            }
            if (flag) {
                StackTraceArr.add(stackTrace[i]);
            }
        }
        return StackTraceArr;
    }

    @Override
    public void doBefore(String className, String methodName, String parameterTypes) {

    }

    @Override
    public void doAfter(String className, String methodName, String parameterTypes) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        List<StackTraceElement> stackTraceList = extracted(methodName, stackTrace);
        logger.info(stackTraceList.stream().map(elem->elem.getClassName()+"."+elem.getMethodName()).collect(Collectors.joining("\n")));
    }
}
