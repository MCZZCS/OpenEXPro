package io.github.mczzcs;

import io.github.mczzcs.exe.thread.ThreadManager;
import io.github.mczzcs.exe.thread.ThreadTask;
import io.github.mczzcs.compile.Compiler;
import io.github.mczzcs.util.CompileException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CompileManager {
    static ArrayList<Compiler> compilers = new ArrayList<>();

    public static void compile(ArrayList<String> files){
        ThreadTask task = new ThreadTask("main");
        for(String s:files)compilers.add(new Compiler(s));
        for(Compiler c:compilers)c.compile(task);
        ThreadManager.addThread(task);
        ThreadManager.launch();
    }

    public static ArrayList<String> getFileData(String name){
        try(BufferedReader reader = new BufferedReader(new FileReader(name))){
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine())!=null)lines.add(line);
            return lines;
        }catch (IOException io){
            throw new CompileException("IOException:"+io.getLocalizedMessage(),name);
        }
    }
}
