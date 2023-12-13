package io.github.mczzcs.compile.code.struct.loop;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.StructNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.List;

public class WhileNode extends StructNode {
    List<ASTNode> bool,group;
    public WhileNode(List<ASTNode> bool, List<ASTNode> group){
        this.bool = bool;
        this.group = group;
    }
    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        while (true){
            for(ASTNode b:bool){
                b.executor(executor);
            }

            ExObject obj = executor.pop();

            if(!Boolean.parseBoolean(obj.getData()))break;

            for(ASTNode b:group){
                if(b instanceof BackNode)return;

                b.executor(executor);
            }
        }
    }
}
