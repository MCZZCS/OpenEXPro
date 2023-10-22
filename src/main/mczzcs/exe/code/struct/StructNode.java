package io.github.mczzcs.exe.code.struct;

import io.github.mczzcs.exe.code.ASTNode;

public abstract class StructNode implements ASTNode {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
