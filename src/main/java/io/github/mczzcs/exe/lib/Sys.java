package io.github.mczzcs.exe.lib;

import io.github.mczzcs.ConsoleModel;
import io.github.mczzcs.Main;
import io.github.mczzcs.exe.core.Executor;
import io.github.mczzcs.exe.core.RuntimeShutdownHook;
import io.github.mczzcs.exe.obj.*;
import io.github.mczzcs.exe.thread.ThreadManager;
import io.github.mczzcs.exe.thread.ThreadTask;
import io.github.mczzcs.util.VMRuntimeException;

import java.util.ArrayList;
import java.util.Scanner;

public class Sys implements RuntimeLibrary{
    ArrayList<RuntimeFunction> rfs;
    public Sys(){
        rfs = new ArrayList<>();
        rfs.add(new Print());
        rfs.add(new Memory());
        rfs.add(new Input());
        rfs.add(new Thread());
        rfs.add(new SysInfo());
        rfs.add(new Stop());
        rfs.add(new Sleep());
        rfs.add(new NanoTime());
    }
    @Override
    public ArrayList<RuntimeFunction> functions() {
        return rfs;
    }
    @Override
    public String getName() {
        return "system";
    }

    private static class NanoTime implements RuntimeFunction{

        @Override
        public int getVarNum() {
            return 0;
        }

        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException {
            return new ExInt((int) System.nanoTime());
        }

        @Override
        public String getName() {
            return "nano_time";
        }
    }
    private static class Print implements RuntimeFunction{
        @Override
        public int getVarNum() {
            return 1;
        }
        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) {
            ExObject obj = vars.get(0);
            if(obj.getType()==ExObject.ARRAY){
                System.out.println(obj);
            }else System.out.println(obj.getData());
            return new ExNull();
        }
        @Override
        public java.lang.String getName() {
            return "print";
        }
    }

    private static class Sleep implements RuntimeFunction{

        @Override
        public int getVarNum() {
            return 1;
        }

        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException {
            ExObject o = vars.get(0);
            if(o.getType()!=ExObject.INTEGER)throw new VMRuntimeException("不兼容INT的类型,无法将未知类型输入进sleep函数的参数",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_ACCESS_EXCEPTION);
            try {
                java.lang.Thread.sleep(Integer.parseInt(o.getData()));
            } catch (InterruptedException e) {
                throw new VMRuntimeException("[VMERROR-内部错误]:"+e.getLocalizedMessage(),executor.getThread());
            }
            return new ExNull();
        }

        @Override
        public java.lang.String getName() {
            return "sleep";
        }
    }

    private static class Stop implements RuntimeFunction{

        @Override
        public int getVarNum() {
            return 1;
        }

        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException {
            ExObject o = vars.get(0);
            if(o.getType()!=ExObject.INTEGER)throw new VMRuntimeException("不兼容INT的类型,无法将未知类型输入进stop函数的参数",executor.getThread(), VMRuntimeException.EnumVMException.ILLEGAL_ACCESS_EXCEPTION);
            RuntimeShutdownHook.exit_code = Integer.parseInt(o.getData());
            System.exit(RuntimeShutdownHook.exit_code);
            return new ExNull();
        }

        @Override
        public java.lang.String getName() {
            return "stop";
        }
    }

    private static class Memory implements RuntimeFunction{
        @Override
        public int getVarNum() {
            return 0;
        }

        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) {
            Runtime runtime = Runtime.getRuntime();
            long mb = runtime.totalMemory() - runtime.freeMemory();
            return new ExDouble(mb(mb));
        }

        private static double mb (long s) {
            return Double.parseDouble(String.format("%.2f",(double)s / (1024 * 1024)));
        }


        @Override
        public String getName() {
            return "memory";
        }
    }

    private static class Input implements RuntimeFunction{
        @Override
        public int getVarNum() {
            return 0;
        }

        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) {
            executor.getThread().setStatus(ThreadManager.Status.WAIT);
            Scanner s = new Scanner(System.in);
            String data = s.nextLine();
            executor.getThread().setStatus(ThreadManager.Status.RUNNING);
            return new ExString(data);
        }

        @Override
        public java.lang.String getName() {
            return "input";
        }
    }

    private static class Thread implements RuntimeFunction{

        @Override
        public int getVarNum() {
            return 2;
        }

        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException {
            ExObject name = vars.get(0),func = vars.get(1);
            ThreadTask task = new ThreadTask(name.getData());
            Function function = null;
            for(Function f:ThreadManager.getFunctions()) {
                if(f.getLib().equals(executor.getExecuting().getInvoke_name())){
                    if(f.getName().equals(func.getData())) function = f;
                }
            }
            if(function == null)throw new VMRuntimeException("Not found function:"+func.getData(),executor.getThread());
            task.setFunction(function);
            task.start();
            ThreadManager.addThread(task);
            return new ExString(task.getName());
        }

        @Override
        public java.lang.String getName() {
            return "thread";
        }
    }

    private static class SysInfo implements RuntimeFunction{

        @Override
        public int getVarNum() {
            return 1;
        }

        @Override
        public ExObject invoke(ArrayList<ExObject> vars, Executor executor) throws VMRuntimeException {
            ExObject object = vars.get(0);

            if(object.getType() == ExObject.ARRAY) throw new VMRuntimeException("Illegal argument: Cannot pass in a parameter of type ARRAY.",executor.getThread());

            return switch (object.getData()) {
                case "os" -> new ExString(System.getProperty("os.name"));
                case "version" -> new ExString(Main.version);
                case "svm" -> new ExBool(ConsoleModel.isStamonVM);
                case "edition" -> new ExString(Main.name);
                default -> new ExNull();
            };
        }

        @Override
        public java.lang.String getName() {
            return "sysinfo";
        }
    }
}
