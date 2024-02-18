package io.github.mczzcs.compile.code.opcode;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.ObjectSize;
import io.github.mczzcs.exe.obj.ExBool;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

public class BigNode extends OpNode {
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        ExObject obj = ObjectSize.getValue(executor.pop());
        ExObject obj1 = ObjectSize.getValue(executor.pop());

        if(obj.getType()==ExObject.STRING||obj1.getType()==ExObject.STRING) throw new VMRuntimeException("字符串不能参与比较运算",executor.getThread());
        if(obj.getType()==ExObject.BOOLEAN||obj1.getType()==ExObject.BOOLEAN)executor.push(new ExBool(false));

        if(obj.getType()==ExObject.DOUBLE||obj1.getType()==ExObject.DOUBLE) {
            executor.push(new ExBool(Double.parseDouble(obj1.getData()) > Double.parseDouble(obj.getData())));
        }else if(obj.getType()==ExObject.INTEGER&&obj1.getType()==ExObject.INTEGER){
            executor.push(new ExBool(Integer.parseInt(obj1.getData()) > Integer.parseInt(obj.getData())));
        }
    }
}
