package io.github.mczzcs.compile;

import io.github.mczzcs.CompileManager;
import io.github.mczzcs.ConsoleModel;
import io.github.mczzcs.exe.code.ASTNode;
import io.github.mczzcs.exe.core.Script;
import io.github.mczzcs.exe.thread.ThreadTask;
import io.github.mczzcs.compile.parser.BaseParser;
import io.github.mczzcs.compile.parser.Parser;
import io.github.mczzcs.util.CompileException;

import java.util.ArrayList;

public class Compiler {
    String filename;
    ArrayList<ASTNode> bcs;
    ArrayList<String> libname;
    public ArrayList<String> value_names;
    public ArrayList<String> array_names;

    public ArrayList<String> getLibnames() {
        return libname;
    }

    public Compiler(String filename) {
        this.filename = filename;
        this.libname = new ArrayList<>();
        this.value_names = new ArrayList<>();
        this.array_names = new ArrayList<>();
        libname.add(filename.split("\\.")[0]);
        this.bcs = new ArrayList<>();
    }

    public ArrayList<String> getValueNames() {
        return value_names;
    }

    public void compile(ThreadTask task) {
        try {
            ConsoleModel.getOutput().debug("Lexing file '"+filename+"'");
            LexicalAnalysis al = new LexicalAnalysis(CompileManager.getFileData(filename), filename);
            ArrayList<Token> t = new ArrayList<>();
            for (Token b : al.getTokens()) {
                if (b.type == Token.LINE||b.type == Token.TEXT) continue;
                t.add(b);
            }

            ConsoleModel.getOutput().debug("Parsing file '"+filename+"'");
            Parser parser = new Parser(t, al.file_name);

            try {
                while (true) {
                    BaseParser bp = parser.getParser(this);
                    if (bp == null) break;
                    bcs.add(bp.eval(parser, this, null));
                }
            }catch (NullPointerException e){
                throw new CompileException("';' expected.",filename);
            }


            task.addScripts(new Script(filename.split("\\.")[0], filename, bcs));
        }catch (CompileException e){
            e.printStackTrace();
            System.exit(0);
        }
    }
}
