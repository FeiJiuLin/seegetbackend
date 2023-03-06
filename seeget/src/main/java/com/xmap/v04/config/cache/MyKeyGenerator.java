//package com.xmap.v04.config.cache;
//
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//import java.util.Arrays;
//
///**
// * 自定义缓存key
// **/
//@Component
//public class MyKeyGenerator implements KeyGenerator {
//
//    @Override
//    public String generate(Object target, Method method, Object... params) {
//        return Arrays.toString(params);
//    }
//}
//
