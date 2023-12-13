package io.github.mczzcs.util;

import io.github.mczzcs.exe.thread.RuntimeInitThread;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TextFormer {
    static Map<String, String> lang = new HashMap<>();
    static {
        try {
            String data = """
                    need.lr.b='}' expected.
                    need.lp.b='{' expected.
                    need.lp.s='(' expected.
                    need.lr.s=')' expected.
                    need.sem.marks='"' expected.
                    need.sem.call='.'expected.
                    miss.function.body=Missing function body.
                    miss.statement.body=Missing statement body.
                    type.not.valid=Type name is not valid.
                    unable.resolve.symbols=Unable to resolve symbols.
                    not.statement=Not a statement.
                    illegal.expression.start=Illegal start of expression.
                    illegal.expression.comb=Illegal combination of expressions.
                    illegal.string.escape=Illegal escape character in string literal.
                    illegal.key=Illegal keywords.
                    outside.function.return=Return outside function.
                    outside.loop.back=Back outside loop
                                    
                    no.such.function=No such function
                    op.type=The operation type is incorrect.
                                    
                    command.help=Print help info about openex commands.
                    command.lang=Select the output language.
                    command.file=Run one or more scripts.(Please separate multiple file names with space)
                    command.version=Print openex version
                    command.compile=Enable StamonVM compilation mode.
                    command.debug=Enable debug mode.
                    command.concur=Enable concurrent compilation mode.
                    """;
            Properties properties = new Properties();
            properties.load(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                lang.put((String) entry.getKey(), (String) entry.getValue());
            }
        }catch (IOException e){
        }
    }

    public static String buildSubCommand(String command, String args, String message) {
        StringBuilder sb = new StringBuilder();
        int index = 4 + command.length() + args.length();
        sb.append("   ").append(command).append(' ').append(args);
        sb.append(" ".repeat(Math.max(0, 29 - index)));
        sb.append(format(message));
        sb.append('\n');
        return sb.toString();
    }

    public static InputStream getResource(String path) {
        String pathx;
        if (path.startsWith("/")) pathx = path;
        else pathx = "/" + path;
        return TextFormer.class.getResourceAsStream(pathx);
    }

    public static void read(File file) {
        try {
            Properties lang_config = new Properties();
            lang_config.load(new FileInputStream(file));
            for (Map.Entry<Object, Object> entry : lang_config.entrySet()) {
                lang.put((String) entry.getKey(), (String) entry.getValue());
            }
        }catch (IOException e){
            throw new VMRuntimeException("Cannot load lang file '"+file.getName()+"'.",new RuntimeInitThread(), VMRuntimeException.EnumVMException.VM_ERROR);
        }
    }

    public static String format(String format) {
        String r = lang.get(format);
        if (r == null) return format;
        return r;
    }
}
