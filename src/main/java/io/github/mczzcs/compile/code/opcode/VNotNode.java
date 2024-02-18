package io.github.mczzcs.compile.code.opcode;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.ObjectSize;
import io.github.mczzcs.exe.obj.ExDouble;
import io.github.mczzcs.exe.obj.ExInt;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

public class VNotNode extends OpNode{
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject object = executor.pop();
        object = ObjectSize.getValue(object);

        if(object.getType()==ExObject.STRING||object.getType()==ExObject.BOOLEAN||object.getType()==ExObject.ARRAY)
            throw new VMRuntimeException("The operation type is incorrect.",executor.getThread(),VMRuntimeException.EnumVMException.TYPE_CAST_EXCEPTION);
        else if(object.getType()==ExObject.NULL)
            throw new VMRuntimeException("取反运算发生空指针异常",executor.getThread(), VMRuntimeException.EnumVMException.NULL_PRINT_EXCEPTION);

        if(object.getType()==ExObject.DOUBLE){
            executor.push(new ExDouble(-Double.parseDouble(object.getData())));
        }else if(object.getType()==ExObject.INTEGER){
            executor.push(new ExInt(-Integer.parseInt(object.getData())));
        }

    }
}
