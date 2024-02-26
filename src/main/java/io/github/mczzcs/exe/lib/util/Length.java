package io.github.mczzcs.exe.lib.util;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.lib.RuntimeMethod;
import io.github.mczzcs.exe.obj.ExArray;
import io.github.mczzcs.exe.obj.ExInt;
import io.github.mczzcs.exe.obj.ExObject;

import java.util.List;

public class Length extends RuntimeMethod {
    ExArray array;
    public Length(ExArray array) {
        super("length", array);
        this.array = array;
    }

    @Override
    public ExObject implMethod(Executor executor, List<ExObject> vars) {
        return new ExInt(array.length());
    }

    @Override
    public int getVarNum() {
        return 0;
    }
}
