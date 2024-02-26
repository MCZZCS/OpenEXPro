package io.github.mczzcs.compile.tokens;

import io.github.mczzcs.compile.code.struct.GroupASTNode;
import io.github.mczzcs.compile.tokens.Token;

public class TokenX extends Token {
    GroupASTNode bc;
    public TokenX(GroupASTNode bc){
        this.bc = bc;
    }

    public GroupASTNode getBc() {
        return bc;
    }

    @Override
    public int getType() {
        return EXP;
    }

    @Override
    public String toString() {
        return bc.toString();
    }
}
