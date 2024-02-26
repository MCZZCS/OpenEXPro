package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.GroupASTNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.tokens.Token;
import io.github.mczzcs.compile.ExpressionParsing;
import io.github.mczzcs.util.CompileException;

import java.util.List;
import java.util.Set;

public class ExpParser implements BaseParser{
    List<Token> tds;
    FunctionParser fparser;

    public ExpParser(List<Token> tds, FunctionParser fparser){
        this.tds = tds;
        this.fparser = fparser;
    }
    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {
        ExpressionParsing ep = new ExpressionParsing(tds,parser,compiler);
        return new GroupASTNode(ep.calculate(ep.transitSuffix(tos)));
    }
}
