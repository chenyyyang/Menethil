package cn.wow.blizzard.custom;

import cn.wow.blizzard.core.InterceptorsDeposit;

import java.util.HashSet;

public class CustomParameters {

   public static HashSet<String> INCLUDE_PACKAGES = new HashSet<>(1);

   static {
     /* 2021-06-26 22:18:57 INFO  [main] InterceptorsDeposit:11 - doBefore:real.world.tools.zkClient.ZKClientBuildereventThreadPoolSize
      2021-06-26 22:18:57 INFO  [main] InterceptorsDeposit:15 - doAfter:real.world.tools.zkClient.ZKClientBuildereventThreadPoolSize
      2021-06-26 22:18:57 INFO  [main] InterceptorsDeposit:11 - doBefore:real.world.tools.zkClient.ZKClientBuilderbuild
      2021-06-26 22:18:57 INFO  [main] InterceptorsDeposit:11 - doBefore:real.world.tools.zkClient.connection.ZKConnectionImplisZkSaslEnabled
      2021-06-26 22:18:57 INFO  [main] InterceptorsDeposit:15 - doAfter:real.world.tools.zkClient.connection.ZKConnectionImplisZkSaslEnabled
      2021-06-26 22:18:57 INFO  [main] InterceptorsDeposit:11 - doBefore:real.world.tools.zkClient.ZKClientgetEventThreadPoolSize
   */
      INCLUDE_PACKAGES.add("real.world");
   }

}

