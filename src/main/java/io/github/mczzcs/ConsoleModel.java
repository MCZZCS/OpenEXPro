package io.github.mczzcs;

import io.github.mczzcs.util.CompileException;
import io.github.mczzcs.util.TextFormer;
import io.github.mczzcs.util.ScriptOutputStream;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsoleModel {
    static ArrayList<String> filename = new ArrayList<>();
    static ScriptOutputStream output = new ScriptOutputStream();
    public static boolean isStamonVM = false, isConsur = false, debug = false;
    public static String language = "en-US";
    public static void command(String[] args) throws Exception{

        try {

            OptionParser parser = new OptionParser() {
                {
                    acceptsAll(asList("f","filename")).withRequiredArg()
                            .ofType(String.class)
                            .withValuesSeparatedBy(',')
                            .describedAs("Compile the file.");
                    acceptsAll(asList("l","language")).withRequiredArg()
                            .ofType(String.class)
                            .describedAs("Set console language");
                    acceptsAll(asList("h","?","help"),"Print help info about openex commands.");
                    acceptsAll(asList("c","compile"),"Enable StamonVM compilation mode.");
                    acceptsAll(asList("v","version"),"Print version info.");
                    acceptsAll(asList("u","concur"),"Enable concurrent compilation mode.");
                    acceptsAll(asList("d","debug"),"Enable debug mode.");
                }
            };

            OptionSet set = parser.parse(args);


            if(set.has("language")){
                TextFormer.read(new File((String) set.valueOf("language")));
            }


            if (args.length == 0) {
                getOutput().info(getHelpInfo());
                return;
            }
            if (set.has("help")) {
                getOutput().info(getHelpInfo());
                return;
            }
            if(set.has("version")) {
                getOutput().info(Main.name+" "+Main.version);
                return;
            }

            if(set.has("compile")) isStamonVM = true;
            if(set.has("consur")) isConsur = true;
            if(set.has("debug")) debug = true;

            if (!set.has("filename")) throw new CompileException("No has subcommand '-filename'", "<console>");

            filename.addAll((List<String>) set.valuesOf("filename"));

            CompileManager.compile(filename);
        }catch (OptionException e){
            throw new CompileException("parser command was throw exception:"+e.getLocalizedMessage(),"<console>");
        }
    }
    private static List<String> asList(String... params) {
        return Arrays.asList(params);
    }

    public static ScriptOutputStream getOutput() {
        return output;
    }

    public static String getHelpInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(Main.name).append(' ').append(Main.version).append('\n');
        sb.append("Usage: openex [option]... [arg]...\n");
        sb.append(TextFormer.buildSubCommand("-filename","<filename>","command.file"));
        sb.append(TextFormer.buildSubCommand("-compile","","command.compile"));
        sb.append(TextFormer.buildSubCommand("-concur","","command.concur"));
        sb.append(TextFormer.buildSubCommand("-help","","command.help"));
        sb.append(TextFormer.buildSubCommand("-version","","command.version"));
        sb.append(TextFormer.buildSubCommand("-debug","","command.debug"));
        sb.append(TextFormer.buildSubCommand("-language","<lang_file>","command.lang"));
        return sb.toString();
    }
}
