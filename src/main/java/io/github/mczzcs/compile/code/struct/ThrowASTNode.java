package io.github.mczzcs.compile.code.struct;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

public class ThrowASTNode extends StructNode{
    ASTNode message;
    VMRuntimeException.EnumVMException type;

    public ThrowASTNode(ASTNode message,VMRuntimeException.EnumVMException type){
        this.message = message;
        this.type = type;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        message.executor(executor);
        ExObject obj = executor.pop();

        if(obj.getType()==ExObject.ARRAY) {
            throw new VMRuntimeException("The 'message' parameter type does not match, it should be of a not [ARRAY] type.",executor.getThread(), VMRuntimeException.EnumVMException.TYPE_CAST_EXCEPTION);
        }

        throw new VMRuntimeException(obj.getData(), executor.getThread(),type);
    }
}
