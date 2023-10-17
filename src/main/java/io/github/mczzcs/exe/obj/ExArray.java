package io.github.mczzcs.exe.obj;

import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;

public class ExArray extends ExValue{
    String name;
    ExValue[] objs;

    public ExArray(String name,int size){
        this.name = name;
        this.objs = new ExValue[size];
    }
    public ExArray(String name, ArrayList<ExObject> objs){
        this.name = name;
        this.objs = new ExValue[objs.size()];
        for (int i = 0; i < this.objs.length; i++) this.objs[i] = new ExValue(objs.get(i));
    }
    public ExArray(String name,ExObject[] objs){
        this.name = name;
        for (int i = 0; i < this.objs.length; i++) this.objs[i] = new ExValue(objs[i]);
    }

    public void setObj(int index,ExObject obj){
        objs[index] = new ExValue( obj);
    }

    public ExObject[] getObjs() {
        return objs;
    }

    public int length(){
        return objs.length;
    }

    public ExObject getObj(int index) throws VMRuntimeException {
        ExValue obj = objs[index];
        return obj;
    }

    @Override
    public ExObject getVar() {
        return this;
    }

    @Override
    public String toString(){
        int iMax = objs.length - 1;
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(objs[i].var);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    @Override
    public String getData() {
        return name;
    }

    @Override
    public int getType() {
        return ExObject.ARRAY;
    }
}
