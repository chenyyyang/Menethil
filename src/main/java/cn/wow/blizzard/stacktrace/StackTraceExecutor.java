package cn.wow.blizzard.stacktrace;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StackTraceExecutor {

   static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 100,
            TimeUnit.HOURS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy());

}
