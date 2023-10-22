package io.github.mczzcs.compile.parser;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.BackNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.compile.ExpressionParsing;
import io.github.mczzcs.util.CompileException;

import java.util.ArrayList;

public class BackParser implements BaseParser{
    ArrayList<Token> t;
    public BackParser(ArrayList<Token> t){
        this.t = t;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser functionParser) throws CompileException {
        ExpressionParsing p = new ExpressionParsing(t,parser,compiler);
        return new BackNode(p.calculate(p.transitSuffix(functionParser)));
    }
}
