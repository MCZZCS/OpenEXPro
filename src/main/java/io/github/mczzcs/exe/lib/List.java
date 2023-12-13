package io.github.mczzcs.exe.lib;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;

public class List implements RuntimeLibrary{
    ArrayList<RuntimeFunction> functions;

    public List(){
        this.functions = new ArrayList<>();
        functions.add(new Add());
    }
    @Override
    public ArrayList<RuntimeFunction> functions() {
        return functions;
    }

    @Override
    public String getName() {
        return "list";
    }

    private static class Add implements RuntimeFunction{

        @Override
        public int getVarNum() {
            return 2;
        }

        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException {
            return null;
        }

        @Override
        public String getName() {
            return "add";
        }
    }
}
