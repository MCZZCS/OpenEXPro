package io.github.mczzcs.exe.lib;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.DefineMethod;
import io.github.mczzcs.exe.obj.ExClass;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.ObjectSize;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;

public abstract class RuntimeMethod extends DefineMethod {
    String class_name;
    public RuntimeMethod(String method_name,RuntimeClass runtimeClass) {
        super(method_name);
        this.class_name = runtimeClass.getClassName();
    }

    public RuntimeMethod(String method_name, ExClass exClass){
        super(method_name);
        this.class_name = exClass.getClassName();

    }

    public void callMethod(Executor executor){
        try {
            ArrayList<ExObject> obj = new ArrayList<>();

            for (int i = 0; i < getVarNum(); i++){
                ExObject o = executor.pop();

                o = ObjectSize.getValue(o);

                obj.add(o);
            }
            Collections.reverse(obj);

            executor.push(implMethod(executor,obj));
        }catch (EmptyStackException e){
            e.printStackTrace();
            throw new VMRuntimeException("获取函数形参时发生错误,可能传入参数个数不匹配,实际参数为("+getVarNum()+"个)",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_ACCESS_EXCEPTION);
        }
    }

    public String getClassName() {
        return class_name;
    }

    @Override
    public String toString() {
        return class_name+"."+getMethodName();
    }

    public abstract ExObject implMethod(Executor executor, List<ExObject> vars);

    public abstract int getVarNum();
}
