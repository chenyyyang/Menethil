package cn.wow.blizzard.core;

import cn.wow.blizzard.custom.CustomParameters;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;

public class ClassLoadingEnhancer implements ClassFileTransformer {

   static final Logger logger = LoggerFactory.getLogger(ClassLoadingEnhancer.class);

    @Override
    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        className = className.replace('/', '.');

        if (!packagePathFilter(className)) {
            return classfileBuffer;
        }

        if (null == loader) {
            loader = loader == null ? Thread.currentThread().getContextClassLoader() : loader;
        }
        return doEnhance(loader, className, classfileBuffer);
    }

    private byte[] doEnhance(ClassLoader loader, String className, byte[] byteCode) {
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = null;
            try {
                ctClass = classPool.get(className);
            } catch (NotFoundException e) {
                classPool.insertClassPath(new LoaderClassPath(loader));
                ctClass = classPool.get(className);
            }

            if (null == ctClass) {
                return byteCode;
            }

            if (!ctClass.isInterface()) {

                CtConstructor[] constructors = ctClass.getConstructors();

                CtMethod[] methods = ctClass.getDeclaredMethods();
                if (null != methods && methods.length > 0) {

                    for (CtMethod ctMethod : methods) {
                        methodAop(className,ctMethod);
                    }
                    byteCode = ctClass.toBytecode();
                }
            }
            ctClass.detach();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return byteCode;
    }

    private static void methodAop(String className, CtMethod ctMethod) throws CannotCompileException {
        if (null == ctMethod || ctMethod.isEmpty()) {
            return;
        }
        boolean isMethodStatic = Modifier.isStatic(ctMethod.getModifiers());
        String aopClassName = isMethodStatic ? "\"" + className + "\"" : "this.getClass().getName()";

        Class<InterceptorsDeposit> interceptorsDepositClass = InterceptorsDeposit.class;

        ctMethod.insertBefore(interceptorsDepositClass.getName() + ".doBefore"+
                "(" + aopClassName + ",\"" + ctMethod.getName()+ "\");");
        ctMethod.insertAfter(interceptorsDepositClass.getName() + ".doAfter"+
                "(" + aopClassName + ",\"" + ctMethod.getName()+ "\");");
    }

    private boolean packagePathFilter(String className) {
        // do not transform the agent class,prevent deadlock
        if (className.startsWith(InternalParameters.INTERNAL_PACKAGE_NAME)) {
            return false;
        }

        Set<String> includes = CustomParameters.INCLUDE_PACKAGES;

        if (null == includes || includes.isEmpty()) {
            return false;
        }

        boolean doTransformFlag = false;
        // include package
        for (String packageName : includes) {
            if (className.startsWith(packageName)) {
                doTransformFlag = true;
                break;
            }
        }
        return doTransformFlag;
    }

}
