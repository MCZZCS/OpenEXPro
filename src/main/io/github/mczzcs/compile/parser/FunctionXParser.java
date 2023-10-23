package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.ExpressionParsing;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.util.CompileException;

import java.io.EOFException;
import java.util.ArrayList;

public class FunctionXParser {
    ArrayList<Token> tds;
    int index;
    Parser parser;
    Compiler compiler;
    Token buffer;
    FunctionParser func_parser;

    public FunctionXParser(ArrayList<Token> tds, Parser parser, Compiler compiler, FunctionParser func_parser) {
        this.tds = tds;
        index = 0;
        this.parser = parser;
        this.compiler = compiler;
        this.func_parser = func_parser;
    }

    private Token getToken() throws EOFException {
        if (buffer != null) {
            Token r = buffer;
            buffer = null;
            return r;
        } else {
            if (index >= tds.size()) throw new EOFException();
            Token t = tds.get(index);
            index += 1;
            return t;
        }
    }

    private BaseParser getParser() throws EOFException {

        Token buf;
        buf = getToken();
        if (buf.getType() == Token.KEY) {
            switch (buf.getData()) {
                case "value" -> {
                    ArrayList<Token> tds = new ArrayList<>();
                    do {
                        buf = getToken();
                        tds.add(buf);
                    } while (buf.getType() != Token.END);
                    return new BufParser(new ValueParser(tds).evalPreVar(parser, compiler, func_parser));
                }
                case "if" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>(), else_g = new ArrayList<>();
                    ArrayList<ElseIfParser> elseIfParsers = new ArrayList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, parser.filename,parser);
                    t = getToken();
                    int index = 1;
                    do {
                        if (t.getType() == Token.LP && t.getData().equals("(")) {
                            vars.add(t);
                            index += 1;
                        }
                        if (t.getType() == Token.LR && t.getData().equals(")") && index > 0) {
                            index -= 1;
                            vars.add(t);
                        }
                        if (t.getType() == Token.LR && t.getData().equals(")") && index <= 0) {
                            for (Token tddebug : vars) {
                                if (tddebug.getType() == Token.NAME || tddebug.getType() == Token.KEY) {
                                    break;
                                }
                            }
                            break;
                        }
                        vars.add(t);
                        t = getToken();
                    } while (true);
                    t = getToken();
                    int i = 1;
                    if (!(t.getType() == Token.LP && t.getData().equals("{")))
                        throw new CompileException("Missing statement body.", t, parser.filename,parser);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);
                    ExpressionParsing e = new ExpressionParsing(vars, parser, compiler);
                    try {
                        t = getToken();
                    } catch (EOFException e11) {
                        return new IfParser(e.calculate(e.transitSuffix(func_parser)), new Parser.SubParser(groups, parser, compiler, true, false, func_parser).getParsers(), elseIfParsers, new ArrayList<>());
                    }
                    int j = 1;
                    if (t.getType() == Token.KEY && t.getData().equals("else")) {
                        t = getToken();
                        if (!(t.getType() == Token.LP && t.getData().equals("{")))
                            throw new CompileException("Missing statement body.", t, parser.filename,parser);
                        do {
                            t = getToken();
                            if (t.getType() == Token.LP && t.getData().equals("{")) j += 1;
                            if (t.getType() == Token.LR && t.getData().equals("}")) j -= 1;
                            if (j == 0) break;
                            else_g.add(t);
                        } while (true);
                    } else if (t.getType() == Token.KEY && t.getData().equals("elif")) {
                        ElseIfParser eparser;
                        boolean isFirst = true;
                        while (true) {
                            if (!isFirst) {
                                t = getToken();
                                if (t.getType() == Token.KEY && t.getData().equals("else")) {
                                    t = getToken();
                                    if (!(t.getType() == Token.LP && t.getData().equals("{")))
                                        throw new CompileException("Missing statement body.", t, parser.filename,parser);
                                    do {
                                        t = getToken();
                                        if (t.getType() == Token.LP && t.getData().equals("{")) j += 1;
                                        if (t.getType() == Token.LR && t.getData().equals("}")) j -= 1;
                                        if (j == 0) break;
                                        else_g.add(t);
                                    } while (true);
                                    break;
                                } else if (t.getType() == Token.KEY && t.getData().equals("elif")) {

                                } else {
                                    buffer = t;
                                    break;
                                }
                            }
                            isFirst = false;
                            eparser = getElseIf(compiler, parser, func_parser);
                            if (eparser == null) break;
                            elseIfParsers.add(eparser);
                        }
                    } else buffer = t;

                    return new IfParser(e.calculate(e.transitSuffix(func_parser)), new Parser.SubParser(groups, parser, compiler, true, false, func_parser).getParsers(), elseIfParsers, new Parser.SubParser(else_g, parser, compiler, true, false, func_parser).getParsers());
                }
                case "while" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, parser.filename,parser);
                    t = getToken();
                    int index = 1;
                    do {
                        if (t.getType() == Token.LP && t.getData().equals("(")) {
                            vars.add(t);
                            index += 1;
                        }
                        if (t.getType() == Token.LR && t.getData().equals(")") && index > 0) {
                            index -= 1;
                            vars.add(t);
                        }
                        if (t.getType() == Token.LR && t.getData().equals(")") && index <= 0) {
                            for (Token tddebug : vars) {
                                if (tddebug.getType() == Token.NAME || tddebug.getType() == Token.KEY) {
                                    break;
                                }
                            }
                            break;
                        }
                        vars.add(t);
                        t = getToken();
                    } while (true);
                    t = getToken();
                    int i = 1;
                    if (!(t.getType() == Token.LP && t.getData().equals("{")))
                        throw new CompileException("Missing statement body.", t, parser.filename,parser);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);
                    ExpressionParsing e = new ExpressionParsing(vars, parser, compiler);
                    return new WhileParser(e.calculate(e.transitSuffix(func_parser)), new Parser.SubParser(groups, parser, compiler, true, true, func_parser).getParsers(), func_parser);
                }
                case "for" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, parser.filename,parser);
                    t = getToken();
                    int index = 1;
                    do {
                        if (t.getType() == Token.LP && t.getData().equals("(")) {
                            vars.add(t);
                            index += 1;
                        }
                        if (t.getType() == Token.LR && t.getData().equals(")") && index > 0) {
                            index -= 1;
                            vars.add(t);
                        }
                        if (t.getType() == Token.LR && t.getData().equals(")") && index <= 0) {
                            for (Token tddebug : vars) {
                                if (tddebug.getType() == Token.NAME || tddebug.getType() == Token.KEY) {
                                    break;
                                }
                            }
                            break;
                        }
                        vars.add(t);
                        t = getToken();
                    } while (true);
                    t = getToken();
                    int i = 1;
                    if (!(t.getType() == Token.LP && t.getData().equals("{")))
                        throw new CompileException("Missing statement body.", t, parser.filename,parser);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);

                    ArrayList<Token> one_tokens = new ArrayList<>(),
                            two_tokens = new ArrayList<>(),
                            three_tokens = new ArrayList<>();
                    try {
                        int j = 0;
                        for (; j < vars.size(); j++) {
                            Token token = vars.get(j);
                            if (token.getType() == Token.END) {
                                j++;
                                break;
                            }
                            one_tokens.add(token);
                        }
                        for (; j < vars.size(); j++) {
                            Token token = vars.get(j);
                            if (token.getType() == Token.END) {
                                j++;
                                break;
                            }
                            two_tokens.add(token);
                        }
                        for (int varsSize = vars.size(); j < varsSize; j++) {
                            Token token = vars.get(j);
                            three_tokens.add(token);
                        }
                    } catch (IndexOutOfBoundsException ignored) {
                        throw new CompileException("Illegal combination of expressions.", t, parser.filename,parser);
                    }

                    ASTNode node_init = new Parser.SubParser(one_tokens, parser, compiler, true, false, func_parser).getParser().eval(parser, compiler, func_parser);

                    ArrayList<ASTNode> nodes = new ArrayList<>(),exp = new ArrayList<>();
                    if (!two_tokens.isEmpty()) {
                        ExpressionParsing bool_expression = new ExpressionParsing(two_tokens, parser, compiler);
                        nodes.addAll(bool_expression.calculate(bool_expression.transitSuffix(null)));
                    }
                    if (!three_tokens.isEmpty()) {
                        ExpressionParsing expression = new ExpressionParsing(three_tokens, parser, compiler);
                        exp.addAll(expression.calculate(expression.transitSuffix(null)));
                    }

                    return new ForParser(node_init, nodes,exp, new Parser.SubParser(groups, parser, compiler, true, true, func_parser).getParsers(), func_parser);
                }
                case "break","continue" -> {
                    throw new CompileException("Back outside loop", buf, parser.filename,parser);
                }
                case "return" -> {
                    ArrayList<Token> tds = new ArrayList<>();
                    do {
                        buf = getToken();
                        if (buf.getType() == Token.END) break;
                        tds.add(buf);
                    } while (true);
                    return new ReturnParser(tds, func_parser);
                }
                default -> throw new CompileException("Not a statement.", buf, parser.filename,parser);
            }
        } else if (buf.getType() == Token.NAME) {
            ArrayList<Token> tds = new ArrayList<>();
            tds.add(buf);
            do {
                buf = getToken();
                tds.add(buf);
            } while (buf.getType() != Token.END);
            if (compiler.getLibnames().contains(buf.getData())) {
                return new InvokeParser(tds);
            }
            return new ExpParser(tds, func_parser);
        } else throw new CompileException("Illegal start of expression.", buf, parser.filename,parser);
    }

    public ArrayList<BaseParser> getParsers() {
        ArrayList<BaseParser> bp = new ArrayList<>();
        try {
            while (true) bp.add(getParser());
        } catch (EOFException e) {
        }
        return bp;
    }

    private ElseIfParser getElseIf(Compiler c, Parser parser, FunctionParser fparser) throws EOFException {
        ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();

        Token t = getToken();
        if (!(t.getType() == Token.LP && t.getData().equals("(")))
            throw new CompileException("'(' expected.", t, parser.filename,parser);
        t = getToken();
        int index = 1;
        do {
            if (t.getType() == Token.LP && t.getData().equals("(")) {
                vars.add(t);
                index += 1;
            }
            if (t.getType() == Token.LR && t.getData().equals(")") && index > 0) {
                index -= 1;
                vars.add(t);
            }
            if (t.getType() == Token.LR && t.getData().equals(")") && index <= 0) break;

            vars.add(t);
            t = getToken();
        } while (true);
        t = getToken();
        int i = 1;
        if (!(t.getType() == Token.LP && t.getData().equals("{")))
            throw new CompileException("Missing statement body.", t, parser.filename,parser);
        do {
            t = getToken();
            if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
            if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
            if (i == 0) break;
            groups.add(t);
        } while (true);
        ExpressionParsing e = new ExpressionParsing(vars, parser, c);
        return new ElseIfParser(e.calculate(e.transitSuffix(fparser)), new Parser.SubParser(groups, parser, c, true, false, fparser).getParsers());
    }
}
