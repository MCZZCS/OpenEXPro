package io.github.mczzcs.compile.code.struct;

import io.github.mczzcs.compile.code.ASTNode;

public abstract class StructNode implements ASTNode {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
