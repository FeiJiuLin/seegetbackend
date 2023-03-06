package com.xmap.v04.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
    public static final String REGEX_EMAIL =
            "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";//邮箱

    public static final String REGEX_MOBILE =
            "^((13[0-9])|(15[^4,\\D])|(16[0-9])|(18[0-9])|(17[0-9]))\\d{8}$";//手机号

    /**
     * 邮箱检验
     *
     * */
    public  static  boolean isEmail(String email){
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 判断手机号是否正确
     *
     *
     * */
    public  static  boolean isPhone(String tel){
        return Pattern.matches(REGEX_MOBILE, tel);
    }


}
