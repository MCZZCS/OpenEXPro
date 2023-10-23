package io.github.mczzcs.exe.thread;

import io.github.mczzcs.Main;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.core.LoaderStackFrame;
import io.github.mczzcs.exe.core.Script;
import io.github.mczzcs.exe.core.StackFrame;
import io.github.mczzcs.exe.lib.Function;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class ThreadTask {
    Executor executor;
    String name;
    Thread thread;
    ThreadManager.Status status;
    ArrayList<Script> scripts;
    Function function;
    Stack<StackFrame> stackFrames;

    public ThreadTask(String name){
        this.name = name;
        this.scripts = new ArrayList<>();
        this.stackFrames = new Stack<>();
        this.thread = new Thread(() ->{
            try{
                stackFrames.push(new LoaderStackFrame(executor));
                executor.launch();
            }catch (VMRuntimeException e){
                status = ThreadManager.Status.ERROR;
                e.printStackTrace();
            }catch (EmptyStackException e){
                status = ThreadManager.Status.ERROR;
                throw new VMRuntimeException("操作栈出栈异常", executor.getThread(), VMRuntimeException.EnumVMException.VM_OP_STACK_ERROR);
            }
        });
        this.thread.setName("OpenEX-Executor-"+name);
        this.status = ThreadManager.Status.LOADING;
    }

    public String getFilename(){
        return executor.getExecuting().getFilename();
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void setStatus(ThreadManager.Status status) {
        this.status = status;
    }

    public void addScripts(Script scripts) {
        this.scripts.add(scripts);
    }

    public StackFrame getCallStackPeek(){
        return stackFrames.peek();
    }

    public Stack<StackFrame> getCallStack() {
        return stackFrames;
    }

    public void pushCallStackFrame(StackFrame stackFrame){
        stackFrame.getValues().addAll(this.stackFrames.peek().getValues());
        this.stackFrames.push(stackFrame);
    }

    public void popCallStackFrame(){
        this.stackFrames.pop();
    }

    public String getName() {
        return name;
    }

    public void start(){
        if(function==null) {
            this.executor = new Executor(scripts.get(0), this);
            this.status = ThreadManager.Status.RUNNING;
            this.thread.start();
        }else {
            this.status = ThreadManager.Status.RUNNING;
            this.executor = new Executor(new Script(function.getLib(), function.getLib()+".exf",function.getBcs()),this);
            this.thread.start();
        }
    }
}
