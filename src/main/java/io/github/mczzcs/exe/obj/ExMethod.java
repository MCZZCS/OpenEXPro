package io.github.mczzcs.exe.obj;

import io.github.mczzcs.compile.code.ASTNode;

public class ExMethod extends ExObject{
    String method_name;
    ASTNode vars;

    public ExMethod(String method_name,ASTNode vars){
        this.method_name = method_name;
        this.vars = vars;
    }

    public ExMethod(String method_name){
        this.method_name = method_name;
    }

    public ASTNode getVars() {
        return vars;
    }

    public String getMethodName() {
        return method_name;
    }

    @Override
    public String getData() {
        return method_name;
    }

    @Override
    public int getType() {
        return METHOD;
    }

    @Override
    public String toString() {
        return "Method: "+method_name +"||Args: "+vars;
    }
}
