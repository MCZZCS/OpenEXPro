package io.github.mczzcs.exe.lib;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.VMRuntimeException;
import io.github.mczzcs.exe.obj.*;

import java.util.ArrayList;

public class Array implements RuntimeLibrary{
    ArrayList<RuntimeFunction> functions;
    public Array(){
        functions = new ArrayList<>();
        functions.add(new GetObject());
        functions.add(new SetObject());
        functions.add(new ArrayLength());
        functions.add(new ToString());
    }
    @Override
    public ArrayList<RuntimeFunction> functions() {
        return functions;
    }

    @Override
    public String getName() {
        return "array";
    }

    private static class ToString implements RuntimeFunction{

        @Override
        public int getVarNum() {
            return 1;
        }

        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException {
            ExObject o = vars.get(0);
            if(o.getType()!=ExObject.ARRAY)throw new VMRuntimeException("传入参数类型必须为数组类型",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_ACCESS_EXCEPTION);
            return new ExString(o.toString());
        }

        @Override
        public String getName() {
            return "to_string";
        }
    }

    private static class GetObject implements RuntimeFunction{
        @Override
        public int getVarNum() {
            return 2;
        }
        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException {
            ExObject o = vars.get(0);
            ExObject index = vars.get(1);


            if(o.getType()!=ExObject.ARRAY)throw new VMRuntimeException("传入参数类型必须为数组类型",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_ACCESS_EXCEPTION);
            if(index.getType()!=ExObject.INTEGER)throw new VMRuntimeException("数组索引必须为整数类型",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_ACCESS_EXCEPTION);
            ExArray a = (ExArray) o;

            int i = Integer.parseInt(index.getData());
            if(i >= a.length())throw new VMRuntimeException("数组索引越界,原数组长度为(index:"+a.length()+"),索引为(index:"+i+")",executor.getThread(), VMRuntimeException.EnumVMException.INDEX_OUT_OF_BOUNDS_EXCEPTION);

            return a.getObj(i);
        }
        @Override
        public String getName() {
            return "get_object";
        }
    }
    private static class SetObject implements RuntimeFunction{
        @Override
        public int getVarNum() {
            return 3;
        }
        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException {
            ExObject o = vars.get(0);
            ExObject set = vars.get(1);
            ExObject index = vars.get(2);
            if(o.getType()!=ExObject.ARRAY)throw new VMRuntimeException("传入参数类型必须为数组类型",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_ACCESS_EXCEPTION);
            if(index.getType()!=ExObject.INTEGER)throw new VMRuntimeException("数组索引必须为整数类型",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_ACCESS_EXCEPTION);
            ExArray a = (ExArray) o;

            int i = Integer.parseInt(index.getData());
            if(i >= a.length())throw new VMRuntimeException("数组索引越界,原数组长度为(index:"+a.length()+"),索引为(index:"+i+")",executor.getThread(), VMRuntimeException.EnumVMException.INDEX_OUT_OF_BOUNDS_EXCEPTION);
            a.setObj(i,set);
            return new ExNull();
        }
        @Override
        public String getName() {
            return "set_object";
        }
    }
    private static class ArrayLength implements RuntimeFunction{
        @Override
        public int getVarNum() {
            return 1;
        }
        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException {
            ExObject o = vars.get(0);

            if(o.getType()!=ExObject.ARRAY)throw new VMRuntimeException("传入参数类型必须为数组类型",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_ACCESS_EXCEPTION);
            ExArray a = (ExArray) o;
            return new ExInt(a.length());
        }
        @Override
        public String getName() {
            return "length";
        }
    }
}
