package io.github.mczzcs.compile.code.opcode;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.ObjectSize;
import io.github.mczzcs.exe.obj.*;
import io.github.mczzcs.util.VMRuntimeException;

public class AddMovNode extends OpNode{
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject t1 = executor.pop();
        ExObject t2 = executor.pop();

        t1 = ObjectSize.getValue(t1);

        if(t2.getType()!=ExObject.VALUE)throw new VMRuntimeException("The operation type is incorrect",executor.getThread(), VMRuntimeException.EnumVMException.TYPE_CAST_EXCEPTION);

        ExValue value = (ExValue) t2;
        if(t1.getType()==ExObject.STRING||value.getVar().getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN|| value.getVar().getType()==ExObject.BOOLEAN){

            value.setVar(new ExString(((ExValue) t2).getVar()+t1.getData()));
        }
        else if(t1.getType()==ExObject.DOUBLE||value.getVar().getType()==ExObject.DOUBLE){
            value.setVar(new ExDouble(Double.parseDouble(t2.getData())+Double.parseDouble(t1.getData())));
        }
        else if(t1.getType()==ExObject.NULL||((ExValue) t2).getVar().getType()==ExObject.NULL)throw new VMRuntimeException("加法运算时发生空指针异常",executor.getThread(), VMRuntimeException.EnumVMException.NULL_PRINT_EXCEPTION);
        else{
            value.setVar(new ExInt(Integer.parseInt(((ExValue) t2).getVar().getData())+Integer.parseInt(t1.getData())));
        }
    }
}
