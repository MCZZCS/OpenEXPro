package io.github.mczzcs.compile.stamon.table;

import io.github.mczzcs.exe.obj.ExDouble;
import io.github.mczzcs.exe.obj.ExObject;

import java.io.DataOutputStream;
import java.io.IOException;

public class ConstDouble extends Const{
    ExDouble exDouble;
    int id;
    public ConstDouble(int id,ExDouble exDouble){
        this.exDouble = exDouble;
        this.id = id;
    }
    @Override
    public void dump(DataOutputStream outputStream,ConstTable table) throws IOException {
        outputStream.writeInt(id);
        outputStream.writeByte(0x04);
        outputStream.writeInt(0x04);
        outputStream.writeDouble(Double.parseDouble(exDouble.getData()));
    }

    @Override
    public ExObject getObject() {
        return exDouble;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getSize() {
        return 6;
    }
}
