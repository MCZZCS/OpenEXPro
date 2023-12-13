import io.github.mczzcs.Main;

import java.util.Scanner;

public class Debug {
    public static void main(String[] args) {
        //args = new String[]{"-d","-f","main.exf,script.exf"};
        //args = new String[]{"-filename","debug.exf"};
        //args = new String[]{"-language","en-US","-filename","debug.exf"};
        //args = new String[]{"-debug","-java", "-f", "debug.exf"};
        args = new String[]{"-debug","-c", "-f", "debug.exf"};
        //args = new String[]{"-h"};//,"-l","zh-CN.lang"};
        Main.main(args);
    }
}


