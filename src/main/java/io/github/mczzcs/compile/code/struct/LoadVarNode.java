package io.github.mczzcs.compile.code.struct;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.ExValue;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.List;

public class LoadVarNode extends StructNode {
    String name;
    int type;
    List<ASTNode> bcs;
    public LoadVarNode(String name, int type, List<ASTNode> bcs){
        this.name = name;
        this.bcs = bcs;
        this.type = type;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        for(ASTNode b:bcs)b.executor(executor);

        ExValue v = new ExValue(name,type);
        v.setVar(executor.pop());
        executor.getThread().getCallStackPeek().getValues().add(v);
    }

    @Override
    public String toString() {
        return "LOAD:"+bcs;
    }
}
