package io.github.mczzcs.compile.parser;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.NulASTNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.util.CompileException;

import java.util.ArrayList;

public class IncludeParser implements BaseParser{
    ArrayList<Token> tds;

    public IncludeParser(ArrayList<Token> tds){
        this.tds = tds;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser functionParser)throws CompileException {
        if(tds.size()>2)throw new CompileException("Unable to resolve symbols.",tds.get(tds.size()-2),parser.filename);
        Token l = tds.get(0);
        if(l.getType()==Token.STRING) compiler.getLibnames().add(l.getData());
        else throw new CompileException("Type name is not valid.",l, parser.filename);
        return new NulASTNode();
    }
}
