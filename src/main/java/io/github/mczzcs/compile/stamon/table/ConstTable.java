package io.github.mczzcs.compile.stamon.table;

import io.github.mczzcs.compile.stamon.StamonByteCodeFile;
import io.github.mczzcs.exe.obj.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ConstTable {
    /*
    null 0x01
    integer 0x02
    double 0x04
    string 0x05
     */
    StamonByteCodeFile byteCodeFile;
    List<Const> consts;
    int size;
    int index;


    public ConstTable(StamonByteCodeFile byteCodeFile){
        this.byteCodeFile = byteCodeFile;
        this.consts = new LinkedList<>();
        this.size = 0;
    }

    public int checkConst(ExObject exInt,int type) {
        for (Const c : consts) {
            if (c.getObject().getType() == type) {
                if (c.getObject().getData().equals(exInt.getData())) {
                    return c.getID();
                }
            }
        }
        return -1;
    }

    public int poolObject(ExObject object){
        int type = object.getType();
        return switch (type){
            case ExObject.INTEGER -> poolInt((ExInt) object);
            case ExObject.BOOLEAN -> poolBoolean((ExBool) object);
            case ExObject.DOUBLE -> poolDouble((ExDouble) object);
            case ExObject.STRING -> poolString((ExString) object);
            case ExObject.NULL -> poolNull((ExNull) object);
            default -> -1;
        };
    }

    public int poolBoolean(ExBool bool){
        int ret;
        if((ret = checkConst(bool,ExObject.INTEGER))!=-1){
            return ret;
        }else {
            ret = index;
            consts.add(new ConstInteger(index, new ExInt(Boolean.parseBoolean(bool.getData()) ? 1 : 0)));
            index++;
        }
        return ret;
    }

    public int poolInt(ExInt exInt){
        int ret;
        if((ret = checkConst(exInt,ExObject.INTEGER))!=-1){
            return ret;
        }else {
            ret = index;
            consts.add(new ConstInteger(index, exInt));
            index++;
        }
        return ret;
    }

    public int poolNull(ExNull exNull){
        int ret;
        if((ret = checkConst(exNull,ExObject.NULL))!=-1){
            return ret;
        }else {
            ret = index;
            consts.add(new ConstNull(index));
            index++;
        }
        return ret;
    }

    public int poolDouble(ExDouble exDouble){
        int ret;
        if((ret = checkConst(exDouble,ExObject.DOUBLE))!=-1){
            return ret;
        }else {
            ret = index;
            consts.add(new ConstDouble(index, exDouble));
            index++;
        }
        return ret;
    }

    public int poolString(ExString string){
        int ret;
        if((ret = checkConst(string,ExObject.STRING))!=-1){
            return ret;
        }else {
            ret = index;
            consts.add(new ConstString(index, string));
            index++;
        }
        return ret;
    }

    public void dump(DataOutputStream outputStream) throws IOException {
        outputStream.writeInt(consts.size());
        for(Const c:consts) c.dump(outputStream,this);
    }
}
