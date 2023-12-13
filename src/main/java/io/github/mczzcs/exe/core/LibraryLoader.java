package io.github.mczzcs.exe.core;

import io.github.mczzcs.exe.lib.*;

import java.util.ArrayList;

public class LibraryLoader {

    static ArrayList<RuntimeLibrary> rls = new ArrayList<>();
    static {
        rls.add(new Sys());
        rls.add(new Array());
        rls.add(new Type());
        rls.add(new File());
        rls.add(new StringLib());
        rls.add(new List());
    }

    private static String slashify(String path, boolean isDirectory) {
        String p = path;
        if (java.io.File.separatorChar != '/')
            p = p.replace(java.io.File.separatorChar, '/');
        if (!p.startsWith("/"))
            p = "/" + p;
        if (!p.endsWith("/") && isDirectory)
            p = p + "/";
        return p;
    }

    public static ArrayList<RuntimeLibrary> getLibs(){
        return rls;
    }
}
