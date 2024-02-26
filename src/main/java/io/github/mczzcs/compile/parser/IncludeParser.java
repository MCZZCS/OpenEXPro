package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.NulASTNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.tokens.Token;
import io.github.mczzcs.util.CompileException;

import java.util.List;
import java.util.Set;

public class IncludeParser implements BaseParser{
    List<Token> tds;

    public IncludeParser(List<Token> tds){
        this.tds = tds;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos)throws CompileException {
        if(tds.size()>2)throw new CompileException("Unable to resolve symbols.",tds.get(tds.size()-2),parser.filename,parser);
        Token l = tds.get(0);
        if(l.getType()==Token.STRING) compiler.getLibnames().add(l.getData());
        else throw new CompileException("Type name is not valid.",l, parser.filename,parser);
        return new NulASTNode();
    }
}
