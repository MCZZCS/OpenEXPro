package io.github.mczzcs.util;

import io.github.mczzcs.Main;
import io.github.mczzcs.compile.Token;

import java.io.PrintStream;

public class CompileException extends RuntimeException{
    Token token;
    String filename;
    String message;
    int status;

    public CompileException(String message,String filename){
        this.message = message;
        this.filename = filename;
        this.status = 2;
    }
    public CompileException(String message, Token token, String filename){
        this.token = token;
        this.filename = filename;
        this.status = 1;
        this.message = message;
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

        s.println("Cause by Thread/"+Thread.currentThread().getName()+" CompileException");
        StackTraceElement[] trace = getStackTrace();
        for (StackTraceElement traceElement : trace)
            s.println("\tat " + traceElement);

    }
}
