package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.decide.ElseIfNode;
import io.github.mczzcs.compile.code.struct.decide.ElseNode;
import io.github.mczzcs.compile.code.struct.decide.IfNode;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.util.CompileException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class IfParser implements BaseParser{
    BaseParser bool;
    List<BaseParser> group;
    List<BaseParser> else_group;
    List<ElseIfParser> elseIfParsers;

    public IfParser(BaseParser bool, List<BaseParser> group, List<ElseIfParser> elseIfParsers, List<BaseParser> else_group){
        this.bool = bool;
        this.group = group;
        this.else_group = else_group;
        this.elseIfParsers = elseIfParsers;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {
        Set<String> tos_buf = new HashSet<>(tos);
        List<ASTNode> groups = new LinkedList<>(),else_groups = new LinkedList<>();
        List<ElseIfNode> ein = new LinkedList<>();

        List<ASTNode> bool_o = new LinkedList<>();
        bool_o.add(bool.eval(parser,compiler,tos));


        for(BaseParser bp:group){
            groups.add(bp.eval(parser, compiler,tos));
        }

        for(ElseIfParser eip:elseIfParsers){
            List<ASTNode> b = eip.bool;
            List<ASTNode> group = new LinkedList<>(){
                {
                   for(BaseParser baseParser:eip.group) add(baseParser.eval(parser, compiler,tos));
                }
            };
            ein.add(new ElseIfNode(b,group));
        }

        for(BaseParser bp:else_group)else_groups.add(bp.eval(parser, compiler,tos));

        tos.clear();
        tos.addAll(tos_buf);

        return new IfNode(bool_o,groups,ein,new ElseNode(else_groups));
    }
}

