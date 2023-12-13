package io.github.mczzcs.compile.stamon.ir;

import io.github.mczzcs.compile.code.opcode.PushNode;
import io.github.mczzcs.compile.stamon.StamonByteCodeFile;

import java.io.DataOutputStream;
import java.io.IOException;

public class PushCode implements ByteCode{
    int opcode;

    public PushCode(PushNode node, StamonByteCodeFile byteCodeFile){
        this.opcode = byteCodeFile.getConstTable().poolObject(node.getObj());
    }

    @Override
    public void dump(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(PUSH_CODE);
        outputStream.writeInt(opcode);
    }
}
