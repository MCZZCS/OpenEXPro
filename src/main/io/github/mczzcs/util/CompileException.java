package io.github.mczzcs.util;

import io.github.mczzcs.Main;
import io.github.mczzcs.compile.Token;
import io.github.mczzcs.compile.parser.Parser;

import java.io.PrintStream;
import java.util.ArrayList;

public class CompileException extends RuntimeException{
    Token token;
    Parser parser;
    String filename;
    String message;
    int status;

    public CompileException(String message,String filename){
        this.message = Lang.format(message);
        this.filename = filename;
        this.status = 2;
        this.parser = null;
    }
    public CompileException(String message, Token token, String filename,Parser parser){
        this.token = token;
        this.filename = filename;
        this.status = 1;
        this.message = Lang.format(message);
        this.parser = parser;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        if (status == 1) {
            s.println("Compile Error: " + message +
                    "\n\tToken: " + token.getData() +
                    "\n\tLine: " + token.getLine() +
                    "\n\tFilename: " + filename +
                    "\n\tEdition: " + Main.name+
                    "\n\tVersion: " + Main.version);
        } else if (status == 2) {
            s.println("Compile Error: " + message +
                    "\n\tEdition: " + Main.name +
                    "\n\tVersion: " + Main.version);
        }

        s.println("Location: ");
        if(parser == null) s.println("\t<no_such_statement>");

        int index = 0,buf = 0;
        int line = token.getLine();
        StringBuilder sb = new StringBuilder();
        sb.append('\t');

        for(Token token1:parser.getTds()){
            if(token1.getLine() == line){
                sb.append(token1.getData()).append(' ');
                if(token.equals(token1)){
                    buf = index;
                }
                index += token1.getData().length() + 1;
            }
        }
        s.println(sb);


        StringBuilder sbb = new StringBuilder();
        sbb.append('\t');
        sbb.append(" ".repeat(Math.max(0,buf)));

        if(!((buf = token.getData().length()) < 3)){
            sbb.append(" ".repeat(Math.max(0,buf/2)));
        }

        sbb.append('^');
        s.println(sbb);

    }
}
