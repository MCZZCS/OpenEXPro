package io.github.mczzcs.compile.stamon.table;

import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.exe.obj.ExString;

import java.io.DataOutputStream;
import java.io.IOException;

public class ConstString extends Const{
    ExString string;
    int id;
    public ConstString(int id,ExString string){
        this.id = id;
        this.string = string;
    }
    @Override
    public void dump(DataOutputStream outputStream,ConstTable table) throws IOException {
        outputStream.writeInt(id);
        outputStream.writeByte(0x05);
        outputStream.writeInt(string.getData().length());
        for(char c:string.getData().toCharArray())
            outputStream.writeByte(c);
    }

    @Override
    public ExObject getObject() {
        return string;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getSize() {
        return 3 + string.getData().length();
    }
}
