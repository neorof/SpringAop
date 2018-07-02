package com.midea;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//不标注对象默认全部类型
//@Target(ElementType.METHOD)
//@Target(ElementType.TYPE)
//@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AopAnnotation {

}

