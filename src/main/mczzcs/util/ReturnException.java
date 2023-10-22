package io.github.mczzcs.util;

import io.github.mczzcs.exe.obj.ExObject;

public class ReturnException extends RuntimeException{
    ExObject obj;
    public ReturnException(ExObject obj){
        this.obj = obj;
    }

    public ExObject getObj() {
        return obj;
    }
}
