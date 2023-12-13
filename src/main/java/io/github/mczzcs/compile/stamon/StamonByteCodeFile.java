package io.github.mczzcs.compile.stamon;

import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.compile.stamon.ir.ByteCode;
import io.github.mczzcs.compile.stamon.table.ConstTable;
import io.github.mczzcs.util.CompileException;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class StamonByteCodeFile {
    Compiler compiler;
    ConstTable constTable;

    List<ByteCode> codes;
    public StamonByteCodeFile(Compiler compiler){
        this.compiler = compiler;
        this.codes = new LinkedList<>();
        this.constTable = new ConstTable(this);
    }

    public void createFile(){
        String name = compiler.getFilename().split("\\.")[0];

        StamonCompiler.compile(codes,compiler.getBcs(),this);

        try(BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(name+".stvc"))){
            dump(new DataOutputStream(outputStream));
        }catch (IOException io){
            throw new CompileException("Cannot create stamonvm byte code file.",compiler.getFilename());
        }
    }

    public ConstTable getConstTable() {
        return constTable;
    }

    public List<ByteCode> getCodes() {
        return codes;
    }

    public void dump(DataOutputStream stream) throws IOException {
        stream.writeShort(0xABDB);
        stream.writeByte(StamonCompiler.version[0]);
        stream.writeByte(StamonCompiler.version[1]<<4 + StamonCompiler.version[2]&0x0f);
        constTable.dump(stream);

        StamonCompiler.dump(stream,codes);
    }
}
