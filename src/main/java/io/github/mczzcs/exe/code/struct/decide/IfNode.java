package io.github.mczzcs.exe.code.struct.decide;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.StructNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;

public class IfNode extends StructNode {
    ArrayList<ASTNode> bool,group;
    ElseNode else_group;
    ArrayList<ElseIfNode> elseIfNodes;

    public IfNode(ArrayList<ASTNode> bool, ArrayList<ASTNode> group,ArrayList<ElseIfNode> elseIfNodes, ElseNode else_group){
        this.bool = bool;
        this.group = group;
        this.else_group = else_group;
        this.elseIfNodes = elseIfNodes;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        for(ASTNode b:bool)b.executor(executor);
        ExObject o = executor.pop();

        if(Boolean.parseBoolean(o.getData())){
            for(ASTNode b:group)b.executor(executor);
        }else {
            boolean isok = true;
            for(ElseIfNode node:elseIfNodes){
                if(node.ifexecutor(executor)){
                    isok = false;
                    break;
                }
            }
            if(isok) else_group.executor(executor);
        }
    }
}
