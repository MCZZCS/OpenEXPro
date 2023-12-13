package io.github.mczzcs.util;

import io.github.mczzcs.exe.core.LoaderStackFrame;
import io.github.mczzcs.exe.core.RuntimeStackFrame;
import io.github.mczzcs.exe.core.StackFrame;
import io.github.mczzcs.exe.thread.ThreadTask;
import io.github.mczzcs.Main;

import java.io.PrintStream;

public class VMRuntimeException extends RuntimeException{
    String message;
    ThreadTask task;
    EnumVMException exception;

    public VMRuntimeException(String message, ThreadTask task){
        this.message = message;
        this.task = task;
        this.exception = EnumVMException.VM_ERROR;
    }

    public VMRuntimeException(String message,ThreadTask task,EnumVMException exception){
        this.message =  message;
        this.task = task;
        this.exception = exception;
    }

    public EnumVMException getException() {
        return exception;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        s.println("ScriptRuntimeError: "+message+"\n\t" +
                "ThreadName: "+task.getName()+"\n\t" +
                "FileName: "+task.getFilename()+"\n\t" +
                "Exception: "+exception.name()+"\n\t" +
                "Edition: " +Main.name + "\n\t" +
                "Version: "+ Main.version + "\n" +
                buildCallStack());
    }

    public String buildCallStack(){
        StringBuilder sb = new StringBuilder();
        sb.append("Call stack struct:");
        for(StackFrame stackFrame:task.getCallStack()){
            sb.append("\n\t")
                    .append("at ")
                    .append("<").append(stackFrame.getFunction().getFilename()).append(">: ")
                    .append(stackFrame.getFunction().getLib())
                    .append('.')
                    .append(stackFrame.getFunction().getName())
                    .append('(');
            if(stackFrame instanceof RuntimeStackFrame){
                sb.append("Runtime Function");
            }else if(stackFrame instanceof LoaderStackFrame){
                sb.append("Script Loader");
            }else sb.append("User Script");
            sb.append(')');
        }

        return sb.toString();
    }

    public enum EnumVMException{
        VM_ERROR(false),
        VM_OP_STACK_ERROR(false),
        NULL_PRINT_EXCEPTION(true),
        INDEX_OUT_OF_BOUNDS_EXCEPTION(true),
        ILLEGAL_ACCESS_EXCEPTION(true),
        ILLEGAL_STATE_EXCEPTION(true),
        FILE_IO_EXCEPTION(true),
        NO_SUCH_FUNCTION_EXCEPTION(true),
        TYPE_CAST_EXCEPTION(true),
        NO_SUCH_VALUE_ERROR(false),
        STACK_OVER_FLOW_ERROR(false);

        boolean isCatch;
        EnumVMException(boolean isCatch){
            this.isCatch = isCatch;
        }

        public boolean canCatch(){
            return isCatch;
        }
    }
}
