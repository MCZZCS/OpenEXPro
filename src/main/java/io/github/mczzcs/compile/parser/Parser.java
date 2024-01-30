package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.ExpressionParsing;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.util.CompileException;
import io.github.mczzcs.compile.Compiler;

import java.util.LinkedList;
import java.util.List;

public class Parser {

    List<Token> tds;
    int index;
    Token buffer;
    String filename;

    public String getFilename() {
        return filename;
    }

    public Parser(List<Token> tds, String filename) {

        this.tds = tds;
        index = 0;
        this.filename = filename;
    }

    private Token getToken() {
        if (buffer != null) {
            Token r = buffer;
            buffer = null;
            return r;
        } else {
            if (index >= tds.size()) return null;
            Token t = tds.get(index);
            index += 1;
            return t;
        }
    }

    public BaseParser getParser(Compiler c) {

        Token buf;
        buf = getToken();
        if (buf == null) return null;
        if (buf.getType() == Token.KEY) {
            switch (buf.getData()) {
                case "include" -> {
                    List<Token> tds = new LinkedList<>();
                    do {
                        buf = getToken();
                        tds.add(buf);
                    } while (buf.getType() != Token.END);
                    return new IncludeParser(tds);
                }
                case "value" -> {
                    List<Token> tds = new LinkedList<>();
                    do {
                        buf = getToken();
                        tds.add(buf);
                    } while (buf.getType() != Token.END);
                    return new ValueParser(tds);
                }
                case "throw" -> {
                    List<Token> tds = new LinkedList<>();
                    do {
                        buf = getToken();
                        tds.add(buf);
                    } while (buf.getType() != Token.END);
                    return new ThrowParser(tds);
                }
                case "try" -> {
                    List<Token> try_group = new LinkedList<>(),catch_expression = new LinkedList<>(), catch_group = new LinkedList<>();
                    Token t = getToken();
                    int i = 1;
                    if (!(t.getType() == Token.LP && "{".equals(t.getData()))) {
                        throw new CompileException("Missing statement body.", t, getFilename(),this);
                    }
                    try {
                        do {
                            t = getToken();
                            if (t.getType() == Token.LP && "{".equals(t.getData())) {
                                i += 1;
                            }
                            if (t.getType() == Token.LR && "}".equals(t.getData())) {
                                i -= 1;
                            }
                            if (i == 0) {
                                break;
                            }
                            try_group.add(t);
                        } while (true);
                    } catch (NullPointerException e) {
                        throw new CompileException("'}' expected.", filename);
                    }

                    Token bufff = t;
                    t = getToken();
                    if (t == null) {
                        throw new CompileException("'catch' expected.",bufff,filename,this);
                    }
                    if(t.getType()!=Token.KEY&&!"catch".equals(t.getData())) {
                        throw new CompileException("'catch' expected.",t,filename,this);
                    }

                    do {
                        t = getToken();
                        catch_expression.add(t);
                    } while (!(t.getType() == Token.LP && "{".equals(t.getData())));

                    i = 1;
                    try {
                        do {
                            t = getToken();
                            if (t.getType() == Token.LP && "{".equals(t.getData())) {
                                i += 1;
                            }
                            if (t.getType() == Token.LR && "}".equals(t.getData())) {
                                i -= 1;
                            }
                            if (i == 0) {
                                break;
                            }
                            catch_group.add(t);
                        } while (true);
                    } catch (NullPointerException e) {
                        throw new CompileException("'}' expected.", filename);
                    }

                    return new TryParser(new SubParser(try_group,this,c,false,false,null).getParsers() ,catch_expression,new SubParser(catch_group,this,c,false,false,null).getParsers());
                }
                case "function" -> {
                    List<Token> vars = new LinkedList<>(), groups = new LinkedList<>();
                    Token t = getToken();
                    if (!(t.getType() == Token.NAME))
                        throw new CompileException("Type name is not valid.", t, getFilename(),this);
                    Token name = t;
                    t = getToken();
                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, getFilename(),this);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LR && t.getData().equals(")")) break;
                        if (t.getType() == Token.NAME) c.value_names.add(t.getData());
                        vars.add(t);
                    } while (true);
                    t = getToken();
                    int i = 1;
                    if (!(t.getType() == Token.LP && t.getData().equals("{")))
                        throw new CompileException("Missing function body.", t, getFilename(),this);
                    try {
                        do {
                            t = getToken();
                            if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                            if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                            if (i == 0) break;
                            groups.add(t);
                        } while (true);
                    } catch (NullPointerException e) {
                        throw new CompileException("'}' expected.", filename);
                    }

                    if(c.function_names.contains(name.getData()))
                        throw new CompileException("The function is already defined.",name,filename,this);

                    FunctionParser parser = new FunctionParser();
                    parser.setFunctionName(name.getData());
                    c.function_names.add(name.getData());
                    parser.setVars(vars);
                    parser.setParsers(new FunctionXParser(groups, this, c, parser).getParsers());
                    return parser;

                }
                case "if" -> {
                    List<Token> vars = new LinkedList<>(), groups = new LinkedList<>()
                            , else_g = new LinkedList<>();
                    List<ElseIfParser> elseIfParsers = new LinkedList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, filename,this);
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
                        throw new CompileException("Missing statement body.", t, getFilename(),this);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);
                    ExpressionParsing e = new ExpressionParsing(vars, this, c);

                    t = getToken();
                    if (t == null)
                        return new IfParser(e, new SubParser(groups, this, c, false, false, null).getParsers(), elseIfParsers, new LinkedList<>());

                    int j = 1;
                    if (t.getType() == Token.KEY && t.getData().equals("else")) {
                        t = getToken();
                        if (!(t.getType() == Token.LP && t.getData().equals("{")))
                            throw new CompileException("Missing statement body.", t, getFilename(),this);
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
                                        throw new CompileException("Missing statement body.", t, getFilename(),this);
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
                            eparser = getElseIf(c, this);
                            elseIfParsers.add(eparser);
                        }
                    } else buffer = t;

                    return new IfParser(e, new SubParser(groups, this, c, false, false, null).getParsers(), elseIfParsers, new SubParser(else_g, this, c, false, false, null).getParsers());
                }
                case "while" -> {
                    List<Token> vars = new LinkedList<>(), groups = new LinkedList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, this.filename,this);
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
                        throw new CompileException("Missing statement body.", t, this.filename,this);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);
                    ExpressionParsing e = new ExpressionParsing(vars, this, c);
                    return new WhileParser(e, new SubParser(groups, this, c, false, true, null).getParsers(), null);
                }
                case "for" -> {
                    List<Token> vars = new LinkedList<>(), groups = new LinkedList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, this.filename,this);
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
                        throw new CompileException("Missing statement body.", t, this.filename,this);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);

                    List<Token> one_tokens = new LinkedList<>(),
                            two_tokens = new LinkedList<>(),
                            three_tokens = new LinkedList<>();
                    try {
                        int j = 0;
                        for (; j < vars.size(); j++) {
                            Token token = vars.get(j);
                            if (token.getType() == Token.END){
                                j++;
                                break;
                            }
                            one_tokens.add(token);
                        }
                        for (; j < vars.size(); j++) {
                            Token token = vars.get(j);
                            if (token.getType() == Token.END){
                                j++;
                                break;
                            }
                            two_tokens.add(token);
                        }
                        for (int varsSize = vars.size(); j < varsSize; j++) {
                            Token token = vars.get(j);
                            three_tokens.add(token);
                        }
                    }catch (IndexOutOfBoundsException ignored){
                        throw new CompileException("Illegal combination of expressions.",t,filename,this);
                    }

                    BaseParser node_init = new SubParser(one_tokens,this,c,false,false,null).getParser();

                    BaseParser nodes = new VoidParser(),exp = new VoidParser();
                    if(!two_tokens.isEmpty()) {
                        ExpressionParsing bool_expression = new ExpressionParsing(two_tokens, this, c);
                        nodes = bool_expression;
                    }
                    if(!three_tokens.isEmpty()) {
                        ExpressionParsing expression = new ExpressionParsing(three_tokens, this, c);
                        exp = expression;
                    }

                    return new ForParser(node_init,nodes,exp, new SubParser(groups, this, c, false, true, null).getParsers(), null);
                }
                case "break","continue" -> throw new CompileException("Back outside loop", buf, filename,this);
                default -> throw new CompileException("Not a statement.", buf, getFilename(),this);
            }
        } else if (buf.getType() == Token.NAME) {
            List<Token> tds = new LinkedList<>();
            tds.add(buf);
            do {
                buf = getToken();
                tds.add(buf);
            } while (buf.getType() != Token.END);
            if (c.getLibnames().contains(buf.getData())) {
                return new InvokeParser(tds);
            }
            return new ExpParser(tds, null);

        } else if (buf.getType() == Token.SEM) {
            return null;
        } else throw new CompileException("Illegal start of expression.", buf, filename,this);
    }

    private ElseIfParser getElseIf(Compiler c, Parser parser) {
        List<Token> vars = new LinkedList<>(), groups = new LinkedList<>();

        Token t = getToken();
        if (!(t.getType() == Token.LP && t.getData().equals("(")))
            throw new CompileException("'(' expected.", t, filename,parser);
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
            throw new CompileException("Missing statement body.", t, getFilename(),parser);
        do {
            t = getToken();
            if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
            if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
            if (i == 0) break;
            groups.add(t);
        } while (true);
        ExpressionParsing e = new ExpressionParsing(vars, this, c);
        return new ElseIfParser(e.calculate(e.transitSuffix(null)), new SubParser(groups, parser, c, false, false, null).getParsers());
    }


    public static class SubParser {
        List<Token> tds;
        int index;
        Parser parser;
        Compiler compiler;
        FunctionParser fparser;
        Token buffer;
        boolean function;
        boolean while_st;

        public SubParser(List<Token> tds, Parser parser, Compiler compiler, boolean function, boolean while_st, FunctionParser fparser) {
            this.tds = tds;
            index = 0;
            this.parser = parser;
            this.compiler = compiler;
            this.function = function;
            this.while_st = while_st;
            this.fparser = fparser;
        }

        private Token getToken() {
            if (buffer != null) {
                Token r = buffer;
                buffer = null;
                return r;
            } else {
                if (index >= tds.size()) return null;
                Token t = tds.get(index);
                index += 1;
                return t;
            }
        }

        BaseParser getParser() {

            Token buf;
            buf = getToken();
            if (buf == null) return null;
            if (buf.getType() == Token.KEY) {
                switch (buf.getData()) {
                    case "value" -> {
                        List<Token> tds = new LinkedList<>();
                        do {
                            buf = getToken();
                            tds.add(buf);
                            if(buf == null)break;
                        } while (buf.getType() != Token.END);
                        return new ValueParser(tds);
                    }
                    case "if" -> {
                        List<Token> vars = new LinkedList<>(), groups = new LinkedList<>()
                                , else_g = new LinkedList<>();
                        List<ElseIfParser> elseIfParsers = new LinkedList<>();
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

                        t = getToken();
                        if (t == null)
                            return new IfParser(e, new SubParser(groups, parser, compiler, function, while_st, fparser).getParsers(), elseIfParsers, new LinkedList<>());

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
                                eparser = getElseIf(compiler, parser, fparser, function, while_st);
                                elseIfParsers.add(eparser);
                            }
                        } else buffer = t;

                        return new IfParser(e, new SubParser(groups, parser, compiler, function, while_st, fparser).getParsers(), elseIfParsers, new SubParser(else_g, parser, compiler, function, while_st, fparser).getParsers());
                    }
                    case "try" -> {
                        List<Token> try_group = new LinkedList<>(),catch_expression = new LinkedList<>(), catch_group = new LinkedList<>();
                        Token t = getToken();
                        int i = 1;
                        if (!(t.getType() == Token.LP && "{".equals(t.getData()))) {
                            throw new CompileException("Missing statement body.", t, parser.filename, parser);
                        }
                        try {
                            do {
                                t = getToken();
                                if (t.getType() == Token.LP && "{".equals(t.getData())) {
                                    i += 1;
                                }
                                if (t.getType() == Token.LR && "}".equals(t.getData())) {
                                    i -= 1;
                                }
                                if (i == 0) {
                                    break;
                                }
                                try_group.add(t);
                            } while (true);
                        } catch (NullPointerException e) {
                            throw new CompileException("'}' expected.", parser.filename);
                        }

                        Token bufff = t;
                        t = getToken();
                        if (t == null) {
                            throw new CompileException("'catch' expected.",bufff, parser.filename, parser);
                        }
                        if(t.getType()!=Token.KEY&&!"catch".equals(t.getData())) {
                            throw new CompileException("'catch' expected.",t, parser.filename, parser);
                        }

                        do {
                            t = getToken();
                            catch_expression.add(t);
                        } while (!(t.getType() == Token.LP && "{".equals(t.getData())));

                        i = 1;
                        try {
                            do {
                                t = getToken();
                                if (t.getType() == Token.LP && "{".equals(t.getData())) {
                                    i += 1;
                                }
                                if (t.getType() == Token.LR && "}".equals(t.getData())) {
                                    i -= 1;
                                }
                                if (i == 0) {
                                    break;
                                }
                                catch_group.add(t);
                            } while (true);
                        } catch (NullPointerException e) {
                            throw new CompileException("'}' expected.", parser.filename);
                        }

                        return new TryParser(new SubParser(try_group,parser,compiler,function,while_st,fparser).getParsers() ,catch_expression,new SubParser(catch_group,parser,compiler,function,while_st,fparser).getParsers());
                    }
                    case "throw" -> {
                        List<Token> tds = new LinkedList<>();
                        do {
                            buf = getToken();
                            tds.add(buf);
                        } while (buf.getType() != Token.END);
                        return new ThrowParser(tds);
                    }
                    case "while" -> {
                        List<Token> vars = new LinkedList<>(), groups = new LinkedList<>();
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
                        return new WhileParser(e, new SubParser(groups, parser, compiler, function, true, fparser).getParsers(), fparser);
                    }
                    case "for" -> {
                        List<Token> vars = new LinkedList<>(), groups = new LinkedList<>();
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

                        List<Token> one_tokens = new LinkedList<>(),
                                two_tokens = new LinkedList<>(),
                                three_tokens = new LinkedList<>();
                        try {
                            int j = 0;
                            for (; j < vars.size(); j++) {
                                Token token = vars.get(j);
                                if (token.getType() == Token.END){
                                    j++;
                                    break;
                                }
                                one_tokens.add(token);
                            }
                            for (; j < vars.size(); j++) {
                                Token token = vars.get(j);
                                if (token.getType() == Token.END){
                                    j++;
                                    break;
                                }
                                two_tokens.add(token);
                            }
                            for (int varsSize = vars.size(); j < varsSize; j++) {
                                Token token = vars.get(j);
                                three_tokens.add(token);
                            }
                        }catch (IndexOutOfBoundsException ignored){
                            throw new CompileException("Illegal combination of expressions.",t, parser.filename,parser);
                        }

                        BaseParser node_init = new SubParser(one_tokens,parser,compiler,function,while_st,fparser).getParser();

                        BaseParser nodes = new VoidParser(),exp = new VoidParser();
                        if(!two_tokens.isEmpty()) {
                            nodes = new ExpressionParsing(two_tokens, parser, compiler);
                        }
                        if(!three_tokens.isEmpty()) {
                            exp = new ExpressionParsing(three_tokens, parser, compiler);
                        }

                        return new ForParser(node_init,nodes,exp, new SubParser(groups, parser, compiler, function, while_st, fparser).getParsers(), fparser);
                    }
                    case "break" -> {
                        if (while_st) {
                            List<Token> tds = new LinkedList<>();
                            do {
                                buf = getToken();
                                if (buf.getType() == Token.END) break;
                                tds.add(buf);
                            } while (true);

                            if(!tds.isEmpty()) throw new CompileException("Illegal combination of expressions.",buf, parser.filename,parser);

                            return new BackParser();
                        } else throw new CompileException("Back outside loop", buf, parser.filename,parser);
                    }
                    case "continue" -> {
                        if (while_st) {
                            List<Token> tds = new LinkedList<>();
                            do {
                                buf = getToken();
                                if (buf.getType() == Token.END) break;
                                tds.add(buf);
                            } while (true);

                            if(!tds.isEmpty()) throw new CompileException("Illegal combination of expressions.",buf, parser.filename,parser);

                            return new ContinueParser();
                        } else throw new CompileException("Back outside loop", buf, parser.filename,parser);
                    }
                    case "return" -> {
                        if (function) {
                            List<Token> tds = new LinkedList<>();
                            do {
                                buf = getToken();
                                if (buf.getType() == Token.END) break;
                                tds.add(buf);
                            } while (true);
                            return new ReturnParser(tds, fparser);
                        } else throw new CompileException("Return outside function.", buf, parser.filename,parser);
                    }
                    default -> throw new CompileException("Not a statement.", buf, parser.filename,parser);
                }
            } else if (buf.getType() == Token.NAME) {
                List<Token> tds = new LinkedList<>();
                tds.add(buf);
                do {
                    buf = getToken();
                    tds.add(buf);
                } while (buf.getType() != Token.END);
                if (compiler.getLibnames().contains(buf.getData())) {
                    return new InvokeParser(tds);
                }
                return new ExpParser(tds, fparser);

            } else throw new CompileException("Illegal start of expression.", buf, parser.filename,parser);
        }

        public List<BaseParser> getParsers() {
            List<BaseParser> bp = new LinkedList<>();
            BaseParser bpp;
            while ((bpp = getParser()) != null) bp.add(bpp);
            return bp;
        }

        private ElseIfParser getElseIf(Compiler c, Parser parser, FunctionParser fparser, boolean function, boolean while_st) {
            List<Token> vars = new LinkedList<>(), groups = new LinkedList<>();

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
            return new ElseIfParser(e.calculate(e.transitSuffix(compiler.value_names)), new SubParser(groups, parser, c, function, while_st, fparser).getParsers());
        }
    }

    public List<Token> getTds() {
        return tds;
    }
}
