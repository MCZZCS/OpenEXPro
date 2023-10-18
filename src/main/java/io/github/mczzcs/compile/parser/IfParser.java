package io.github.mczzcs.compile.parser;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.decide.ElseIfNode;
import io.github.mczzcs.exe.code.struct.decide.ElseNode;
import io.github.mczzcs.exe.code.struct.decide.IfNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.util.CompileException;

import java.util.ArrayList;

public class IfParser implements BaseParser{
    ArrayList<ASTNode> bool;
    ArrayList<BaseParser> group;
    ArrayList<BaseParser> else_group;
    ArrayList<ElseIfParser> elseIfParsers;

    public IfParser(ArrayList<ASTNode> bool, ArrayList<BaseParser> group,ArrayList<ElseIfParser> elseIfParsers, ArrayList<BaseParser> else_group){
        this.bool = bool;
        this.group = group;
        this.else_group = else_group;
        this.elseIfParsers = elseIfParsers;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser functionParser) throws CompileException {
        ArrayList<ASTNode> groups = new ArrayList<>(),else_groups = new ArrayList<>();
        ArrayList<ElseIfNode> ein = new ArrayList<>();
        for(BaseParser bp:group){
            groups.add(bp.eval(parser, compiler,functionParser));
        }

        for(ElseIfParser eip:elseIfParsers){
            ArrayList<ASTNode> b = eip.bool;
            ArrayList<ASTNode> group = new ArrayList<>(){
                {
                   for(BaseParser baseParser:eip.group) add(baseParser.eval(parser, compiler, functionParser));
                }
            };
            ein.add(new ElseIfNode(b,group));
        }

        for(BaseParser bp:else_group)else_groups.add(bp.eval(parser, compiler,functionParser));

        return new IfNode(bool,groups,ein,new ElseNode(else_groups));
    }
}

