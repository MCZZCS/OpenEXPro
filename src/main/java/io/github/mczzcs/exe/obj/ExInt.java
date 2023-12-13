package io.github.mczzcs.exe.obj;

public class ExInt extends ExObject{
    int data;
    public ExInt(int data){
        this.data = data;
    }

    @Override
    public String getData() {
        return data+"";
    }

    @Override
    public int getType() {
        return INTEGER;
    }

    @Override
    public String toString() {
        return "[INT]:"+data;
    }
}
