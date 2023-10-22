package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.util.CompileException;

public class BufParser implements BaseParser{
    ASTNode nodes;
    public BufParser(ASTNode nodes){
        this.nodes = nodes;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser functionParser) throws CompileException {
        return nodes;
    }
}
