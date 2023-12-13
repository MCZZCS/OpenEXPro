package io.github.mczzcs.exe.core;

import io.github.mczzcs.exe.lib.Function;
import io.github.mczzcs.exe.obj.ExObject;
import io.github.mczzcs.exe.obj.ExValue;

import java.util.*;

public class StackFrame {
    Function function;
    ArrayList<ExValue> pre_value_table;
    Deque<ExObject> op_stack;

    public StackFrame(Function function){
        this.function = function;
        this.pre_value_table = new ArrayList<>();
        this.op_stack = new LinkedList<>();
    }

    public void push(ExObject object){
        this.op_stack.push(object);
    }

    public ExObject pop(){
        return this.op_stack.pop();
    }

    public Deque<ExObject> getOpStack() {
        return op_stack;
    }

    public List<ExValue> getValues(){
        return pre_value_table;
    }

    public Function getFunction() {
        return function;
    }
}
