package io.github.mczzcs.compile.stamon.ir;

import java.io.DataOutputStream;
import java.io.IOException;

public interface ByteCode {
    byte MOV_CODE = 0x03
            ,MEMBER_CODE = 0X04
            ,INDEX_CODE = 0X05
            ,ADD_CODE = 0X06
            ,SUB_CODE = 0X07
            ,MUL_CODE = 0X08
            ,DIV_CODE = 0X09
            ,MOD_CODE = 0X0A
            ,SHL_CODE = 0X0B
            ,SHR_CODE = 0X0C
            ,LESS_CODE= 0X0D
            ,LESS_EQU_CODE = 0X0E
            ,BIG_CODE = 0X0F
            ,BIG_EQU_CODE = 0X10
            ,EQU_CODE =0X11
            ,NOT_EQU = 0X12
            ,BIT_AND_CODE = 0X13
            ,BIT_XOR_CODE = 0X14
            ,BIT_OR_CODE = 0X15
            ,AND_CODE = 0X16
            ,OR_CODE = 0X17
            ,INC_CODE = 0X18
            ,DEC_CODE = 0X19
            ,NOT_CODE = 0X1A
            ,BIT_NOT_CODE = 0X1B
            ,NEG_CODE = 0X1C
            ,POS_CODE = 0X1D
            ,NEW_CODE = 0X1E
            ,PUSH_CODE = 0X1F
            ,POP_CODE = 0X20
            ,IF_CODE = 0X21
            ,JMP_CODE = 0X22
            ,PUSH_SCOPE_CODE = 0X23
            ,POP_SCOPE_CODE = 0X24
            ,CALL_CODE = 0X25
            ,RET_CODE = 0X26
            ,SFN_CODE = 0X27;


    void dump(DataOutputStream outputStream) throws IOException;

}
