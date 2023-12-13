package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.loop.ContinueNode;
import io.github.mczzcs.compile.code.struct.loop.LoopNodeX;
import io.github.mczzcs.util.CompileException;

import java.util.*;

public class ForParser implements BaseParser {
    BaseParser bool;
    BaseParser expression;
    List<BaseParser> parsers;
    BaseParser value_parser;
    FunctionParser fparser;

    Set<String> buf;

    public ForParser(BaseParser value_parser, BaseParser bool, BaseParser expression, List<BaseParser> parsers, FunctionParser fparser) {
        this.bool = bool;
        this.parsers = parsers;
        this.fparser = fparser;
        this.value_parser = value_parser;
        this.expression = expression;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {

        ArrayList<ASTNode> b = new ArrayList<>();
        this.buf = new HashSet<>(tos);

        ASTNode va = value_parser.eval(parser, compiler, tos);
        ASTNode bo = bool.eval(parser, compiler, tos);
        ASTNode ex = expression.eval(parser, compiler, tos);


        for (int i = 0, parsersSize = parsers.size(); i < parsersSize; i++) {
            BaseParser p = parsers.get(i);
            if (p instanceof ContinueNode) {
                if (i == parsersSize - 1) break;
            }
            b.add(p.eval(parser, compiler, tos));
        }

        tos.clear();
        tos.addAll(buf);

        return new LoopNodeX(va, Collections.singletonList(bo), b, Collections.singletonList(ex));
    }
}
