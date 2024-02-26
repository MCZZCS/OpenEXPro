package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.struct.LoadVarNode;
import io.github.mczzcs.compile.code.struct.NulASTNode;
import io.github.mczzcs.exe.lib.Function;
import io.github.mczzcs.exe.thread.ThreadManager;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.tokens.Token;
import io.github.mczzcs.util.CompileException;

import java.util.*;

public class FunctionParser implements BaseParser{
    List<Token> vars;
    List<BaseParser> parsers;
    List<String> pre_value_names,pre_array_names;
    String function_name;

    public FunctionParser(){
        this.pre_value_names = new LinkedList<>();
        this.pre_array_names = new LinkedList<>();
    }

    public FunctionParser(String function_name, List<Token> vars,List<BaseParser> parsers){
        this.function_name = function_name;
        this.parsers = parsers;
        this.pre_value_names = new LinkedList<>();
        this.pre_array_names = new LinkedList<>();
        this.vars = vars;
    }

    public void setFunctionName(String function_name) {
        this.function_name = function_name;
    }

    public void setParsers(List<BaseParser> parsers) {
        this.parsers = parsers;
    }

    public void setVars(List<Token> vars) {
        this.vars = vars;
    }

    public List<String> getPreValueNames() {
        return pre_value_names;
    }

    public List<String> getPreArrayNames() {
        return pre_array_names;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {
        ArrayList<ASTNode> bcs = new ArrayList<>();
        try {
            for (Iterator<Token> iterator = vars.iterator(); iterator.hasNext(); ) {
                Token t = iterator.next();
                if (!(t.getType() == Token.NAME))
                    throw new CompileException("Type name is not valid..", t, parser.filename,parser);
                bcs.add(new LoadVarNode(t.getData(), 1, new ArrayList<>()));
                t = iterator.next();
                if (!(t.getType() == Token.SEM && t.getData().equals(",")))
                    throw new CompileException("Unable to resolve symbols.", t, parser.filename,parser);
            }
        }catch (NoSuchElementException ignored){
        }

        Collections.reverse(bcs);

        for(BaseParser bp:parsers){
            if(bp instanceof ValueParser){
                bcs.add(((ValueParser) bp).evalPreVar(parser,compiler,tos));
                continue;
            }
            bcs.add(bp.eval(parser,compiler,tos));
        }

        ThreadManager.getFunctions().add(new Function(parser.getFilename().split("\\.")[0],function_name,bcs, parser.filename));
        return new NulASTNode();
    }
}
