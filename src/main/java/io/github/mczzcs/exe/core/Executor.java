package io.github.mczzcs.exe.core;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.exe.lib.RuntimeLibrary;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.exe.thread.ThreadTask;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class Executor {

    ArrayList<RuntimeLibrary> rls;
    ThreadTask thread;
    Script executing;

    public Script getExecuting() {
        return executing;
    }

    public ThreadTask getThread() {
        return thread;
    }

    public void push(ExObject obj){
        getThread().getCallStackPeek().push(obj);
    }
    public ExObject pop(){
        return getThread().getCallStackPeek().pop();
    }
    public ExObject peek(){
        return getThread().getCallStackPeek().op_stack.peek();
    }

    public ArrayList<RuntimeLibrary> getLibrary() {
        return rls;
    }

    public Executor(Script script, ThreadTask thread){
        this.executing = script;
        this.thread = thread;
        this.rls = LibraryLoader.getLibs();
    }

    public void launch() throws VMRuntimeException {
        for(ASTNode bc: executing.bcs){
            bc.executor(this);
        }
    }
}
