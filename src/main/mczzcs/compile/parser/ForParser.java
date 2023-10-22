package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.loop.LoopNodeX;
import io.github.mczzcs.util.CompileException;

import java.util.ArrayList;

public class ForParser implements BaseParser{
    ArrayList<ASTNode> bool;
    ArrayList<BaseParser> parsers;
    ASTNode value_parser;
    FunctionParser fparser;

    public ForParser(ASTNode value_parser,ArrayList<ASTNode> bool, ArrayList<BaseParser> parsers,FunctionParser fparser){
        this.bool = bool;
        this.parsers = parsers;
        this.fparser = fparser;
        this.value_parser = value_parser;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, FunctionParser functionParser) throws CompileException {
        ArrayList<ASTNode> b = new ArrayList<>();
        for(BaseParser p:parsers)b.add(p.eval(parser,compiler,functionParser));

        return new LoopNodeX(value_parser,bool,b);
    }
}
