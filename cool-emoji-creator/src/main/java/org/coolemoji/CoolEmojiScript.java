package org.coolemoji;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class CoolEmojiScript {

    private static final File root = new File("/home/john/dev/cool-emoji-for-pidgin/");
    private static final File source = new File(root, "source");
    private static final File output = new File(root, "cool-emojis/emojis");
    private static final File theme = new File(root, "cool-emojis/theme");
    private static final Map<String, String> mappings = new HashMap<String, String>();

    public static void main(String[] args) throws InterruptedException, IOException, IM4JavaException {

        BufferedReader reader = new BufferedReader(new FileReader(new File(root, "mappings.txt")));

        while(reader.ready()){
            String line = reader.readLine();

            int spaceLocation = line.indexOf(" ");

            mappings.put(line.substring(0, spaceLocation), line.substring(spaceLocation+1));
        }


        FileOutputStream os = new FileOutputStream(theme);
        PrintStream print = new PrintStream(os);

        print.println("Name=Cool Emojis for Pidgin\n" +
                "Description=Cool Emojis for Pidgin\n" +
                "Icon=emojis/test.png\n" +
                "Author=John Ericksen\n\n\n" +
                "[default]");

        scanSource(source, print);


    }

    public static void scanSource(File source, PrintStream print) throws InterruptedException, IOException, IM4JavaException {
        for (File file : source.listFiles()) {

            if(file.isDirectory()){
                scanSource(file, print);
            }
            else{
                writeFile(file, print);
            }
        }
    }

    public static void writeFile(File input, PrintStream print) throws InterruptedException, IOException, IM4JavaException {
        // create command
        ConvertCmd cmd = new ConvertCmd();

        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.addImage(input.getAbsolutePath());
        op.resize(24, 24);

        op.addImage(output.getAbsolutePath() + "/" + input.getName());

        // execute the operation
        cmd.run(op);

        String name = input.getName().substring(0, input.getName().indexOf("."));
        print.println("emojis/" + input.getName() + "\t:" + name + ": " + (mappings.containsKey(input.getName()) ?  mappings.get(input.getName()) : ""));
    }
}
