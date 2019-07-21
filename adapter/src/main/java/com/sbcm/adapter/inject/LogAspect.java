package com.sbcm.adapter.inject;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;


import java.lang.reflect.Method;

//@Aspect注解声明整个类是一个切面
@Aspect

/*
@Configuration用于定义配置类，可替换xml配置文件，被注解的类内部包含有一个或多个被@Bean注解的方法，这些方法将会被
AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，并用于构建bean定义，初始化Spring容器。
*/
@Configuration
public class LogAspect {
    
    /**
     * @Pointcut注解声明一个切入点，@annotation这个指示器匹配那些有指定注解的连接点
     * 意思就是所有带有@AspectLogs注解的方法都会拦截
     */
    @Pointcut("@annotation(com.sbcm.adapter.inject.AspectLogs)")
    public void logsAspect() {
 
    }
 

    /**
     * 声明该方法是一个前置通知：在目标方法开始之前执行，（）内的参数是指定触发的切入点
     *
     * @param joinPoint 切点
     */
    @Before("logsAspect()")
    public void doBefore(JoinPoint joinPoint) {
        try{
            Method method = getControllerMethod(joinPoint);
            String methodDescription = method.getAnnotation(AspectLogs.class).description();
        }catch (Exception e){}
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static Method getControllerMethod(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        Method ret = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    ret = method;
                    break;
                }
            }
        }
        return ret;
    }

}  