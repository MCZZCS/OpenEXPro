package io.github.mczzcs.exe.core;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.exe.obj.ExValue;

import java.util.LinkedList;
import java.util.List;

public class Script {
    List<ASTNode> bcs;
    List<ExValue> values;
    String invoke_name;
    String filename;

    public Script(String invoke_name,String filename,List<ASTNode> bcs){
        this.invoke_name = invoke_name;
        this.filename= filename;
        this.bcs = bcs;
        this.values = new LinkedList<>();
    }

    public List<ExValue> getValues() {
        return values;
    }

    public List<ASTNode> getBcs() {
        return bcs;
    }

    public String getFilename() {
        return filename;
    }

    public String getInvoke_name() {
        return invoke_name;
    }
}
