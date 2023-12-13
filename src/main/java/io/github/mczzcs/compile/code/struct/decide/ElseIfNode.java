package io.github.mczzcs.compile.code.struct.decide;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.StructNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.List;

public class ElseIfNode extends StructNode {
    List<ASTNode> bool,group;

    public ElseIfNode(List<ASTNode> bool, List<ASTNode> group){
        this.bool = bool;
        this.group = group;
    }

    public boolean ifexecutor(Executor executor) throws VMRuntimeException {
        for(ASTNode b:bool)b.executor(executor);
        ExObject o = executor.pop();

        if(Boolean.parseBoolean(o.getData())){
            for(ASTNode b:group)b.executor(executor);
            return true;
        }else return false;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
    }
}
