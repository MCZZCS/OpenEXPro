package io.github.mczzcs.exe.code.struct;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.lib.Function;
import io.github.mczzcs.exe.lib.RuntimeLibrary;
import io.github.mczzcs.exe.thread.ThreadManager;
import io.github.mczzcs.util.ReturnException;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;

public class InvokeASTNode extends StructNode {
    String lib,function;
    ArrayList<ASTNode> var;

    public InvokeASTNode(String lib, String function, ArrayList<ASTNode> var){
        this.lib = lib;
        this.function = function;
        this.var = var;
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

                            rf.exe(executor);
                            return;
                        }
                    }
                    throw new VMRuntimeException("找不到指定方法:" + function, executor.getThread());
                }
            }
            for (Function function1 : ThreadManager.getFunctions()) {
                if (function1.getLib().equals(lib)) {
                    if (function1.getName().equals(function)) {
                        for (ASTNode bc : var) bc.executor(executor);
                        for (ASTNode bc : function1.getBcs()) {
                            bc.executor(executor);
                        }
                        return;
                    }
                }
            }
        }catch (ReturnException e){
            executor.push(e.getObj());
            return;
        }

        throw new VMRuntimeException("No such function:" +lib+"."+ function, executor.getThread());
    }
}
