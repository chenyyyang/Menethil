# Menethil

## 概述
当我们阅读一个SDK的源码的时候，经常想分析源码中的调用轨迹，从哪个方法进入SDK，然后如何一步步的调用到目标方法。Menethil是一个对目标方法调用链上下文记录和分析的小工具。  
利用JVM自带的Instrument+javassist字节码增强，在方法的执行前后插入拦截器的调用方法，拦截后处理数据插入Mysql。阿里的[arthas](https://github.com/alibaba/arthas)功能全面强大，本工具主打的遍是简单，代码量少，改源码实现其他功能也非常容易


### 打包

```
环境： jdk8+    Mysql8.0+

1.git clone 
2.用IDEA打开项目
3.打开cn.wow.blizzard.custom.GlobalConfiguration
    修改mysql 连接信息（只需修改用户名和密码，会自动创建db和table）
4.INCLUDE_PACKAGES.add("real.world");增加需要增强的类所在的包的包名
5.ClassLoadingInterceptorsDeposit.InterceptorsDeposit.add(new StackTraceInterceptor());
    增加自定义的拦截器，这边已经实现了一个堆栈记录功能的的拦截器。
6.mvn package
    得到一个Menethil-1.0-SNAPSHOT-jar-with-dependencies.jar

```

### 集成
如果是IDEA(2021.1.2版本)，打开目标工程，我这里打开的是一个分布式任务调度项目Westworld


![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/1c106124e29d4670bd26960787030a82~tplv-k3u1fbpfcp-watermark.image)
1. Edit Run Configurations
2. Modify options --> Add VM options
3. 输入
 ```
-javaagent:/Users/{省略}/Menethil/target/Menethil-1.0-SNAPSHOT-jar-with-dependencies.jar
```
4.javaagent添加完成
5.启动项目，去本地DB查看效果

### 分析
数据格式概览

![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/9c99770a08584dc68778b66c6a637958~tplv-k3u1fbpfcp-watermark.image)
以121 行数据举例子：  
表示main线程的在2021-06-27T15:14:49.756的堆栈，深度是17，类加载器是AppClassLoader
stackCrc主要用来去重。查看stacktrace的堆栈细节
```
    类文件名：行号        类完整名称  ::方法名称
ZKClient.java:661  real.world.tools.zkClient.ZKClient$3  ::call
ZKClient.java:658  real.world.tools.zkClient.ZKClient$3  ::call
ZKConnectionImpl.java:137  real.world.tools.zkClient.connection.ZKConnectionImpl  ::retryUntilConnected
ZKClient.java:1105  real.world.tools.zkClient.ZKClient  ::retryUntilConnected
ZKClient.java:658  real.world.tools.zkClient.ZKClient  ::exists
ZKClient.java:634  real.world.tools.zkClient.ZKClient$2  ::call
ZKClient.java:630  real.world.tools.zkClient.ZKClient$2  ::call
ZKConnectionImpl.java:137  real.world.tools.zkClient.connection.ZKConnectionImpl  ::retryUntilConnected
ZKClient.java:1105  real.world.tools.zkClient.ZKClient  ::retryUntilConnected
ZKClient.java:630  real.world.tools.zkClient.ZKClient  ::watchForChilds
ZKClient.java:335  real.world.tools.zkClient.ZKClient  ::listenChildDataChanges
VillageOfficial.java:55  real.world.government.VillageOfficial  ::lookupPopulation
StateGovernment.java:36  real.world.government.StateGovernment  ::buildNation
StateGovernment.java:26  real.world.government.StateGovernment  ::<init>
StateGovernment.java:44  real.world.government.StateGovernment  ::get
Westworld.java:38  real.world.Westworld  ::case1
Westworld.java:21  real.world.Westworld  ::main

```

可以看到是main线程从main方法为入口，调用了case1方法，然后调用了StateGovernment.get方法...
