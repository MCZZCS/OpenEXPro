package io.github.mczzcs.exe.core;

import io.github.mczzcs.ConsoleModel;

public class RuntimeShutdownHook extends Thread{

    public static int exit_code = 0;

    public RuntimeShutdownHook(){
        setName("Runtime Shutdown Hook");
    }

    @Override
    public void run() {
        ConsoleModel.getOutput().debug("Process stopped, exit code '"+exit_code+"'.");
    }
}
