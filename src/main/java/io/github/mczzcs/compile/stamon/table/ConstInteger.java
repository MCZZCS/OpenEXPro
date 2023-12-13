package io.github.mczzcs.compile.stamon.table;

import io.github.mczzcs.exe.obj.ExInt;
import io.github.mczzcs.exe.obj.ExObject;

import java.io.DataOutputStream;
import java.io.IOException;

public class ConstInteger extends Const{
    ExInt exInt;
    int id;
    public ConstInteger(int id,ExInt exInt){
        this.id = id;
        this.exInt = exInt;
    }
    @Override
    public void dump(DataOutputStream outputStream,ConstTable table) throws IOException {
        outputStream.writeInt(id);
        outputStream.writeByte(0x02);
        outputStream.writeInt(0x02);
        outputStream.writeShort(Integer.parseInt(exInt.getData()));
    }

    @Override
    public ExObject getObject() {
        return exInt;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getSize() {
        return 4;
    }
}
