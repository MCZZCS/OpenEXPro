package io.github.mczzcs.exe.obj;

import io.github.mczzcs.exe.lib.RuntimeClass;
import io.github.mczzcs.exe.lib.RuntimeMethod;
import io.github.mczzcs.exe.lib.util.Length;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class ExArray extends RuntimeClass {
    String name;
    ExValue[] objs;
    List<RuntimeMethod> methods;

    public ExArray(String name,int size){
        super("array");
        this.name = name;
        this.objs = new ExValue[size];
        this.methods = new ArrayList<>();
        this.methods.add(new Length(this));
    }
    public ExArray(String name, List<ExObject> objs){
        super("array");
        this.name = name;
        this.objs = new ExValue[objs.size()];
        for (int i = 0; i < this.objs.length; i++) this.objs[i] = new ExValue(objs.get(i));
        this.methods = new ArrayList<>();
        this.methods.add(new Length(this));
    }
    public ExArray(String name,ExObject[] objs){
        super("array");
        this.name = name;
        for (int i = 0; i < this.objs.length; i++) this.objs[i] = new ExValue(objs[i]);
        this.methods = new ArrayList<>();
        this.methods.add(new Length(this));
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

    @Override
    public List<RuntimeMethod> getMethods() {
        return methods;
    }
}
