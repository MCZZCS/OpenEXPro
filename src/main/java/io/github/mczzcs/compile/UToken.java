package io.github.mczzcs.compile;

public class UToken extends Token{
    Token token;
    public UToken(Token token) {
       this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String getData() {
        return token.data;
    }

    @Override
    public int getType() {
        return SEM;
    }

    @Override
    public String toString() {
        return "[U:"+ token.toString()+"]";
    }
}
