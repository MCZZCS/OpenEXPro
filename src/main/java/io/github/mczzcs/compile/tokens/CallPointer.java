package io.github.mczzcs.compile.tokens;

public class CallPointer extends Token{
    Token name;

    public CallPointer(Token name){
        this.name = name;
    }

    public Token getName() {
        return name;
    }

    @Override
    public int getType() {
        return POINTER;
    }

    @Override
    public String toString() {
        return "Pointer: "+name.getData();
    }
}
