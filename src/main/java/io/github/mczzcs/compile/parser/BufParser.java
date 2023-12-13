package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.util.CompileException;

import java.util.Set;

public class BufParser implements BaseParser{
    ASTNode nodes;
    public BufParser(ASTNode nodes){
        this.nodes = nodes;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {
        return nodes;
    }
}
