package com.midea.aop;

import org.springframework.stereotype.Component;

@Component
public class MyAopClassImpl implements MyApoClass {
    @Override
    public void method() {
        System.out.println("我是被代理的方法！");
    }
}
