package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.loop.BackNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.util.CompileException;

import java.util.Set;


public class BackParser implements BaseParser{
    public BackParser(){
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {
        return new BackNode();
    }
}
