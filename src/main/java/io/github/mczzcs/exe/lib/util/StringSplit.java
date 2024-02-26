package io.github.mczzcs.exe.lib.util;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.lib.RuntimeMethod;
import io.github.mczzcs.exe.obj.ExArray;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.exe.obj.ExString;

import java.util.ArrayList;
import java.util.List;

public class StringSplit extends RuntimeMethod {
    ExString string;
    public StringSplit(ExString string) {
        super("split", string);
        this.string = string;
    }

    @Override
    public ExObject implMethod(Executor executor, List<ExObject> vars) {
        ExObject sp = vars.get(1);
        ArrayList<ExObject> o = new ArrayList<>();
        for(String s:string.getData().split(sp.getData())) o.add(new ExString(s));
        return new ExArray("<STRING>",o);
    }

    @Override
    public int getVarNum() {
        return 1;
    }
}
