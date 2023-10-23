package io.github.mczzcs.exe.thread;

import io.github.mczzcs.exe.core.StackFrame;

import java.util.Stack;

public class RuntimeInitThread extends ThreadTask{

    public RuntimeInitThread() {
        super("<init>");
    }

    @Override
    public Stack<StackFrame> getCallStack() {
        return new Stack<>();
    }
}
