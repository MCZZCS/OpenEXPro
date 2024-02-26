package io.github.mczzcs.compile.tokens;

import io.github.mczzcs.compile.code.ASTNode;

import java.util.List;

public class CallBuffer extends Token{
    Token method_name;
    List<ASTNode> args_parser;

    public CallBuffer(Token method_name,List<ASTNode> args_parser){
        this.method_name = method_name;
        this.args_parser = args_parser;
    }

    @Override
    public int getType() {
        return CALL;
    }

    @Override
    public String toString() {
        return "Method: "+method_name.getData();
    }

    public Token getMethodName() {
        return method_name;
    }

    public List<ASTNode> getArgsParser() {
        return args_parser;
    }
}
