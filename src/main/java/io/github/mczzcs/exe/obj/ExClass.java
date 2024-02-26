package io.github.mczzcs.exe.obj;

import io.github.mczzcs.exe.lib.util.ToString;

import java.util.LinkedList;
import java.util.List;

public class ExClass extends ExValue{
    List<ExClass> vtable;
    List<DefineMethod> functions;
    List<ExValue> values;
    String className;

    public ExClass(String className){
        this.className = className;
        this.functions = new LinkedList<>();
        this.values = new LinkedList<>();
        this.vtable = new LinkedList<>();
        this.functions.add(new ToString(this));
    }

    public List<DefineMethod> getFunctions() {
        return functions;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String getData() {
        return "Class: "+className;
    }

    @Override
    public int getType() {
        return CLASS;
    }

    @Override
    public String toString() {
        return "Class: "+className;
    }
}
