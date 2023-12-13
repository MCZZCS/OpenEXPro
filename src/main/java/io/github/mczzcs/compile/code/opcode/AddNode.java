package io.github.mczzcs.compile.code.opcode;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.lib.util.ObjectSize;
import io.github.mczzcs.util.VMRuntimeException;
import io.github.mczzcs.exe.obj.ExDouble;
import io.github.mczzcs.exe.obj.ExInt;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.exe.obj.ExString;

public class AddNode extends OpNode {

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject t1 = executor.pop();
        ExObject t2 = executor.pop();

        t1 = ObjectSize.getValue(t1);
        t2 = ObjectSize.getValue(t2);

        if(t1.getType()==ExObject.STRING||t2.getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN||t2.getType()==ExObject.BOOLEAN){
            executor.push(new ExString(t2.getData()+t1.getData()));
        }
        else if(t1.getType()==ExObject.DOUBLE||t2.getType()==ExObject.DOUBLE){
            executor.push(new ExDouble(Double.parseDouble(t2.getData())+Double.parseDouble(t1.getData())));
        }
        else if(t1.getType()==ExObject.NULL||t2.getType()==ExObject.NULL)throw new VMRuntimeException("加法运算时发生空指针异常",executor.getThread());
        else{
            executor.push(new ExInt(Integer.parseInt(t2.getData())+Integer.parseInt(t1.getData())));
        }
    }
}
