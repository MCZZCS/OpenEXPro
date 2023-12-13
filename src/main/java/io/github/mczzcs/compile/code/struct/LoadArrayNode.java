package io.github.mczzcs.compile.code.struct;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.ExArray;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.LinkedList;
import java.util.List;

public class LoadArrayNode extends StructNode {
    String name;
    List<GroupASTNode> g;
    int type;
    int size;

    public LoadArrayNode(String name, List<GroupASTNode> g, int type, int size){
        this.name = name;
        this.g = g;
        this.type = type;
        this.size = size;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        List<ExObject> objs = new LinkedList<>();
        for(GroupASTNode gg:g){
            for(ASTNode bb:gg.bc)bb.executor(executor);
            objs.add(executor.pop());
        }
        ExArray array;
        if(objs.size()==0){
            if(size > -1)array = new ExArray(name,size);
            else array = new ExArray(name,0);
        } else array = new ExArray(name,objs);


        executor.getThread().getCallStackPeek().getValues().add(array);
    }
}
