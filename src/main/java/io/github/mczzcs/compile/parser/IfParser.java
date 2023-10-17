package io.github.mczzcs.compile.parser;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.IfNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.util.CompileException;

import java.util.ArrayList;

public class IfParser implements BaseParser{
    ArrayList<ASTNode> bool;
    ArrayList<BaseParser> group;
    ArrayList<BaseParser> else_group;
    FunctionParser fparser;

    public IfParser(ArrayList<ASTNode> bool, ArrayList<BaseParser> group, ArrayList<BaseParser> else_group){
        this.bool = bool;
        this.group = group;
        this.else_group = else_group;
    }


    @Override
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser functionParser) throws CompileException {
        ArrayList<ASTNode> groups = new ArrayList<>(),else_groups = new ArrayList<>();
        for(BaseParser bp:group){
            groups.add(bp.eval(parser, compiler,functionParser));
        }
        for(BaseParser bp:else_group)else_groups.add(bp.eval(parser, compiler,functionParser));

        return new IfNode(bool,groups,else_groups);
    }
}

