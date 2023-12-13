package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.util.CompileException;

import java.util.Set;

public interface BaseParser {
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException;
}
