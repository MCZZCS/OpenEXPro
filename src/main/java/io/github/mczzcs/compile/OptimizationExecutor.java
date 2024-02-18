package io.github.mczzcs.compile;

import io.github.mczzcs.compile.parser.Parser;
import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.opcode.PushNode;
import io.github.mczzcs.compile.code.struct.NulASTNode;
import io.github.mczzcs.util.ObjectSize;
import io.github.mczzcs.exe.obj.*;
import io.github.mczzcs.util.CompileException;

import java.util.Deque;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;

public class OptimizationExecutor {
    List<Token> suffix;
    Deque<ExObject> op_stack;

    Parser parser;
    public OptimizationExecutor(List<Token> suffix,Parser parser){
        this.suffix = suffix;
        this.op_stack = new LinkedList<>();
        this.parser = parser;
    }

    public ASTNode eval(){
        if(suffix.isEmpty()) return new NulASTNode();

        try {
            for (Token token : suffix) {
                if (token.getType() != Token.SEM) {
                    switch (token.getType()) {
                        case Token.INTEGER -> op_stack.push(new ExInt(Integer.parseInt(token.data)));
                        case Token.STRING -> op_stack.push(new ExString(token.data));
                        case Token.DOUBLE -> op_stack.push(new ExDouble(Double.parseDouble(token.data)));
                        case Token.KEY -> {
                            switch (token.data) {
                                case "true" -> op_stack.add(new ExBool(true));
                                case "false" -> op_stack.add(new ExBool(false));
                                case "null" -> {
                                    return null;
                                }
                                default -> throw new CompileException("Illegal keywords.", token, parser.getFilename(), parser);
                            }
                        }
                        case Token.EXP -> {

                        }
                    }

                } else switch (token.getData()) {
                    case "+" -> add(token);
                    case "-" ->{
                        if(token instanceof UToken){
                            vnot(token);
                        }else sub(token);
                    }
                    case "*" -> mul(token);
                    case "/" -> div(token);
                    case "==" -> equ(token);
                    case ">=" -> bigequ(token);
                    case "<=" -> lessequ(token);
                    case ">" -> big(token);
                    case "<" -> less(token);
                    case "!" -> not(token);
                    case "&&" -> and(token);
                    case "||" -> or(token);
                    case "+=", "-=", "%=", "=" ->
                            throw new CompileException("Illegal combination of expressions.", token, parser.getFilename(), parser);
                    case "%" -> divx(token);
                    case "&" -> bitand(token);
                    case "|" -> bitor(token);
                }
            }

            return new PushNode(op_stack.pop());
        }catch (EmptyStackException e){
            return null;
        }
    }

    private void bitand(Token token){
        ExObject t1 = op_stack.pop();
        ExObject t2 = op_stack.pop();

        t1 = ObjectSize.getValue(t1);
        t2 = ObjectSize.getValue(t2);

        if(t1.getType()==ExObject.STRING||t2.getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN||t2.getType()==ExObject.BOOLEAN)throw new CompileException("The operation type is incorrect.",token, parser.getFilename(), parser);
        else if(t1.getType()==ExObject.DOUBLE||t2.getType()==ExObject.DOUBLE){
            throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        }
        else if(t1.getType()==ExObject.NULL||t2.getType()==ExObject.NULL)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else{
            op_stack.push(new ExInt(Integer.parseInt(t2.getData()) & Integer.parseInt(t1.getData())));
        }
    }

    private void bitor(Token token){

        ExObject t1 = op_stack.pop();
        ExObject t2 = op_stack.pop();

        t1 = ObjectSize.getValue(t1);
        t2 = ObjectSize.getValue(t2);

        if(t1.getType()==ExObject.STRING||t2.getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN||t2.getType()==ExObject.BOOLEAN)throw new CompileException("The operation type is incorrect.",token, parser.getFilename(), parser);
        else if(t1.getType()==ExObject.DOUBLE||t2.getType()==ExObject.DOUBLE){
            throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        }
        else if(t1.getType()==ExObject.NULL||t2.getType()==ExObject.NULL)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else{
            op_stack.push(new ExInt(Integer.parseInt(t2.getData()) | Integer.parseInt(t1.getData())));
        }
    }

    private void vnot(Token token){
        ExObject object = op_stack.pop();


        if(object.getType()==ExObject.STRING||object.getType()==ExObject.BOOLEAN||object.getType()==ExObject.ARRAY)
            throw new CompileException("The operation type is incorrect.",token, parser.getFilename(), parser);
        else if(object.getType()==ExObject.NULL)
            throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);

        if(object.getType()==ExObject.DOUBLE){
            op_stack.push(new ExDouble(-Double.parseDouble(object.getData())));
        }else if(object.getType()==ExObject.INTEGER){
            op_stack.push(new ExInt(-Integer.parseInt(object.getData())));
        }
    }

    private void or(Token token){
        ExObject obj = op_stack.pop();
        ExObject obj1 = op_stack.pop();

        obj = ObjectSize.getValue(obj);
        obj1 = ObjectSize.getValue(obj1);

        if(obj1.getType()==ExObject.BOOLEAN&&obj.getType()==ExObject.BOOLEAN){
            op_stack.push(new ExBool(Boolean.parseBoolean(obj.getData())||Boolean.parseBoolean(obj1.getData())));
        }else throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
    }

    private void and(Token token){
        ExObject obj = op_stack.pop();
        ExObject obj1 = op_stack.pop();

        obj = ObjectSize.getValue(obj);
        obj1 = ObjectSize.getValue(obj1);

        if(obj1.getType()==ExObject.BOOLEAN&&obj.getType()==ExObject.BOOLEAN){
            op_stack.push(new ExBool(Boolean.parseBoolean(obj.getData())&&Boolean.parseBoolean(obj1.getData())));
        }else throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
    }

    private void not(Token token){
        ExObject obj = op_stack.pop();
        obj = ObjectSize.getValue(obj);
        if(obj.getType()==ExObject.BOOLEAN){
            op_stack.push(new ExBool(!Boolean.parseBoolean(obj.getData())));
        }else throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
    }

    private void lessequ(Token token){
        ExObject obj = ObjectSize.getValue(op_stack.pop());
        ExObject obj1 = ObjectSize.getValue(op_stack.pop());

        if(obj.getType()==ExObject.STRING||obj1.getType()==ExObject.STRING) throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        if(obj.getType()==ExObject.BOOLEAN||obj1.getType()==ExObject.BOOLEAN)op_stack.push(new ExBool(false));

        if(obj.getType()==ExObject.DOUBLE||obj1.getType()==ExObject.DOUBLE) {
            op_stack.push(new ExBool(Double.parseDouble(obj1.getData()) <= Double.parseDouble(obj.getData())));
        }else if(obj.getType()==ExObject.INTEGER&&obj1.getType()==ExObject.INTEGER){
            op_stack.push(new ExBool(Integer.parseInt(obj1.getData()) <= Integer.parseInt(obj.getData())));
        }
    }

    private void bigequ(Token token){
        ExObject obj = ObjectSize.getValue(op_stack.pop());
        ExObject obj1 = ObjectSize.getValue(op_stack.pop());

        if(obj.getType()==ExObject.STRING||obj1.getType()==ExObject.STRING) throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        if(obj.getType()==ExObject.BOOLEAN||obj1.getType()==ExObject.BOOLEAN)op_stack.push(new ExBool(false));

        if(obj.getType()==ExObject.DOUBLE||obj1.getType()==ExObject.DOUBLE) {
            op_stack.push(new ExBool(Double.parseDouble(obj1.getData()) >= Double.parseDouble(obj.getData())));
        }else if(obj.getType()==ExObject.INTEGER&&obj1.getType()==ExObject.INTEGER){
            op_stack.push(new ExBool(Integer.parseInt(obj1.getData()) >= Integer.parseInt(obj.getData())));
        }
    }

    private void less(Token token){
        ExObject obj = ObjectSize.getValue(op_stack.pop());
        ExObject obj1 = ObjectSize.getValue(op_stack.pop());

        if(obj.getType()==ExObject.STRING||obj1.getType()==ExObject.STRING) throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        if(obj.getType()==ExObject.BOOLEAN||obj1.getType()==ExObject.BOOLEAN)op_stack.push(new ExBool(false));

        if(obj.getType()==ExObject.DOUBLE||obj1.getType()==ExObject.DOUBLE) {
            op_stack.push(new ExBool(Double.parseDouble(obj1.getData()) < Double.parseDouble(obj.getData())));
        }else if(obj.getType()==ExObject.INTEGER&&obj1.getType()==ExObject.INTEGER){
            op_stack.push(new ExBool(Integer.parseInt(obj1.getData()) < Integer.parseInt(obj.getData())));
        }
    }

    private void big(Token token){
        ExObject obj = ObjectSize.getValue(op_stack.pop());
        ExObject obj1 = ObjectSize.getValue(op_stack.pop());

        if(obj.getType()==ExObject.STRING||obj1.getType()==ExObject.STRING) throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        if(obj.getType()==ExObject.BOOLEAN||obj1.getType()==ExObject.BOOLEAN)op_stack.push(new ExBool(false));

        if(obj.getType()==ExObject.DOUBLE||obj1.getType()==ExObject.DOUBLE) {
            op_stack.push(new ExBool(Double.parseDouble(obj1.getData()) > Double.parseDouble(obj.getData())));
        }else if(obj.getType()==ExObject.INTEGER&&obj1.getType()==ExObject.INTEGER){
            op_stack.push(new ExBool(Integer.parseInt(obj1.getData()) > Integer.parseInt(obj.getData())));
        }
    }

    private void equ(Token token){
        ExObject obj = ObjectSize.getValue(op_stack.pop());
        ExObject obj1 = ObjectSize.getValue(op_stack.pop());

        if(obj.getType()==obj1.getType()){
            op_stack.push(new ExBool(obj.getData().equals(obj1.getData())));
        }else op_stack.push(new ExBool(false));
    }

    private void add(Token token){
        ExObject t1 = op_stack.pop();
        ExObject t2 = op_stack.pop();

        t1 = ObjectSize.getValue(t1);
        t2 = ObjectSize.getValue(t2);

        if(t1.getType()==ExObject.STRING||t2.getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN||t2.getType()==ExObject.BOOLEAN){
            op_stack.push(new ExString(t2.getData()+t1.getData()));
        }
        else if(t1.getType()==ExObject.DOUBLE||t2.getType()==ExObject.DOUBLE){
            op_stack.push(new ExDouble(Double.parseDouble(t2.getData())+Double.parseDouble(t1.getData())));
        }
        else if(t1.getType()==ExObject.NULL||t2.getType()==ExObject.NULL)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else{
            op_stack.push(new ExInt(Integer.parseInt(t2.getData())+Integer.parseInt(t1.getData())));
        }
    }

    private void sub(Token token){
        ExObject t1 = op_stack.pop();
        ExObject t2 = op_stack.pop();

        t1 = ObjectSize.getValue(t1);
        t2 = ObjectSize.getValue(t2);

        if(t1.getType()==ExObject.STRING||t2.getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN||t2.getType()==ExObject.BOOLEAN)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else if(t1.getType()==ExObject.DOUBLE||t2.getType()==ExObject.DOUBLE){
            op_stack.push(new ExDouble(Double.parseDouble(t2.getData())-Double.parseDouble(t1.getData())));
        }
        else if(t1.getType()==ExObject.NULL||t2.getType()==ExObject.NULL)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else{
            op_stack.push(new ExInt(Integer.parseInt(t2.getData())-Integer.parseInt(t1.getData())));
        }
    }

    private void mul(Token token){
        ExObject t1 = op_stack.pop();
        ExObject t2 = op_stack.pop();

        t1 = ObjectSize.getValue(t1);
        t2 = ObjectSize.getValue(t2);

        if(t1.getType()==ExObject.STRING||t2.getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN||t2.getType()==ExObject.BOOLEAN)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else if(t1.getType()==ExObject.DOUBLE||t2.getType()==ExObject.DOUBLE){
            op_stack.push(new ExDouble(Double.parseDouble(t2.getData())*Double.parseDouble(t1.getData())));
        }
        else if(t1.getType()==ExObject.NULL||t2.getType()==ExObject.NULL)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else{
            op_stack.push(new ExInt(Integer.parseInt(t2.getData())*Integer.parseInt(t1.getData())));
        }
    }

    private void div(Token token){
        ExObject t1 = op_stack.pop();
        ExObject t2 = op_stack.pop();

        t1 = ObjectSize.getValue(t1);
        t2 = ObjectSize.getValue(t2);

        if(t1.getType()==ExObject.STRING||t2.getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN||t2.getType()==ExObject.BOOLEAN)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else if(t1.getType()==ExObject.DOUBLE||t2.getType()==ExObject.DOUBLE){
            op_stack.push(new ExDouble(Double.parseDouble(t2.getData())/Double.parseDouble(t1.getData())));
        }
        else if(t1.getType()==ExObject.NULL||t2.getType()==ExObject.NULL)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else{
            op_stack.push(new ExInt(Integer.parseInt(t2.getData())/Integer.parseInt(t1.getData())));
        }
    }

    private void divx(Token token){
        ExObject t1 = op_stack.pop();
        ExObject t2 = op_stack.pop();

        t1 = ObjectSize.getValue(t1);
        t2 = ObjectSize.getValue(t2);

        if(t1.getType()==ExObject.STRING||t2.getType()==ExObject.STRING||t1.getType()==ExObject.BOOLEAN||t2.getType()==ExObject.BOOLEAN)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else if(t1.getType()==ExObject.DOUBLE||t2.getType()==ExObject.DOUBLE){
            op_stack.push(new ExDouble(Double.parseDouble(t2.getData())%Double.parseDouble(t1.getData())));
        }
        else if(t1.getType()==ExObject.NULL||t2.getType()==ExObject.NULL)throw new CompileException("Illegal combination of expressions.",token, parser.getFilename(), parser);
        else{
            op_stack.push(new ExInt(Integer.parseInt(t2.getData())%Integer.parseInt(t1.getData())));
        }
    }
}
