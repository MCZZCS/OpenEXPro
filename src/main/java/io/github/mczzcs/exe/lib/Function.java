package io.github.mczzcs.exe.lib;

import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.obj.ExValue;

import java.util.ArrayList;

public class Function extends ExValue {
    String lib;
    String name;
    ArrayList<ASTNode> bcs;

    public Function(String lib, String name, ArrayList<ASTNode> bcs){
        this.lib = lib;
        this.name = name;
        this.bcs = bcs;
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

}
