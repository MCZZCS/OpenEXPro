package io.github.mczzcs.compile;

import io.github.mczzcs.ConsoleModel;
import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.compile.code.opcode.*;
import io.github.mczzcs.compile.code.struct.GroupASTNode;
import io.github.mczzcs.compile.code.struct.InvokeASTNode;
import io.github.mczzcs.compile.code.struct.MovVarNode;
import io.github.mczzcs.compile.parser.BaseParser;
import io.github.mczzcs.compile.parser.Parser;
import io.github.mczzcs.compile.tokens.*;
import io.github.mczzcs.exe.obj.*;
import io.github.mczzcs.util.CompileException;

import java.util.*;

public class ExpressionParsing implements BaseParser {
    private static final Set<String> OP_DATA = Set.of("+", "-", "*", "/", ">=", "<=", "==", "!", "&",
            "|", "=", ">", "<", ",", "+=", "-=", "*=", "/=", "%", "%=",
            "++", "--", "&&", "||", "^", ".");
    List<Token> tds;
    Parser parser;
    Compiler compiler;
    Token buffer = null;
    int index;

    public ExpressionParsing(List<Token> tds, Parser parser, Compiler compiler) {
        this.tds = tds;
        this.parser = parser;
        this.compiler = compiler;
        this.index = 0;
    }

    private Token getToken(List<Token> tokens) {
        Token t;
        if (buffer == null) {
            if (index >= tokens.size()) return null;
            t = tokens.get(index);
            index += 1;
        } else {
            t = buffer;
            buffer = null;
        }
        return t;
    }

    public List<Token> transitSuffix(Set<String> tos) {

        boolean isUnary = true;

        Deque<Token> op_stack = new LinkedList<>();
        List<Token> suffixList = new LinkedList<>();

        while (true) {
            Token token = getToken(tds);
            try {
                if (token == null) break;

                if (isOperator(token)) {
                    if (isUnaryOperator(token, isUnary)) {
                        op_stack.push(new UToken(token));
                    } else {
                        if (op_stack.isEmpty() || ("(".equals(op_stack.peek().getData()) && op_stack.peek().getType() == Token.LP) || priority(token) > priority(op_stack.peek())) {
                            op_stack.push(token);
                        } else {
                            while (!op_stack.isEmpty() && !("(".equals(op_stack.peek().getData()) && op_stack.peek().getType() == Token.LP)) {
                                if (priority(token) <= priority(op_stack.peek())) {
                                    suffixList.add(op_stack.pop());
                                }
                                if (op_stack.isEmpty()) break;
                                if (priority(token) > priority(op_stack.peek())) break;
                            }
                            op_stack.push(token);
                        }
                    }
                    isUnary = true;
                } else if (isNumber(token)) {
                    if (isUnary) {
                        isUnary = false;
                    }
                    suffixList.add(token);
                } else if (token.getType() == Token.KEY) {
                    if (isUnary) {
                        isUnary = false;
                    }
                    if (token.getData().equals("true") || token.getData().equals("false") || token.getData().equals("null"))
                        suffixList.add(token);
                    else throw new CompileException("Illegal keywords.", token, parser.getFilename(), parser);
                } else {
                    Token name = token;
                    if(compiler.getLibnames().contains(name.getData())){
                        suffixList.add(new CallPointer(name));
                        continue;
                    }

                    token = getToken(tds);
                    if (token == null) {
                        suffixList.add(name);
                        break;
                    }


                    if (token.getType() == Token.LP && "[".equals(token.getData())) {
                        int ibl = 1;
                        List<Token> ts = new LinkedList<>();
                        List<ASTNode> bcs = new LinkedList<>(), var = new LinkedList<>();
                        do {
                            token = getToken(tds);
                            if (token.getType() == Token.LP && "[".equals(token.getData())) {
                                ibl += 1;
                            }
                            if (token.getType() == Token.LR && "]".equals(token.getData())) {
                                ibl -= 1;
                            }
                            if (ibl <= 0) {
                                break;
                            }
                            ts.add(token);
                        } while (true);
                        ExpressionParsing ep = new ExpressionParsing(ts, parser,compiler);

                        var.add(new MovVarNode(name.getData()));
                        var.add(new GroupASTNode(ep.eval(parser, compiler, tos)));

                        bcs.add(new InvokeASTNode("array", "get_object", var));

                        suffixList.add(new TokenX(new GroupASTNode(bcs)));
                    } else if (token.getType() == Token.LP && "(".equals(token.getData())) {
                        token = getToken(tds);

                        /*
                        if(name.getType() != Token.NAME)
                            throw new CompileException("Unable to resolve symbols.",token, parser.getFilename(), parser);
                         */
                        ArrayList<ASTNode> values = new ArrayList<>();

                        try {
                            do {
                                ArrayList<Token> tds = new ArrayList<>();
                                int index = 1;
                                do {
                                    if (token.getType() == Token.LP && "(".equals(token.getData())) {
                                        tds.add(token);
                                        index += 1;
                                    }
                                    if ((token.getType() == Token.SEM && ",".equals(token.getData()) && index <= 0)) {
                                        token = getToken(ExpressionParsing.this.tds);
                                        ExpressionParsing inble = new ExpressionParsing(tds, parser, compiler);
                                        values.add(new GroupASTNode(inble.calculate(inble.transitSuffix(tos))));
                                        tds.clear();
                                        continue;
                                    }
                                    if (token.getType() == Token.LR && ")".equals(token.getData()) && index > 0) {
                                        index -= 1;
                                        tds.add(token);
                                    }
                                    if (token.getType() == Token.LR && ")".equals(token.getData()) && index <= 0) {

                                        Token tdd = tds.get(0);
                                        if (!(tdd.getType() == Token.LP && "(".equals(tdd.getData()))) {
                                            tdd = tds.get(tds.size() - 1);
                                            if (tdd.getType() == Token.LR && ")".equals(tdd.getData())) {
                                                tds.remove(tds.size() - 1);
                                            }
                                        }

                                        ExpressionParsing inble = new ExpressionParsing(tds, parser, compiler);
                                        values.add(new GroupASTNode(inble.calculate(inble.transitSuffix(tos))));

                                        tds.clear();
                                        break;
                                    }
                                    tds.add(token);
                                    token = getToken(ExpressionParsing.this.tds);
                                } while (true);
                            } while (!(token.getType() == Token.LR && ")".equals(token.getData())));
                        } catch (IndexOutOfBoundsException ignored) {
                            throw new CompileException("')' expected.", token, parser.getFilename(), parser);
                        }
                        Collections.reverse(values);

                        suffixList.add(new CallBuffer(name, values));
                    } else {
                        buffer = token;
                        suffixList.add(name);
                    }
                }

                /*else if (token.getType() == Token.NAME) {
                    if (isUnary) {
                        isUnary = false; // 单目运算符标志位更新
                    }

                    Token name = token;
                    if (compiler.getLibnames().contains(name.getData())) {
                        suffixList.add(new CallPointer(name));
                        continue;
                    }
                    if (token.getType() == Token.END) break;

                    token = getToken(tds);
                    if (token == null) {
                        suffixList.add(name);
                        break;
                    }

                    if (token.getType() == Token.LP && token.getData().equals("[")) {
                        int ibl = 1;
                        List<Token> ts = new LinkedList<>();
                        List<ASTNode> bcs = new LinkedList<>(), var = new LinkedList<>();
                        do {
                            token = getToken(tds);
                            if (token.getType() == Token.LP && token.getData().equals("[")) ibl += 1;
                            if (token.getType() == Token.LR && token.getData().equals("]")) ibl -= 1;
                            if (ibl <= 0) break;
                            ts.add(token);
                        } while (true);
                        ExpressionParsing ep = new ExpressionParsing(ts, parser, compiler);

                        var.add(new MovVarNode(name.data));
                        var.add(new GroupASTNode(ep.calculate(ep.transitSuffix(tos))));

                        bcs.add(new InvokeASTNode("array", "get_object", var));

                        suffixList.add(new TokenX(new GroupASTNode(bcs)));
                        continue;
                    } else buffer = token;
                    suffixList.add(name);
                } else if ("(".equals(token.getData()) && token.getType() == Token.LP) {
                    op_stack.push(token);
                } else if (")".equals(token.getData()) && token.getType() == Token.LR) {
                    while (!op_stack.isEmpty()) {
                        if ("(".equals(op_stack.peek().getData()) && op_stack.peek().getType() == Token.LP) {
                            op_stack.pop();
                            break;
                        } else {
                            suffixList.add(op_stack.pop());
                        }
                    }
                } else if (token.getType() == Token.END) break;
                else throw new CompileException("Unable to resolve symbols.", token, parser.getFilename(), parser);
                */
            } catch (NullPointerException e) {
                throw new CompileException("Illegal combination of expressions.", parser.getFilename());
            }
        }

        while (!op_stack.isEmpty()) {
            suffixList.add(op_stack.pop());
        }


        for (Token t : suffixList)
            if (t.getType() == Token.LP && t.getData().equals("("))
                throw new CompileException("')' expected.", t, parser.getFilename(), parser);

        return suffixList;
    }

    private static boolean isUnaryOperator(Token token, boolean isU) {
        if (token.getType() != Token.SEM) return false;
        return isU && (token.getData().equals("-") || token.getData().equals("++") || token.getData().equals("--"));
    }

    private static boolean isNumber(Token token) {
        return token.getType() == Token.INTEGER || token.getType() == Token.DOUBLE || token.getType() == Token.STRING;
    }

    private static boolean isOperator(Token op) {
        if (op.getType() != Token.SEM) return false;
        return OP_DATA.contains(op.getData());
    }

    private static int priority(Token op) {
        if (op.getType() != Token.SEM) return -1;

        return switch (op.getData()) {
            case "." -> 11;
            case "!", "++", "--" -> 10;
            case "*", "/", "*=", "/=", "%", "%=" -> 9;
            case "+", "-", "+=", "-=" -> 8;
            case ">", ">=", "<=", "<" -> 7;
            case "==" -> 6;
            case "&" -> 5;
            case "^" -> 4;
            case "|" -> 3;
            case "&&", "||" -> 2;
            case "=" -> 1;
            case "," -> 0;
            default -> -1;
        };
    }

    public List<ASTNode> calculate(List<Token> suffx) {
        List<ASTNode> bbc = new LinkedList<>();

        boolean co1 = true;
        for (Token token : suffx) {
            if (token.getType() == Token.NAME ||
                    token.getType() == Token.EXP ||
                    token.getType() == Token.CALL ||
                    token.getType() == Token.POINTER) {
                co1 = false;
                break;
            }
        }
        if (co1) {

            OptimizationExecutor executor = new OptimizationExecutor(suffx, parser);
            ASTNode node = executor.eval();

            if (node == null) {
                ConsoleModel.getOutput().warn("Expression (line: " + suffx.get(0).line + ") cannot be evaluated in o1 mode.");
            } else return Collections.singletonList(node);
        }

        for (Token td : suffx) {
            if (td.getType() == Token.NAME) bbc.add(new MovVarNode(td.getData()));
            else if (td.getType() == Token.KEY) {
                switch (td.getData()) {
                    case "true" -> bbc.add(new PushNode(new ExBool(true)));
                    case "false" -> bbc.add(new PushNode(new ExBool(false)));
                    case "null" -> bbc.add(new PushNode(new ExNull()));
                    default -> throw new CompileException("Illegal keywords.", td, parser.getFilename(), parser);
                }
            } else if (td.getType() == Token.INTEGER) bbc.add(new PushNode(new ExInt(Integer.parseInt(td.getData()))));
            else if (td.getType() == Token.DOUBLE)
                bbc.add(new PushNode(new ExDouble(Double.parseDouble(td.getData()))));
            else if (td.getType() == Token.SEM) {
                switch (td.getData()) {
                    case "+" -> bbc.add(new AddNode());
                    case "-" -> {
                        if (td instanceof UToken) {
                            bbc.add(new VNotNode());
                        } else bbc.add(new SubNode());
                    }
                    case "*" -> bbc.add(new MulNode());
                    case "/" -> bbc.add(new DivNode());
                    case "==" -> bbc.add(new EquNode());
                    case ">=" -> bbc.add(new BigEquNode());
                    case "<=" -> bbc.add(new LessEquNode());
                    case ">" -> bbc.add(new BigNode());
                    case "<" -> bbc.add(new LessNode());
                    case "!" -> bbc.add(new NotNode());
                    case "&" -> bbc.add(new BitAndNode());
                    case "|" -> bbc.add(new BitOrNode());
                    case "=" -> bbc.add(new MovNode());
                    case "+=" -> bbc.add(new AddMovNode());
                    case "-=" -> bbc.add(new SubMovNode());
                    case "%" -> bbc.add(new DivXNode());
                    case "%=" -> bbc.add(new DivXMovNode());
                    case "++" -> bbc.add(new AddXNode());
                    case "&&" -> bbc.add(new AndNode());
                    case "||" -> bbc.add(new OrNode());
                    case "." -> bbc.add(new CallNode());
                }
            } else if (td.getType() == Token.STRING) bbc.add(new PushNode(new ExString(td.getData())));
            else if (td.getType() == Token.EXP) bbc.add(((TokenX) td).getBc());
            else if(td.getType() == Token.CALL){
                CallBuffer cb = (CallBuffer) td;
                bbc.add(new PushNode(new ExMethod(cb.getMethodName().getData(),new GroupASTNode(new LinkedList<>(){
                    {
                        addAll( cb.getArgsParser());
                    }
                }))));
            } else if(td.getType() == Token.POINTER){
                CallPointer spb = (CallPointer) td;
                bbc.add(new PushNode(new ExClass(spb.getName().getData())));
            }
        }

        return bbc;
    }

    @Override
    public ASTNode eval(Parser parser, Compiler compiler, Set<String> tos) throws CompileException {
        return new GroupASTNode(calculate(transitSuffix(tos)));
    }
}
