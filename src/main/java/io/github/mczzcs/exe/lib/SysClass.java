package io.github.mczzcs.exe.lib;

import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.lib.util.CallMethod;
import io.github.mczzcs.exe.obj.ExClass;
import io.github.mczzcs.exe.obj.ExNull;
import io.github.mczzcs.exe.obj.ExObject;

import java.util.ArrayList;
import java.util.List;

public class SysClass extends RuntimeClass{
    List<RuntimeMethod> methods;

    public SysClass() {
        super("system");
        this.methods = new ArrayList<>();
        this.methods.add(new Print());
    }

    @Override
    public List<RuntimeMethod> getMethods() {
        return methods;
    }

    private class Print extends RuntimeMethod{

        public Print() {
            super("print",SysClass.this);
        }

        @Override
        public ExObject implMethod(Executor executor, List<ExObject> vars) {
            ExObject obj = vars.get(0);
            if(obj.getType()==ExObject.ARRAY){
                System.out.println(obj);
            }else {

                if(obj.getType() == CLASS){
                    ExClass exClass = (ExClass) obj;
                    ExObject ret = CallMethod.call(exClass,"toString",executor);
                    System.out.println(ret.getData());
                    return new ExNull();
                }
                System.out.println(obj.getData());
            }
            return new ExNull();
        }

        @Override
        public int getVarNum() {
            return 1;
        }
    }
}
