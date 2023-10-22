package io.github.mczzcs.compile.parser;

import io.github.mczzcs.compile.ExpressionParsing;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.util.CompileException;
import io.github.mczzcs.compile.Compiler;

import java.util.ArrayList;

public class Parser {

    ArrayList<Token> tds;
    int index;
    Token buffer;
    String filename;

    public String getFilename() {
        return filename;
    }

    public Parser(ArrayList<Token> tds, String filename) {
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
                    ArrayList<Token> tds = new ArrayList<>();
                    do {
                        buf = getToken();
                        tds.add(buf);
                    } while (buf.getType() != Token.END);
                    return new IncludeParser(tds);
                }
                case "value" -> {
                    ArrayList<Token> tds = new ArrayList<>();
                    do {
                        buf = getToken();
                        tds.add(buf);
                    } while (buf.getType() != Token.END);
                    return new ValueParser(tds);
                }
                case "function" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();
                    Token t = getToken();
                    if (!(t.getType() == Token.NAME))
                        throw new CompileException("Type name is not valid.", t, getFilename());
                    String name = t.getData();
                    t = getToken();
                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, getFilename());
                    do {
                        t = getToken();
                        if (t.getType() == Token.LR && t.getData().equals(")")) break;
                        if (t.getType() == Token.NAME) c.value_names.add(t.getData());
                        vars.add(t);
                    } while (true);
                    t = getToken();
                    int i = 1;
                    if (!(t.getType() == Token.LP && t.getData().equals("{")))
                        throw new CompileException("Missing function body.", t, getFilename());
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

                    FunctionParser parser = new FunctionParser();
                    parser.setFunctionName(name);
                    parser.setVars(vars);
                    parser.setParsers(new FunctionXParser(groups, this, c, parser).getParsers());
                    return parser;

                }
                case "if" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>(), else_g = new ArrayList<>();
                    ArrayList<ElseIfParser> elseIfParsers = new ArrayList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, filename);
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
                        throw new CompileException("Missing statement body.", t, getFilename());
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
                        return new IfParser(e.calculate(e.transitSuffix(null)), new SubParser(groups, this, c, false, false, null).getParsers(), elseIfParsers, new ArrayList<>());

                    int j = 1;
                    if (t.getType() == Token.KEY && t.getData().equals("else")) {
                        t = getToken();
                        if (!(t.getType() == Token.LP && t.getData().equals("{")))
                            throw new CompileException("Missing statement body.", t, getFilename());
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
                                        throw new CompileException("Missing statement body.", t, getFilename());
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
                            eparser = getElseIf(c, this, null, false, false);
                            if (eparser == null) break;
                            elseIfParsers.add(eparser);
                        }
                    } else buffer = t;

                    return new IfParser(e.calculate(e.transitSuffix(null)), new SubParser(groups, this, c, false, false, null).getParsers(), elseIfParsers, new SubParser(else_g, this, c, false, false, null).getParsers());
                }
                case "while" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, this.filename);
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
                        throw new CompileException("Missing statement body.", t, this.filename);
                    do {
                        t = getToken();
                        if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                        if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                        if (i == 0) break;
                        groups.add(t);
                    } while (true);
                    ExpressionParsing e = new ExpressionParsing(vars, this, c);
                    return new WhileParser(e.calculate(e.transitSuffix(null)), new SubParser(groups, this, c, false, true, null).getParsers(), null);
                }
                case "for" -> {
                    ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();
                    Token t = getToken();

                    if (!(t.getType() == Token.LP && t.getData().equals("(")))
                        throw new CompileException("'(' expected.", t, this.filename);
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
                        throw new CompileException("Missing statement body.", t, this.filename);
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
                        throw new CompileException("Illegal combination of expressions.",t,filename);
                    }

                    ASTNode node_init = new SubParser(one_tokens,this,c,false,false,null).getParser().eval(this,c,null);

                    ArrayList<ASTNode> nodes = new ArrayList<>();
                    if(!two_tokens.isEmpty()) {
                        ExpressionParsing bool_expression = new ExpressionParsing(two_tokens, this, c);
                        nodes.addAll(bool_expression.calculate(bool_expression.transitSuffix(null)));
                    }
                    if(!three_tokens.isEmpty()) {
                        ExpressionParsing expression = new ExpressionParsing(three_tokens, this, c);
                        nodes.addAll(expression.calculate(expression.transitSuffix(null)));
                    }

                    return new ForParser(node_init,nodes, new SubParser(groups, this, c, false, true, null).getParsers(), null);
                }
                default -> throw new CompileException("Not a statement.", buf, getFilename());
            }
        } else if (buf.getType() == Token.NAME) {
            ArrayList<Token> tds = new ArrayList<>();
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
        } else throw new CompileException("Illegal start of expression.", buf, filename);
    }

    private ElseIfParser getElseIf(Compiler c, Parser parser, FunctionParser fparser, boolean function, boolean while_st) {
        ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();

        Token t = getToken();
        if (!(t.getType() == Token.LP && t.getData().equals("(")))
            throw new CompileException("'(' expected.", t, filename);
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
            throw new CompileException("Missing statement body.", t, getFilename());
        do {
            t = getToken();
            if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
            if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
            if (i == 0) break;
            groups.add(t);
        } while (true);
        ExpressionParsing e = new ExpressionParsing(vars, this, c);
        return new ElseIfParser(e.calculate(e.transitSuffix(fparser)), new SubParser(groups, parser, c, function, while_st, fparser).getParsers());
    }


    public static class SubParser {
        ArrayList<Token> tds;
        int index;
        Parser parser;
        Compiler compiler;
        FunctionParser fparser;
        Token buffer;
        boolean function;
        boolean while_st;

        public SubParser(ArrayList<Token> tds, Parser parser, Compiler compiler, boolean function, boolean while_st, FunctionParser fparser) {
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
                        ArrayList<Token> tds = new ArrayList<>();
                        do {
                            buf = getToken();
                            tds.add(buf);
                            if(buf == null)break;
                        } while (buf.getType() != Token.END);
                        return new ValueParser(tds);
                    }
                    case "if" -> {
                        ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>(), else_g = new ArrayList<>();
                        ArrayList<ElseIfParser> elseIfParsers = new ArrayList<>();
                        Token t = getToken();

                        if (!(t.getType() == Token.LP && t.getData().equals("(")))
                            throw new CompileException("'(' expected.", t, parser.filename);
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
                            throw new CompileException("Missing statement body.", t, parser.filename);
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
                            return new IfParser(e.calculate(e.transitSuffix(fparser)), new SubParser(groups, parser, compiler, function, while_st, fparser).getParsers(), elseIfParsers, new ArrayList<>());

                        int j = 1;
                        if (t.getType() == Token.KEY && t.getData().equals("else")) {
                            t = getToken();
                            if (!(t.getType() == Token.LP && t.getData().equals("{")))
                                throw new CompileException("Missing statement body.", t, parser.filename);
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
                                            throw new CompileException("Missing statement body.", t, parser.filename);
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
                                if (eparser == null) break;
                                elseIfParsers.add(eparser);
                            }
                        } else buffer = t;

                        return new IfParser(e.calculate(e.transitSuffix(fparser)), new SubParser(groups, parser, compiler, function, while_st, fparser).getParsers(), elseIfParsers, new SubParser(else_g, parser, compiler, function, while_st, fparser).getParsers());
                    }
                    case "while" -> {
                        ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();
                        Token t = getToken();

                        if (!(t.getType() == Token.LP && t.getData().equals("(")))
                            throw new CompileException("'(' expected.", t, parser.filename);
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
                            throw new CompileException("Missing statement body.", t, parser.filename);
                        do {
                            t = getToken();
                            if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                            if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                            if (i == 0) break;
                            groups.add(t);
                        } while (true);
                        ExpressionParsing e = new ExpressionParsing(vars, parser, compiler);
                        return new WhileParser(e.calculate(e.transitSuffix(fparser)), new SubParser(groups, parser, compiler, function, true, fparser).getParsers(), fparser);
                    }
                    case "for" -> {
                        ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();
                        Token t = getToken();

                        if (!(t.getType() == Token.LP && t.getData().equals("(")))
                            throw new CompileException("'(' expected.", t, parser.filename);
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
                            throw new CompileException("Missing statement body.", t, parser.filename);
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
                            throw new CompileException("Illegal combination of expressions.",t, parser.filename);
                        }

                        ASTNode node_init = new SubParser(one_tokens,parser,compiler,function,while_st,fparser).getParser().eval(parser,compiler,fparser);

                        ArrayList<ASTNode> nodes = new ArrayList<>();
                        if(!two_tokens.isEmpty()) {
                            ExpressionParsing bool_expression = new ExpressionParsing(two_tokens, parser, compiler);
                            nodes.addAll(bool_expression.calculate(bool_expression.transitSuffix(null)));
                        }
                        if(!three_tokens.isEmpty()) {
                            ExpressionParsing expression = new ExpressionParsing(three_tokens, parser, compiler);
                            nodes.addAll(expression.calculate(expression.transitSuffix(null)));
                        }

                        return new ForParser(node_init,nodes, new SubParser(groups, parser, compiler, function, while_st, fparser).getParsers(), fparser);
                    }
                    case "break" -> {
                        if (while_st) {
                            ArrayList<Token> tds = new ArrayList<>();
                            do {
                                buf = getToken();
                                if (buf.getType() == Token.END) break;
                                tds.add(buf);
                            } while (true);
                            return new BackParser(tds);
                        } else throw new CompileException("Back outside loop", buf, parser.filename);
                    }
                    case "return" -> {
                        if (function) {
                            ArrayList<Token> tds = new ArrayList<>();
                            do {
                                buf = getToken();
                                if (buf.getType() == Token.END) break;
                                tds.add(buf);
                            } while (true);
                            return new ReturnParser(tds, fparser);
                        } else throw new CompileException("Return outside function.", buf, parser.filename);
                    }
                    default -> throw new CompileException("Not a statement.", buf, parser.filename);
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
                return new ExpParser(tds, null);

            } else throw new CompileException("Illegal start of expression.", buf, parser.filename);
        }

        public ArrayList<BaseParser> getParsers() {
            ArrayList<BaseParser> bp = new ArrayList<>();
            BaseParser bpp;
            while ((bpp = getParser()) != null) bp.add(bpp);
            return bp;
        }

        private ElseIfParser getElseIf(Compiler c, Parser parser, FunctionParser fparser, boolean function, boolean while_st) {
            ArrayList<Token> vars = new ArrayList<>(), groups = new ArrayList<>();

            Token t = getToken();
            if (!(t.getType() == Token.LP && t.getData().equals("(")))
                throw new CompileException("'(' expected.", t, parser.filename);
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
                throw new CompileException("Missing statement body.", t, parser.filename);
            do {
                t = getToken();
                if (t.getType() == Token.LP && t.getData().equals("{")) i += 1;
                if (t.getType() == Token.LR && t.getData().equals("}")) i -= 1;
                if (i == 0) break;
                groups.add(t);
            } while (true);
            ExpressionParsing e = new ExpressionParsing(vars, parser, c);
            return new ElseIfParser(e.calculate(e.transitSuffix(fparser)), new SubParser(groups, parser, c, function, while_st, fparser).getParsers());
        }
    }
}
