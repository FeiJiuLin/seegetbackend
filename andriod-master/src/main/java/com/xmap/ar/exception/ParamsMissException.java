package com.xmap.ar.exception;

import java.util.ArrayList;

public class ParamsMissException extends Exception{
    public ParamsMissException(String message) {
        super(message + " is needed.");
    }

    public ParamsMissException(ArrayList<String> params) {
        super(String.join(",", params) + "is needed.");
    }
}