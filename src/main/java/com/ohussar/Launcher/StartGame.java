package com.ohussar.Launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartGame {
    public static void startGame(){

        try {

            String res = Loader.FINAL_COMMAND.replace("%CLASSPATH%", "@bakedclasspath.txt");


            res = res.replace("${RAM}", "4096");
            res = res.replace("${auth_player_name}", "ohussar");
            res = res.replace("${auth_uuid}", "4652a9b1476c4079941f419e9ae17667");




            List<String> a = parseCommandString(res);
            for (String m : a){
                System.out.println(m);
                if(m.equals("Windows 10")){
                    a.set(a.indexOf(m),  "\"Windows 10\"");
                    System.out.println("GOT");
                    break;
                }
            }
            a = fixParsedTokens(a);

            ProcessBuilder pb = new ProcessBuilder(a);
            pb.directory(new File(System.getProperty("user.dir")));
            pb.inheritIO();
            Process process = pb.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<String> fixParsedTokens(List<String> rawTokens) {
        List<String> fixedTokens = new ArrayList<>();

        for (int i = 0; i < rawTokens.size(); i++) {
            String current = rawTokens.get(i);

            // Check if the current token ends in '=' or a known separator, AND it's not the last token.
            // This targets tokens like "-Dos.name=", "-Djava.net.preferIPv6Addresses=", etc.
            if (current.endsWith("=") && (i + 1) < rawTokens.size()) {
                String next = rawTokens.get(i + 1);

                // 1. Merge the key and value into a single token.
                //    Example: "-Dos.name=" + "Windows 10" (Note: "Windows 10" might still have quotes here)
                String mergedToken = current + next;

                // 2. Clean up any quotes that were left by the parser (e.g., in "Windows 10")
                mergedToken = mergedToken.replace("\"", "");

                fixedTokens.add(mergedToken);
                i++; // Skip the next token since we just merged it.
            } else {
                // Add all other tokens normally (executable, class name, flags like -Xss1M, etc.)
                fixedTokens.add(current);
            }
        }
        return fixedTokens;
    }
    public static List<String> parseCommandString(String commandString) {
        List<String> tokens = new ArrayList<>();

        // Regex:
        // 1. ([^\\s"=]+="[^"]*")  -> Matches -Dkey="value with spaces"
        // 2. | ("[^\"]*")         -> Matches simple quoted strings "like this"
        // 3. | (\\S+)             -> Matches any remaining non-space tokens (paths, -Xmx, etc.)
        Pattern pattern = Pattern.compile("([^\\s\"=]+=|\"[^\"]*\"|\\S+)");
        Matcher matcher = pattern.matcher(commandString);

        while (matcher.find()) {
            String token = matcher.group();

            // Check for and handle the special -Dkey="value" case
            if (token.contains("=\"") && token.endsWith("\"")) {
                // Example: -Dos.name="Windows 10"

                // 1. Remove the quotes from the value part
                int valueStartIndex = token.indexOf('\"');
                String key = token.substring(0, valueStartIndex + 1); // e.g., -Dos.name=
                String value = token.substring(valueStartIndex + 1, token.length() - 1); // e.g., Windows 10

                // 2. Reassemble without quotes
                tokens.add(key + value);

            } else if (token.startsWith("\"") && token.endsWith("\"")) {
                // Handle standalone quoted strings (e.g., if you had "my program.jar")
                tokens.add(token.substring(1, token.length() - 1));
            } else {
                tokens.add(token);
            }
        }
        return tokens;
    }
}
