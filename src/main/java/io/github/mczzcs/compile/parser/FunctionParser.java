package io.github.mczzcs.compile.parser;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.code.struct.LoadVarNode;
import io.github.mczzcs.exe.code.struct.NulASTNode;
import io.github.mczzcs.exe.lib.Function;
import io.github.mczzcs.exe.thread.ThreadManager;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.util.CompileException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FunctionParser implements BaseParser{
    ArrayList<Token> vars;
    ArrayList<BaseParser> parsers;
    ArrayList<String> pre_value_names,pre_array_names;
    String function_name;

    public FunctionParser(){
        this.pre_value_names = new ArrayList<>();
        this.pre_array_names = new ArrayList<>();
    }

    public FunctionParser(String function_name, ArrayList<Token> vars,ArrayList<BaseParser> parsers){
        this.function_name = function_name;
        this.parsers = parsers;
        this.pre_value_names = new ArrayList<>();
        this.pre_array_names = new ArrayList<>();
        this.vars = vars;
    }

    public void setFunctionName(String function_name) {
        this.function_name = function_name;
    }

    public void setParsers(ArrayList<BaseParser> parsers) {
        this.parsers = parsers;
    }

    public void setVars(ArrayList<Token> vars) {
        this.vars = vars;
    }

    public ArrayList<String> getPreValueNames() {
        return pre_value_names;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler,FunctionParser functionParser) throws CompileException {
        ArrayList<ASTNode> bcs = new ArrayList<>();
        try {
            for (Iterator<Token> iterator = vars.iterator(); iterator.hasNext(); ) {
                Token t = iterator.next();
                if (!(t.getType() == Token.NAME))
                    throw new CompileException("Type name is not valid..", t, parser.filename);
                bcs.add(new LoadVarNode(t.getData(), 1, new ArrayList<>()));
                t = iterator.next();
                if (!(t.getType() == Token.SEM && t.getData().equals(",")))
                    throw new CompileException("Unable to resolve symbols.", t, parser.filename);
            }
        }catch (NoSuchElementException ignored){
        }
        Collections.reverse(bcs);

        for(BaseParser bp:parsers){
            if(bp instanceof ValueParser){
                bcs.add(((ValueParser) bp).evalPreVar(parser,compiler,this));
                continue;
            }
            bcs.add(bp.eval(parser,compiler,this));
        }

        ThreadManager.getFunctions().add(new Function(parser.getFilename().split("\\.")[0],function_name,bcs));
        return new NulASTNode();
    }
}
