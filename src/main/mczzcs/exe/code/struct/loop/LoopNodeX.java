package io.github.mczzcs.exe.code.struct.loop;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.BackNode;
import io.github.mczzcs.exe.code.struct.StructNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;

public class LoopNodeX extends StructNode {
    ArrayList<ASTNode> bool;
    ASTNode statement;
    ArrayList<ASTNode> codes;
    public LoopNodeX(ASTNode statement,ArrayList<ASTNode> bool,ArrayList<ASTNode> codes){
        this.statement = statement;
        this.bool = bool;
        this.codes = codes;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {

        statement.executor(executor);

        while (true){
            for(ASTNode b:bool){
                b.executor(executor);
            }
            ExObject obj = executor.pop();

            if(!Boolean.parseBoolean(obj.getData()))break;

            for(ASTNode b:codes){
                if(b instanceof BackNode)return;
                b.executor(executor);
            }
        }
    }
}
