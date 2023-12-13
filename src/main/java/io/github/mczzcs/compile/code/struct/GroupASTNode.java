package io.github.mczzcs.compile.code.struct;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class GroupASTNode extends StructNode {
    List<ASTNode> bc;
    public GroupASTNode(List<ASTNode> bc){
        this.bc = bc;
    }
    public GroupASTNode(ASTNode bc){
        this.bc = new ArrayList<>();
        this.bc.add(bc);
    }

    public List<ASTNode> getIR() {
        return bc;
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
