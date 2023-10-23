package io.github.mczzcs.exe.lib.util;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.ExArray;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.exe.obj.ExValue;
import io.github.mczzcs.exe.obj.ExVarName;
import io.github.mczzcs.exe.thread.ThreadManager;
import io.github.mczzcs.util.VMRuntimeException;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class ObjectSize {
    public static String getSize(ExObject obj, Executor executor) throws VMRuntimeException {
        return getSize(sizeof(obj,executor));
    }
    private static long sizeof(ExObject object,Executor executor) throws VMRuntimeException {
        long size = 0;
        if(object instanceof ExVarName){
            ExValue buf = null;
            for(ExValue v: ThreadManager.getValues()){
                if(v.getData().equals(object.getData())){
                    buf = v;
                    break;
                }
            }
            for(ExValue v:executor.getThread().getCallStackPeek().getValues()){
                if(v.getData().equals(object.getData())){
                    buf = v;
                    break;
                }
            }
            if(buf == null)throw new VMRuntimeException("找不到指定变量:"+object.getData(),executor.getThread());

            if(buf.getType()==ExObject.ARRAY){
                object = buf;
            }else object = buf.getVar();
        }
        switch (object.getType()){
            case ExObject.INTEGER, ExObject.BOOLEAN -> size = 4;
            case ExObject.DOUBLE -> size = 8;
            case ExObject.NULL -> size = 0;
            case ExObject.STRING -> size = object.getData().getBytes(StandardCharsets.UTF_8).length;
            case ExObject.ARRAY -> {
                for(ExObject object1:((ExArray)object).getObjs()){
                    size += sizeof(object1,executor);
                }
            }
        }
        return size;
    }

    private static String getSize(long size) {
        int GB = 1024 * 1024 * 1024;
        int MB = 1024 * 1024;
        int KB = 1024;
        DecimalFormat df = new DecimalFormat("0.00");
        String resultSize = "";
        if (size / GB >= 1) {
            resultSize = df.format(size / (float) GB) + " GB";
        } else if (size / MB >= 1) {
            resultSize = df.format(size / (float) MB) + " MB";
        } else if (size / KB >= 1) {
            resultSize = df.format(size / (float) KB) + " KB";
        } else {
            resultSize = size + " Bit";
        }
        return resultSize;
    }

    public static ExObject getValue(ExObject o){
        if(o.getType()==ExObject.VALUE){
            ExObject v = ((ExValue)o).getVar();
            if(v.getType()==ExObject.VALUE) return getValue(v);
            else return v;
        }else return o;
    }
}
