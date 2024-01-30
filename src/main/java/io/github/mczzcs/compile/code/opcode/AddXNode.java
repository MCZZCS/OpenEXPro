package io.github.mczzcs.compile.code.opcode;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.lib.util.ObjectSize;
import io.github.mczzcs.exe.obj.*;
import io.github.mczzcs.util.VMRuntimeException;

public class AddXNode extends OpNode{
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject t1 = executor.pop();

        ExObject o1 = ObjectSize.getValue(t1);

        if(t1.getType()!=ExObject.VALUE)throw new VMRuntimeException("The operation type is incorrect",executor.getThread(), VMRuntimeException.EnumVMException.TYPE_CAST_EXCEPTION);

        ExValue value = (ExValue) t1;
        if(t1.getType()==ExObject.STRING||value.getVar().getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN|| value.getVar().getType()==ExObject.BOOLEAN){
            throw new VMRuntimeException("The operation type is incorrect",executor.getThread(), VMRuntimeException.EnumVMException.TYPE_CAST_EXCEPTION);
        }
        else if(t1.getType()==ExObject.DOUBLE||value.getVar().getType()==ExObject.DOUBLE){
            value.setVar(new ExDouble(Double.parseDouble(o1.getData())+1));
        }
        else if(t1.getType()==ExObject.NULL)throw new VMRuntimeException("加法运算时发生空指针异常",executor.getThread(), VMRuntimeException.EnumVMException.NULL_PRINT_EXCEPTION);
        else{
            value.setVar(new ExInt(Integer.parseInt(o1.getData())+1));
        }
    }
}
