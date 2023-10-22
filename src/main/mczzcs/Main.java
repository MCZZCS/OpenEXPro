package io.github.mczzcs;

import io.github.mczzcs.exe.core.RuntimeShutdownHook;

import java.util.HashSet;

public class Main {
    public static final String version = "v0.1.3";
    public static final String name = "OpenEX Pro";

    static HashSet<String> s = new HashSet<>();
    static {
        s.add("function");
        s.add("value");
        s.add("local");
        s.add("global");
        s.add("if");
        s.add("else");
        s.add("while");
        s.add("return");
        s.add("false");
        s.add("true");
        s.add("include");
        s.add("this");
        s.add("null");
        s.add("elif");
        s.add("break");
        s.add("for");
    }
    public static boolean isKey(String ss){
        return s.contains(ss);
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new RuntimeShutdownHook());
        ConsoleModel.command(args);
    }
}
