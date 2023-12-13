package io.github.mczzcs.compile.stamon.ir;

import io.github.mczzcs.compile.code.opcode.*;

import java.io.DataOutputStream;
import java.io.IOException;

public class OpCode implements ByteCode{
    OpNode node;

    public OpCode(OpNode node){
        this.node = node;
    }

    @Override
    public void dump(DataOutputStream outputStream) throws IOException {
        if(node instanceof AddNode) outputStream.writeByte(ADD_CODE);
        else if(node instanceof SubNode) outputStream.writeByte(SUB_CODE);
        else if(node instanceof MulNode) outputStream.writeByte(MUL_CODE);
        else if(node instanceof DivNode) outputStream.writeByte(DIV_CODE);
        else if(node instanceof AndNode) outputStream.writeByte(AND_CODE);
        else if(node instanceof OrNode) outputStream.writeByte(OR_CODE);
        else if(node instanceof NotNode) outputStream.writeByte(NOT_CODE);
        else if(node instanceof BigNode) outputStream.writeByte(BIG_CODE);
        else if(node instanceof LessNode) outputStream.writeByte(LESS_CODE);
        else if(node instanceof BigEquNode) outputStream.writeByte(BIG_EQU_CODE);
        else if(node instanceof LessEquNode) outputStream.writeByte(LESS_EQU_CODE);
        else if(node instanceof EquNode) outputStream.writeByte(EQU_CODE);
        outputStream.writeInt(0x00);
    }
}
