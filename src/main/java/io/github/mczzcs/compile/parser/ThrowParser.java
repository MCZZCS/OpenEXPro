package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.ExpressionParsing;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.ThrowASTNode;
import io.github.mczzcs.util.CompileException;
import io.github.mczzcs.util.VMRuntimeException;
import io.github.mczzcs.compile.Compiler;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ThrowParser implements BaseParser{
    List<Token> tokens;

    private int index = 0;

    private Token getTokens() {
        if (index >= tokens.size()) {
            return null;
        }
        Token td = tokens.get(index);
        index += 1;
        return td;
    }


    public ThrowParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {
        Token token = getTokens();
        try {
            if (token.getType() != Token.NAME) {
                throw new CompileException("Unable to resolve symbols.", token, parser.filename, parser);
            }
            VMRuntimeException.EnumVMException exception = VMRuntimeException.EnumVMException.valueOf(token.getData());

            token = getTokens();
            if (token.getType() != Token.SEM && !":".equals(token.getData())) {
                throw new CompileException("Illegal combination of expressions.", token, parser.filename, parser);
            }

            ExpressionParsing parsing = new ExpressionParsing(new LinkedList<>() {
                {
                    Token token1;
                    while ((token1 = getTokens()) != null) {
                        add(token1);
                    }
                }
            }, parser, compiler);
            return new ThrowASTNode(parsing.eval(parser, compiler, tos), exception);

        } catch (IllegalArgumentException e) {
            throw new CompileException("Not found exception.", token, parser.filename, parser);
        }
    }
}
