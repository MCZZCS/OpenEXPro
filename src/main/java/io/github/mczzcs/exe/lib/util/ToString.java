package io.github.mczzcs.exe.lib.util;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.lib.RuntimeMethod;
import io.github.mczzcs.exe.obj.ExClass;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.exe.obj.ExString;

import java.util.List;

public class ToString extends RuntimeMethod {
    ExClass exClass;
    public ToString(ExClass exClass) {
        super("toString",exClass);
        this.exClass = exClass;
    }

    @Override
    public ExObject implMethod(Executor executor, List<ExObject> vars) {
        return new ExString(exClass.getClassName()+":"+exClass.hashCode());
    }

    @Override
    public int getVarNum() {
        return 0;
    }
}
