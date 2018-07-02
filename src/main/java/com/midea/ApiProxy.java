package com.midea;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class ApiProxy {

    //定义返回任意类型  这个路径下的任意方法 方法任意参数类型
    //第一个*号：表示返回类型，*号表示所有的类型。
    //包名：表示需要拦截的包名，后面的两个句点表示当前包和当前包的所有子包,子孙包下所有类的方法。
    //第二个*号：表示类名，*号表示所有的类
    //*(..):最后这个星号表示方法名，*号表示所有的方法，后面括弧里面表示方法的参数，两个句点表示任何参数
    @Around("execution(* com.midea.aop..*.*(..))") //切点
    //环绕通知逻辑(切面)
    public void doSomethingAround(ProceedingJoinPoint point) throws Throwable{
        String targetMethodName = point.getSignature().getName();
        Class<?> classType = point.getTarget().getClass();
        if (isTagged(classType,targetMethodName)) {
            System.out.println("Tagged! and can do something in this place!");
            point.proceed();  //执行被切方法
            System.out.println("Tagged! and can do something in this place!");
        } else {
            point.proceed();  //执行被切方法
        }
    }

    //扫描父类是否被打上标签,或者父类中的这个方法是否被打伤标签
    private boolean isTagged(Class invokeClass, String methodName) {
        if (isTaggerInInterface(invokeClass, methodName)) {
            return true;
        }
        if (!invokeClass.equals(Object.class)) { //Object没有Superclass  用来跳出递归
            return isTaggedInClass(invokeClass, methodName) || isTagged(invokeClass.getSuperclass(), methodName);
        }
        return false;
    }

    //扫描当前类的接口
    private boolean isTaggerInInterface(Class invokeClass, String methodName) {
        Class[] interfaces = invokeClass.getInterfaces();
        boolean isTaggerInInterface = false;
        for (Class cas : interfaces) {
            isTaggerInInterface = isTaggerInInterface || isTaggedInClass(cas, methodName) || isTaggerInInterface(cas, methodName);
        }
        return isTaggerInInterface;
    }

    //方法名为signatureName的方法tagged有两种情况:方法本身被taged或者方法所在的类被taged
    private boolean isTaggedInClass(Class clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        boolean isTaggedInClass = false;
        for (Method method : methods) {
            isTaggedInClass = isTaggedInClass || (isMethodWithName(method, methodName) && isMethodTagged(method))
                    || (isMethodWithName(method, methodName) && isClassTagged(clazz));
        }
        return isTaggedInClass;
    }


    private boolean isClassTagged(Class invokeClass) {
        return invokeClass.getAnnotation(AopAnnotation.class) != null;
    }

    private boolean isMethodTagged(Method method) {
        return method.getAnnotation(AopAnnotation.class) != null;
    }

    private boolean isMethodWithName(Method method, String name) {
        return method.getName().equals(name);
    }
}
