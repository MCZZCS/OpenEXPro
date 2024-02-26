package io.github.mczzcs.exe.lib;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.exe.obj.ExValue;

import java.util.ArrayList;
import java.util.List;

public class Function extends ExValue {
    String lib;
    String name;
    String filename;
    ArrayList<ASTNode> bcs;

    public Function(String lib, String name, ArrayList<ASTNode> bcs,String filename){
        this.lib = lib;
        this.name = name;
        this.bcs = bcs;
        this.filename = filename;
    }

    public Function(String className, String methodName, List emptyList) {
    }

    public ArrayList<ASTNode> getBcs() {
        return bcs;
    }

    public String getName() {
        return name;
    }

    public String getLib() {
        return lib;
    }

    public String getFilename() {
        return filename;
    }
}
