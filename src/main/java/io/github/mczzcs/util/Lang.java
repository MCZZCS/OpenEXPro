package io.github.mczzcs.util;

import java.util.HashMap;

public class Lang {
    static HashMap<String,String> lang = new HashMap<>();

    /* need.lr.b '}' expected.
     * need.lp.b '{' expected.
     * need.lp.s '(' expected.
     * need.lr.s ')' expected.
     * need.sem.marks '"' expected.
     * need.sem.call '.'expected.
     * miss.function.body Missing function body.
     * miss.statement.body Missing statement body.
     * type.not.valid Type name is not valid.
     * unable.resolve.symbols Unable to resolve symbols.
     * not.statement Not a statement.
     * illegal.expression.start Illegal start of expression.
     * illegal.expression.comb Illegal combination of expressions.
     * illegal.string.escape Illegal escape character in string literal.
     * illegal.key Illegal keywords.
     * outside.function.return Return outside function.
     * outside.loop.back Back outside loop
     */

    /*
     * no.such.function No such function
     *
     */

    public static String buildSubCommand(String command,String args,String message){
        StringBuilder sb = new StringBuilder();
        int index = 4 + command.length() + args.length();
        sb.append("   ").append(command).append(' ').append(args);
        sb.append(" ".repeat(Math.max(0, 29 - index)));
        sb.append(message);
        sb.append('\n');
        return sb.toString();
    }

    public static void read(){

    }

    public static String format(String format){
        String r = lang.get(format);
        if(r == null) return format;
        return r;
    }
}
