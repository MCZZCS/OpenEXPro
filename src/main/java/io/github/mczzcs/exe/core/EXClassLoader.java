package io.github.mczzcs.exe.core;

import io.github.mczzcs.exe.lib.RuntimeClass;
import io.github.mczzcs.exe.lib.SysClass;

import java.util.ArrayList;
import java.util.List;

public class EXClassLoader {
    static List<RuntimeClass> classes = new ArrayList<>();

    static {
        classes.add(new SysClass());
    }

    public static List<RuntimeClass> getLibrary() {
        return classes;
    }
}
