package io.github.mczzcs.exe.code.struct;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;

public class GroupASTNode extends StructNode {
    ArrayList<ASTNode> bc;
    public GroupASTNode(ArrayList<ASTNode> bc){
        this.bc = bc;
    }
    public GroupASTNode(ASTNode bc){
        this.bc = new ArrayList<>();
        this.bc.add(bc);
    }

    @Override
    public String toString() {
        return bc.toString();
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        for(ASTNode bcc:bc)bcc.executor(executor);
    }
}
