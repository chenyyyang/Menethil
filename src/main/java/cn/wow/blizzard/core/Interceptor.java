package cn.wow.blizzard.core;

public interface Interceptor {

     String shortName();

     void doBeforeConstructor(String className, String methodName,String parameterTypes);

     void doBefore(String className, String methodName,String parameterTypes,String loader);

     void doAfter(String className, String methodName,String parameterTypes,String loader);
}
