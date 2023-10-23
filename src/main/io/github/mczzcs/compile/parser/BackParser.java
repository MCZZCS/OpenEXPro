package io.github.mczzcs.compile.parser;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.loop.BackNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.util.CompileException;


public class BackParser implements BaseParser{
    public BackParser(){
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser functionParser) throws CompileException {
        return new BackNode();
    }
}
