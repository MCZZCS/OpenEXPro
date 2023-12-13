package io.github.mczzcs.exe.core;

import io.github.mczzcs.exe.lib.Function;
import io.github.mczzcs.exe.obj.ExValue;

import java.util.ArrayList;
import java.util.List;

public class LoaderStackFrame extends StackFrame{
    Executor executor;

    public LoaderStackFrame(Executor executor) {
        super(new Function("loader","boot",new ArrayList<>(),executor.getExecuting().filename));
        this.executor = executor;
    }

    @Override
    public List<ExValue> getValues() {
        return executor.getExecuting().getValues();
    }
}
