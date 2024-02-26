package io.github.mczzcs.exe.lib.util;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.lib.RuntimeMethod;
import io.github.mczzcs.exe.obj.ExInt;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.exe.obj.ExString;

import java.util.List;

public class StringIndexOf extends RuntimeMethod {
    ExString string;
    public StringIndexOf(ExString string) {
        super("indexOf", string);
        this.string = string;
    }

    @Override
    public ExObject implMethod(Executor executor, List<ExObject> vars) {
        ExObject sp = vars.get(1);
        return new ExInt(string.getData().indexOf(sp.getData()));
    }

    @Override
    public int getVarNum() {
        return 1;
    }
}
