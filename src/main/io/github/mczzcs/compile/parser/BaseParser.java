package io.github.mczzcs.compile.parser;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.util.CompileException;

public interface BaseParser {
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser fparser) throws CompileException;
}
