package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.decide.IfNode;
import io.github.mczzcs.compile.code.struct.loop.BackNode;
import io.github.mczzcs.compile.code.struct.loop.WhileNode;
import io.github.mczzcs.util.CompileException;
import io.github.mczzcs.compile.Compiler;

import java.util.*;

public class WhileParser implements BaseParser{
    BaseParser bool;
    List<BaseParser> parsers;
    FunctionParser fparser;
    public WhileParser(BaseParser bool, List<BaseParser> parsers, FunctionParser fparser){
        this.bool = bool;
        this.parsers = parsers;
        this.fparser = fparser;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {
        List<ASTNode> b = new LinkedList<>();
        Set<String> tos_buf = new HashSet<>(tos);

        List<ASTNode> bpp = Collections.singletonList(bool.eval(parser, compiler, tos));

        for (int i = 0, parsersSize = parsers.size(); i < parsersSize; i++) {
            BaseParser p = parsers.get(i);
            if (p instanceof ValueParser) {
                continue;
            }

            if(i == parsersSize - 1&&p instanceof BackParser){
                tos.clear();
                tos.addAll(tos_buf);
                return new IfNode(bpp,b,new LinkedList<>(),null);
            }

            b.add(p.eval(parser, compiler, tos));
        }

        tos.clear();
        tos.addAll(tos_buf);
        return new WhileNode(bpp,b);
    }
}
