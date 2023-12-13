package io.github.mczzcs.compile.stamon.table;

import io.github.mczzcs.exe.obj.ExNull;
import io.github.mczzcs.exe.obj.ExObject;

import java.io.DataOutputStream;
import java.io.IOException;

public class ConstNull extends Const{
    int id;
    public ConstNull(int id){
        this.id = id;
    }
    @Override
    public void dump(DataOutputStream outputStream,ConstTable table) throws IOException {
        outputStream.writeInt(id);
        outputStream.writeByte(0x01);
        outputStream.writeInt(0x00);
        outputStream.writeByte(0x00);
    }

    @Override
    public ExObject getObject() {
        return new ExNull();
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getSize() {
        return 3;
    }
}
