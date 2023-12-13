package io.github.mczzcs.compile.code.struct.loop;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.StructNode;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.List;

public class LoopNodeX extends StructNode {
    List<ASTNode> bool;
    List<ASTNode> expression;
    ASTNode statement;
    List<ASTNode> codes;
    public LoopNodeX(ASTNode statement,List<ASTNode> bool,List<ASTNode> codes,List<ASTNode> expression){
        this.statement = statement;
        this.bool = bool;
        this.codes = codes;
        this.expression = expression;
    }

    @Override
    public void executor(Executor executor) throws VMRuntimeException {
        statement.executor(executor);
        loop(executor);
    }

    private void loop(Executor executor){
        while (true) {
            for (ASTNode b : bool) {
                b.executor(executor);
            }
            ExObject obj = executor.pop();

            if (!Boolean.parseBoolean(obj.getData())) return;

            for(ASTNode b:expression) b.executor(executor);

            for (ASTNode b : codes) {
                if (b instanceof BackNode) return;
                if (b instanceof ContinueNode){
                    loop(executor);
                    break;
                }
                b.executor(executor);
            }
        }
    }

    @Override
    public String toString() {
        return "[LX]:Bool:"+bool+"|Code:"+codes;
    }
}
