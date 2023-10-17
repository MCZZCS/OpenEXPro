package io.github.mczzcs.exe.code.struct;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;

public class BackNode extends StructNode {
    ArrayList<ASTNode> b;
    public BackNode(ArrayList<ASTNode> b){
        this.b = b;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        for(ASTNode bb:b)bb.executor(executor);
    }
}
