package io.github.mczzcs.exe.code.opcode;

import io.github.mczzcs.exe.code.ASTNode;

public abstract class OpNode implements ASTNode {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
