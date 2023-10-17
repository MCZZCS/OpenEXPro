package io.github.mczzcs;

import java.util.HashSet;

public class Main {
    public static final String compile_version = "v0.1.0";
    public static final String runtime_version = "v0.1.0";

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
    }
    public static boolean isKey(String ss){
        return s.contains(ss);
    }

    public static void main(String[] args) {
        ConsoleModel.command(args);
    }
}
