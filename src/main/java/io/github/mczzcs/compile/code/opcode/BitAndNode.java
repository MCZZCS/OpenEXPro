package io.github.mczzcs.compile.code.opcode;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.ObjectSize;
import io.github.mczzcs.exe.obj.ExInt;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

public class BitAndNode extends OpNode{
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject t1 = executor.pop();
        ExObject t2 = executor.pop();

        t1 = ObjectSize.getValue(t1);
        t2 = ObjectSize.getValue(t2);

        if(t1.getType()==ExObject.STRING||t2.getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN||t2.getType()==ExObject.BOOLEAN)throw new VMRuntimeException("The operation type is incorrect.",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_STATE_EXCEPTION);
        else if(t1.getType()==ExObject.DOUBLE||t2.getType()==ExObject.DOUBLE){
            throw new VMRuntimeException("The operation type is incorrect.",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_STATE_EXCEPTION);
        }
        else if(t1.getType()==ExObject.NULL||t2.getType()==ExObject.NULL)throw new VMRuntimeException("位与运算时发生空指针异常",executor.getThread(), VMRuntimeException.EnumVMException.NULL_PRINT_EXCEPTION);
        else{
            executor.push(new ExInt(Integer.parseInt(t2.getData()) & Integer.parseInt(t1.getData())));
        }
    }
}
