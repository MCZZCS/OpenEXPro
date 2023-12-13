package io.github.mczzcs.compile.code.struct;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.core.RuntimeStackFrame;
import io.github.mczzcs.exe.core.StackFrame;
import io.github.mczzcs.exe.lib.Function;
import io.github.mczzcs.exe.lib.RuntimeLibrary;
import io.github.mczzcs.exe.thread.ThreadManager;
import io.github.mczzcs.util.ReturnException;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class InvokeASTNode extends StructNode {
    String lib,function;
    List<ASTNode> var;

    public InvokeASTNode(String lib, String function, List<ASTNode> var){
        this.lib = lib;
        this.function = function;
        this.var = var;
    }

    public String getLib() {
        return lib;
    }

    public String getFunction() {
        return function;
    }

    public List<ASTNode> getVar() {
        return var;
    }

    @Override
    public String toString() {
        return "Invoke-Path:"+lib+"/"+function+
                "|Value:"+var;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {

        try {
            for (RuntimeLibrary rl : executor.getLibrary()) {
                if (rl.getName().equals(lib)) {
                    for (RuntimeLibrary.RuntimeFunction rf : rl.functions()) {
                        if (rf.getName().equals(function)) {
                            for (ASTNode bc : var) {
                                bc.executor(executor);
                            }

                            try {
                                rf.exe(executor);
                            }catch (VMRuntimeException e){
                                executor.getThread().pushCallStackFrame(
                                        new RuntimeStackFrame(
                                                new Function(lib,function,new ArrayList<>(),
                                                        executor.getThread()
                                                                .getCallStackPeek()
                                                                .getFunction()
                                                                .getFilename())));
                                throw e;
                            }
                            return;
                        }
                    }
                    throw new VMRuntimeException("No such function:" + function, executor.getThread(), VMRuntimeException.EnumVMException.NO_SUCH_FUNCTION_EXCEPTION);
                }
            }
            for (Function function1 : ThreadManager.getFunctions()) {
                if (function1.getLib().equals(lib)) {
                    if (function1.getName().equals(function)) {
                        executor.getThread().pushCallStackFrame(new StackFrame(function1));
                        for (ASTNode bc : var) bc.executor(executor);
                        for (ASTNode bc : function1.getBcs()) {
                            bc.executor(executor);
                        }
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

        throw new VMRuntimeException("No such function:" +lib+"."+ function, executor.getThread(), VMRuntimeException.EnumVMException.NO_SUCH_FUNCTION_EXCEPTION);
    }
}
