package io.github.mczzcs.compile.code.opcode;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.lib.util.ObjectSize;
import io.github.mczzcs.exe.obj.ExBool;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

public class NotNode extends OpNode {
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject obj = executor.pop();
        obj = ObjectSize.getValue(obj);
        if(obj.getType()==ExObject.BOOLEAN){
            executor.push(new ExBool(!Boolean.parseBoolean(obj.getData())));
        }else throw new VMRuntimeException("逻辑运算时提取到未知类型",executor.getThread());
    }
}
