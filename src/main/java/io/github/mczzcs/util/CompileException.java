package io.github.mczzcs.util;

import io.github.mczzcs.ConsoleModel;
import io.github.mczzcs.Main;
import io.github.mczzcs.compile.tokens.Token;
import io.github.mczzcs.compile.parser.Parser;

import java.io.PrintStream;

public class CompileException extends RuntimeException{
    Token token;
    Parser parser;
    String filename;
    String message;
    int status;

    StackTraceElement[] trace;

    public CompileException(String message,String filename){
        this.message = TextFormer.format(message);
        this.filename = filename;
        this.status = 2;
        this.parser = null;
        if(ConsoleModel.debug) this.trace = Thread.currentThread().getStackTrace();
    }
    public CompileException(String message, Token token, String filename,Parser parser){
        this.token = token;
        this.filename = filename;
        this.status = 1;
        this.message = TextFormer.format(message);
        this.parser = parser;
        if(ConsoleModel.debug) this.trace = Thread.currentThread().getStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        if (status == 1) {
            s.println("Compile Error: " + message +
                    "\n\tToken: " + token.getData() +
                    "\n\tEdition: " + Main.name+
                    "\n\tVersion: " + Main.version);
        } else if (status == 2) {
            s.println("Compile Error: " + message +
                    "\n\tEdition: " + Main.name +
                    "\n\tVersion: " + Main.version);
        }

        int line = token == null ? 0 : token.getLine();
        s.println("Location: ("+filename+": "+line+")");
        if(parser == null){
            s.println("\t<no_such_statement>");
            if(ConsoleModel.debug) {
                for (StackTraceElement traceElement : trace)
                    s.println("\tat " + traceElement);
            }
            return;
        }

        int index = 0,buf = 0;

        StringBuilder sb = new StringBuilder();
        sb.append('\t').append(line).append("| ");

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
            sbb.append(" ".repeat(Math.max(0,buf)));
        }

        sbb.append('^');
        s.println(sbb);

        if(ConsoleModel.debug) {
            for (StackTraceElement traceElement : trace)
                s.println("\tat " + traceElement);
        }
    }
}
