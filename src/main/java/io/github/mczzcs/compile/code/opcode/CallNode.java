package io.github.mczzcs.compile.code.opcode;

import io.github.mczzcs.exe.core.EXClassLoader;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.core.RuntimeStackFrame;
import io.github.mczzcs.exe.core.StackFrame;
import io.github.mczzcs.exe.lib.Function;
import io.github.mczzcs.exe.lib.RuntimeClass;
import io.github.mczzcs.exe.lib.RuntimeMethod;
import io.github.mczzcs.exe.obj.DefineMethod;
import io.github.mczzcs.exe.obj.ExClass;
import io.github.mczzcs.exe.obj.ExMethod;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.ObjectSize;
import io.github.mczzcs.util.ReturnException;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;

public class CallNode extends OpNode{
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject call_function = executor.pop();
        ExObject call_pointer = executor.pop();

        call_pointer = ObjectSize.getValue(call_pointer);

        if(call_pointer.getType() == ExObject.NULL)
            throw new VMRuntimeException("Cannot invoke method.Because pointer is null.",executor.getThread(), VMRuntimeException.EnumVMException.NULL_PRINT_EXCEPTION);
        if(call_function.getType() != ExObject.METHOD)
            throw new VMRuntimeException("Cannot invoke method,Because not found method.",executor.getThread(), VMRuntimeException.EnumVMException.NO_SUCH_METHOD_EXCEPTION);
        if(call_pointer.getType() == ExObject.ARRAY||call_pointer.getType() == ExObject.STRING){
            try{
                ExMethod call_method = (ExMethod) call_function;
                ExClass call_class = (ExClass) call_pointer;
                String class_name = call_class.getClassName();
                String method_name = call_method.getMethodName();

                for(DefineMethod method:call_class.getFunctions()){
                    if(method.getMethodName().equals(method_name)){

                        if(method instanceof RuntimeMethod){
                            call_method.getVars().executor(executor);
                            method.callMethod(executor);
                            return;
                        }

                        executor.getThread().pushCallStackFrame(new StackFrame(new Function(class_name,method_name,new ArrayList<>())));

                        call_method.getVars().executor(executor);

                        method.callMethod(executor);
                        executor.getThread().popCallStackFrame();
                        return;
                    }
                }
            }catch (ReturnException e){
                executor.getThread().popCallStackFrame();
                executor.push(e.getObj());
            }
            return;
        }
        if(call_pointer.getType() != ExObject.CLASS)
            throw new VMRuntimeException("Cannot invoke "+call_pointer.getData()+"."+call_function.getData()+",Because not found class.",executor.getThread(), VMRuntimeException.EnumVMException.NO_CLASS_DEF_FOUND_EXCEPTION);


        try{
            ExMethod call_method = (ExMethod) call_function;
            ExClass call_class = (ExClass) call_pointer;
            String class_name = call_class.getClassName();
            String method_name = call_method.getMethodName();

            for(RuntimeClass runtimeClass: EXClassLoader.getLibrary()){
                if(runtimeClass.getClassName().equals(class_name)){
                    for(DefineMethod method: runtimeClass.getFunctions()){
                        if(method.getMethodName().equals(method_name)){

                            call_method.getVars().executor(executor);

                            try {
                                method.callMethod(executor);
                            }catch (VMRuntimeException e){
                                executor.getThread().pushCallStackFrame(
                                        new RuntimeStackFrame(new Function(class_name,method_name,new ArrayList<>())));
                                throw e;
                            }
                            return;
                        }
                    }
                    throw new VMRuntimeException("No such method:" + method_name, executor.getThread(), VMRuntimeException.EnumVMException.NO_SUCH_METHOD_EXCEPTION);
                }

                for(DefineMethod method:call_class.getFunctions()){
                    if(method.getMethodName().equals(method_name)){

                        if(method instanceof RuntimeMethod){
                            call_method.getVars().executor(executor);
                            method.callMethod(executor);
                            return;
                        }

                        executor.getThread().pushCallStackFrame(new StackFrame(new Function(class_name,method_name,new ArrayList<>())));

                        call_method.getVars().executor(executor);

                        method.callMethod(executor);
                        executor.getThread().popCallStackFrame();
                        return;
                    }
                }
            }
        }catch (ReturnException e){
            executor.getThread().popCallStackFrame();
            executor.push(e.getObj());
            return;
        }

        throw new VMRuntimeException("No found class:" +call_pointer.getData(), executor.getThread(), VMRuntimeException.EnumVMException.NOT_FOUND_CLASS_EXCEPTION);
    }
}
