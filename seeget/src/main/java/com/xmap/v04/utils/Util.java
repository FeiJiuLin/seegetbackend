package com.xmap.v04.utils;

import java.sql.Timestamp;
import java.util.Date;

public class Util {

    public Util() {
    }

    public static Timestamp getTime() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public static long getLongTime() {
        Date date = new Date();
        return date.getTime();
    }
}
