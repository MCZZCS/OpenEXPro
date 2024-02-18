package io.github.mczzcs.compile.code.opcode;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.ObjectSize;
import io.github.mczzcs.exe.obj.ExBool;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

public class EquNode extends OpNode {
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject obj = ObjectSize.getValue(executor.pop());
        ExObject obj1 = ObjectSize.getValue(executor.pop());

        if(obj.getType()==obj1.getType()){
            executor.push(new ExBool(obj.getData().equals(obj1.getData())));
        }else executor.push(new ExBool(false));
    }
}
