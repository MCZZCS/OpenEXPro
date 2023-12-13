package io.github.mczzcs.compile.parser;

import io.github.mczzcs.ConsoleModel;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.ExceptionASTNode;
import io.github.mczzcs.compile.code.struct.GroupASTNode;
import io.github.mczzcs.compile.code.struct.LoadVarNode;
import io.github.mczzcs.util.CompileException;
import io.github.mczzcs.util.VMRuntimeException;
import io.github.mczzcs.compile.Compiler;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TryParser implements BaseParser{
    List<BaseParser> try_group , catch_group;
    List<Token> catch_expression;

    private int index = 0;

    private Token getTokens() {
        if (index >= catch_expression.size()) {
            return null;
        }
        Token td = catch_expression.get(index);
        index += 1;
        return td;
    }

    public TryParser(List<BaseParser> try_group , List<Token> catch_expression, List<BaseParser> catch_group){
        this.try_group = try_group;
        this.catch_expression = catch_expression;
        this.catch_group = catch_group;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {
        Token t = null;
        Set<String> tos_buf = tos;
        try {
            GroupASTNode try_node = new GroupASTNode(new LinkedList<>() {
                {
                    for (BaseParser baseParser : try_group) {
                        add(baseParser.eval(parser, compiler, tos));
                    }
                }
            });

            tos.clear();
            tos.addAll(tos_buf);

            t = getTokens();

            if (t.getType() != Token.NAME) {
                throw new CompileException("Unable to resolve symbols.", t, parser.filename, parser);
            }
            VMRuntimeException.EnumVMException exception = VMRuntimeException.EnumVMException.valueOf(t.getData());

            if(!exception.canCatch()) {
                ConsoleModel.getOutput().warn("The exception cannot be handled. (script: "+parser.filename+" line: "+t.getLine()+")");
            }

            t = getTokens();
            if (t.getType() != Token.NAME) {
                throw new CompileException("Unable to resolve symbols.", t, parser.filename, parser);
            }
            if(tos.contains(t.getData())) {
                throw new CompileException("The type '"+t.getData()+"' is already defined.",t, parser.filename, parser);
            }
            tos.add(t.getData());

            Token finalT = t;

            t = getTokens();
            if (t.getType() != Token.SEM && !",".equals(t.getData())) {
                throw new CompileException("',' expected.", t, parser.filename, parser);
            }

            t = getTokens();
            if (t.getType() != Token.NAME) {
                throw new CompileException("Unable to resolve symbols.", t, parser.filename, parser);
            }
            if(tos.contains(t.getData())) {
                throw new CompileException("The type '"+t.getData()+"' is already defined.",t, parser.filename, parser);
            }
            tos.add(t.getData());

            Token finalStack = t;

            return new ExceptionASTNode(try_node, new GroupASTNode(new LinkedList<>(){
                {
                    add(new LoadVarNode(finalT.getData(),0,new LinkedList<>()));
                    add(new LoadVarNode(finalStack.getData(),0,new LinkedList<>()));
                    for (BaseParser baseParser : catch_group) {
                        add(baseParser.eval(parser, compiler, tos));
                    }

                    tos.clear();
                    tos.addAll(tos_buf);
                }
            }), exception);
        }catch (IllegalArgumentException e){
            throw new CompileException("Not found exception.", t, parser.filename, parser);
        }
    }
}
