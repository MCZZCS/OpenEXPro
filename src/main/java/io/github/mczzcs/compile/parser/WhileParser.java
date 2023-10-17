package io.github.mczzcs.compile.parser;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.WhileNode;
import io.github.mczzcs.util.CompileException;
import io.github.mczzcs.compile.Compiler;

import java.util.ArrayList;

public class WhileParser implements BaseParser{
    ArrayList<ASTNode> bool;
    ArrayList<BaseParser> parsers;
    FunctionParser fparser;

    public WhileParser(ArrayList<ASTNode> bool, ArrayList<BaseParser> parsers,FunctionParser fparser){
        this.bool = bool;
        this.parsers = parsers;
        this.fparser = fparser;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser functionParser) throws CompileException {
        ArrayList<ASTNode> b = new ArrayList<>();
        for(BaseParser p:parsers)b.add(p.eval(parser,compiler,functionParser));
        return new WhileNode(bool,b);
    }
}
