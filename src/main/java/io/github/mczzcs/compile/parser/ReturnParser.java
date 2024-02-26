package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.ReturnNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.tokens.Token;
import io.github.mczzcs.compile.ExpressionParsing;
import io.github.mczzcs.util.CompileException;

import java.util.List;
import java.util.Set;

public class ReturnParser implements BaseParser{
    List<Token> tds;
    FunctionParser fparser;
    public ReturnParser(List<Token> tds, FunctionParser fparser){
        this.tds = tds;
        this.fparser = fparser;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {
        ExpressionParsing p = new ExpressionParsing(tds,parser,compiler);
        return new ReturnNode(p.calculate(p.transitSuffix(tos)));
    }
}
