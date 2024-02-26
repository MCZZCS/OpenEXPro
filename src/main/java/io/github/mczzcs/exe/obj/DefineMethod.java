package io.github.mczzcs.exe.obj;

import io.github.mczzcs.compile.code.ASTNode;
import io.github.mczzcs.exe.core.Executor;

public class DefineMethod extends ExMethod{
    ASTNode codes;
    ExClass exClass;
    public DefineMethod(String method_name, ASTNode codes,ExClass exClass) {
        super(method_name);
        this.codes = codes;
        this.exClass = exClass;
    }

    public DefineMethod(String method_name){
        super(method_name);
    }

    public DefineMethod(String fileClass,String method_name, ASTNode codes,String classes) {
        super(method_name);
        this.exClass = new ExClass(classes);
        this.codes = codes;
    }

    public void callMethod(Executor executor){
        codes.executor(executor);
    }

    public String getClassName() {
        return exClass.getClassName();
    }
}
