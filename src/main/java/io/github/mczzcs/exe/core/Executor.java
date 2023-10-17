package io.github.mczzcs.exe.core;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.lib.RuntimeLibrary;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.exe.thread.ThreadTask;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Executor {

    Stack<ExObject> stack = new Stack<>();
    ArrayList<RuntimeLibrary> rls;
    ThreadTask thread;
    Script executing;

    public List<ExObject> getStackList(){
        return stack.stream().toList();
    }

    public String getStack() {
        return stack.toString();
    }

    public Script getExecuting() {
        return executing;
    }

    public ThreadTask getThread() {
        return thread;
    }

    public void push(ExObject obj){
        stack.push(obj);
    }
    public ExObject pop(){
        return stack.pop();
    }
    public ExObject peek(){
        return stack.peek();
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
