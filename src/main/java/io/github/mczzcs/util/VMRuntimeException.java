package io.github.mczzcs.util;

import io.github.mczzcs.ConsoleModel;
import io.github.mczzcs.exe.thread.ThreadTask;
import io.github.mczzcs.Main;

public class VMRuntimeException extends Exception{
    public VMRuntimeException(String message, ThreadTask task){
        ConsoleModel.getOutput().error("ScriptRuntimeError:"+message+"\n\t" +
                "ThreadName:"+task.getName()+"\n\t" +
                "FileName:"+task.getFilename()+"\n\t" +
                "Edition: " +Main.name + "\n\t" +
                "Version: "+ Main.version);
    }
}
