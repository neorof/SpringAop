package com.midea;


import com.midea.aop.MyApoClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopMainClass {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "classpath:applicationContext.xmll" });
        context.start();
        MyApoClass apoClass = context.getBean(MyApoClass.class);
        apoClass.method();
    }
}
