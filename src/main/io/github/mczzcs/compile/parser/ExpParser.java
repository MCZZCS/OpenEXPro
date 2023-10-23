package io.github.mczzcs.compile.parser;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.GroupASTNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.compile.ExpressionParsing;
import io.github.mczzcs.util.CompileException;

import java.util.ArrayList;

public class ExpParser implements BaseParser{
    ArrayList<Token> tds;
    FunctionParser fparser;

    public ExpParser(ArrayList<Token> tds,FunctionParser fparser){
        this.tds = tds;
        this.fparser = fparser;
    }
    @Override
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser functionParser) throws CompileException {
        ExpressionParsing ep = new ExpressionParsing(tds,parser,compiler);
        return new GroupASTNode(ep.calculate(ep.transitSuffix(functionParser)));
    }
}
