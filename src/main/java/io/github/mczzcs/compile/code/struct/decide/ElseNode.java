package io.github.mczzcs.compile.code.struct.decide;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.GroupASTNode;

import java.util.List;

public class ElseNode extends GroupASTNode {
    public ElseNode(List<ASTNode> bc) {
        super(bc);
    }
}
