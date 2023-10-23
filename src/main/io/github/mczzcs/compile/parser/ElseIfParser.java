package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.NulASTNode;
import io.github.mczzcs.util.CompileException;

import java.util.ArrayList;

public class ElseIfParser implements BaseParser{
    ArrayList<ASTNode> bool;
    ArrayList<BaseParser> group;

    public ElseIfParser(ArrayList<ASTNode> bool,ArrayList<BaseParser> group){
        this.bool = bool;
        this.group = group;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, FunctionParser fparser) throws CompileException {
        return new NulASTNode();
    }
}
