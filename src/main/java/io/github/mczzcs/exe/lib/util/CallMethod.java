package io.github.mczzcs.exe.lib.util;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.core.StackFrame;
import io.github.mczzcs.exe.lib.Function;
import io.github.mczzcs.exe.lib.RuntimeMethod;
import io.github.mczzcs.exe.obj.DefineMethod;
import io.github.mczzcs.exe.obj.ExClass;
import io.github.mczzcs.exe.obj.ExNull;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.ReturnException;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;

public class CallMethod {
    public static ExObject call(ExClass call_class, String method_name, Executor executor, ExObject... args){
        try {
            for (DefineMethod method : call_class.getFunctions()) {
                if (method.getMethodName().equals(method_name)) {

                    if (method instanceof RuntimeMethod) {
                        for (ExObject arg : args) executor.push(arg);
                        method.callMethod(executor);
                        return executor.peek();
                    }

                    executor.getThread().pushCallStackFrame(new StackFrame(new Function(call_class.getClassName(),
                            method_name, new ArrayList<>(),executor.getThread().getFilename())));

                    for (ExObject arg : args) executor.push(arg);

                    method.callMethod(executor);
                    executor.getThread().popCallStackFrame();
                    return new ExNull();
                }
            }
        }catch (ReturnException e){
            executor.getThread().popCallStackFrame();
            return e.getObj();
        }

        throw new VMRuntimeException("No such method:" + method_name, executor.getThread(), VMRuntimeException.EnumVMException.NO_SUCH_METHOD_EXCEPTION);
    }
}
