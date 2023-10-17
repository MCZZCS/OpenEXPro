package io.github.mczzcs.compile.parser;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.ReturnNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.compile.ExpressionParsing;
import io.github.mczzcs.util.CompileException;

import java.util.ArrayList;

public class ReturnParser implements BaseParser{
    ArrayList<Token> tds;
    FunctionParser fparser;
    public ReturnParser(ArrayList<Token> tds,FunctionParser fparser){
        this.tds = tds;
        this.fparser = fparser;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser functionParser) throws CompileException {
        ExpressionParsing p = new ExpressionParsing(tds,parser,compiler);
        return new ReturnNode(p.calculate(p.transitSuffix(fparser)));
    }
}
