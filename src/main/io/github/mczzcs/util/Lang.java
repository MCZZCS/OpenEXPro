package io.github.mczzcs.util;

import io.github.mczzcs.exe.thread.RuntimeInitThread;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Lang {
    static Map<String, String> lang = new HashMap<>();

    public static String language = "zh-CN";

    public static String buildSubCommand(String command, String args, String message) {
        StringBuilder sb = new StringBuilder();
        int index = 4 + command.length() + args.length();
        sb.append("   ").append(command).append(' ').append(args);
        sb.append(" ".repeat(Math.max(0, 29 - index)));
        sb.append(message);
        sb.append('\n');
        return sb.toString();
    }

    public static InputStream getResource(String path) {
        String pathx;
        if (path.startsWith("/")) pathx = path;
        else pathx = "/" + path;
        return Lang.class.getResourceAsStream(pathx);
    }

    public static void read(String language) {
        try {
            if (!language.trim().isEmpty()) Lang.language = language + ".lang";

            Properties lang_config = new Properties();
            lang_config.load(getResource(language));
            for (Map.Entry<Object, Object> entry : lang_config.entrySet()) {
                lang.put((String) entry.getKey(), (String) entry.getValue());
            }
        }catch (IOException e){
            throw new VMRuntimeException("Cannot load lang file '"+language+"'.",new RuntimeInitThread(), VMRuntimeException.EnumVMException.VM_ERROR);
        }
    }

    public static String format(String format) {
        String r = lang.get(format);
        if (r == null) return format;
        return r;
    }
}
