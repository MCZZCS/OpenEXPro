package io.github.mczzcs.compile.code.opcode;

import io.github.mczzcs.compile.code.ASTNode;

public abstract class OpNode implements ASTNode {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
