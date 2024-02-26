package io.github.mczzcs.exe.obj;

import io.github.mczzcs.exe.lib.RuntimeClass;
import io.github.mczzcs.exe.lib.RuntimeMethod;
import io.github.mczzcs.exe.lib.util.StringContains;
import io.github.mczzcs.exe.lib.util.StringIndexOf;
import io.github.mczzcs.exe.lib.util.StringSplit;

import java.util.ArrayList;
import java.util.List;

public class ExString extends RuntimeClass {
    String data;
    List<RuntimeMethod> methods;
    public ExString(String data){
        super("string");
        this.data = data;
        this.methods = new ArrayList<>();
        this.methods.add(new StringContains(this));
        this.methods.add(new StringIndexOf(this));
        this.methods.add(new StringSplit(this));
    }

    @Override
    public List<RuntimeMethod> getMethods() {
        return methods;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public int getType() {
        return ExObject.STRING;
    }

    @Override
    public String toString() {
        return "[STRING]:"+data;
    }
}
