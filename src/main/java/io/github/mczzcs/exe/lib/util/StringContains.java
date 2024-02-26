package io.github.mczzcs.exe.lib.util;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.lib.RuntimeMethod;
import io.github.mczzcs.exe.obj.ExBool;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.exe.obj.ExString;

import java.util.List;

public class StringContains extends RuntimeMethod {
    ExString string;
    public StringContains(ExString string) {
        super("contains", string);
        this.string = string;
    }

    @Override
    public ExObject implMethod(Executor executor, List<ExObject> vars) {
        ExObject sp = vars.get(1);
        return new ExBool(string.getData().contains(sp.getData()));
    }

    @Override
    public int getVarNum() {
        return 1;
    }
}
