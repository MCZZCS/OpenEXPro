package io.github.mczzcs.exe.lib;

import io.github.mczzcs.exe.obj.DefineMethod;
import io.github.mczzcs.exe.obj.ExClass;

import java.util.ArrayList;
import java.util.List;

public abstract class RuntimeClass extends ExClass {
    public RuntimeClass(String className) {
        super(className);

    }

    @Override
    public List<DefineMethod> getFunctions() {
        List<DefineMethod> methods = new ArrayList<>(super.getFunctions());
        for(RuntimeMethod method:getMethods()){
            methods.add(method);
        }
        return methods;
    }

    public abstract List<RuntimeMethod> getMethods();
}
