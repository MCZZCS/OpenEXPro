package io.github.mczzcs.compile.code.struct;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.core.StackFrame;
import io.github.mczzcs.exe.obj.ExArray;
import io.github.mczzcs.exe.obj.ExString;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.LinkedList;

public class ExceptionASTNode extends StructNode{
    GroupASTNode try_codes,catch_codes;
    VMRuntimeException.EnumVMException exception;

    public ExceptionASTNode(GroupASTNode try_codes, GroupASTNode catch_codes, VMRuntimeException.EnumVMException exception){
        this.try_codes = try_codes;
        this.catch_codes = catch_codes;
        this.exception = exception;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        try{
            try_codes.executor(executor);
        }catch (VMRuntimeException e){
            if(e.getException().canCatch()){
                if(e.getException().equals(exception)){
                    executor.push(new ExArray("<stack_info>",new LinkedList<>(){
                        {
                            for(StackFrame frame:executor.getThread().getCallStack()){
                                add(new ExString(frame.getFunction().getName()));
                            }
                        }
                    }));
                    executor.push(new ExString(e.getMessage()));
                    catch_codes.executor(executor);
                    return;
                }
            }
            throw e;
        }
    }
}
