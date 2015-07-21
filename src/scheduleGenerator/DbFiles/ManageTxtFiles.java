/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scheduleGenerator.DbFiles;

/**
 *
 * @author ryerrabelli
 */
import java.awt.Color;
import java.nio.charset.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
//import physics.Physics;
//import physics.Unicode;

public class ManageTxtFiles {
    
    public static final String newLn = System.getProperty("line.separator");
    public static final String newLnHTML = "<br>";
    
    private final static String optionsFilePath = "/Users/ryerrabelli/Physics/src/physics/DbFiles/Options";
    private final static String loginPath = "/Users/ryerrabelli/Physics/src/physics/DbFiles/Logins.txt";
    private final static String helpFolder = "/Users/ryerrabelli/Physics/src/physics/DbFiles/Help/";
    
    private static String r = "1";
    public static Boolean CheckPassword (String Username, char[] Password) throws IOException
    {
        String r; String FullPass = "";
        for (int i = 0; i < Password.length; i++)
        {
            FullPass = FullPass + Password[i];
        }
        return isLineInTxt(loginPath, Username.toLowerCase() + " . " + FullPass);
    }

    public static Object getValue(String key) throws IOException {
        int line = findLineContaining(optionsFilePath, key.trim() + " . ");
        String answer = getLine(optionsFilePath,line).split(" . ", 2)[1];
        if (answer.equalsIgnoreCase("default")) return null;
        if (answer.equalsIgnoreCase("true") || answer.equalsIgnoreCase("false")) //boolean
            return Boolean.getBoolean(answer.toLowerCase());
        else if (answer.startsWith("#")) { //color
            answer = answer.replaceFirst("#", "");
            int base = Integer.valueOf(answer.substring(0, 2));
            answer = answer.substring(2).trim();
            Color c = new Color(Integer.valueOf(answer, base));
            return c;
        } else return answer;
    }
    
    public static String getLineOfLinedString(int lineNum, String txt) {
        String[] lines = txt.split(newLn);
        int size = lines.length;
        if (lineNum >= 0) {
            while (lineNum > size) lineNum -= size;
            return lines[lineNum];
        } else {
            while (lineNum < -1 * size) lineNum += size;
            return lines[size + lineNum];
        }
    }
    public static String getLineOfLinedStringRev(int lineNum, String txt) {
        String[] lines = txt.split(newLn);
        String stringrep = "";
        int size = lines.length;
        for (int i = size; i > 0; i--) {
            stringrep = stringrep + lines[i];
        }
        return getLineOfLinedString(lineNum, stringrep);
    }
    
    public static Boolean isLineInTxt(String path, String whattofind) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader txtread = new BufferedReader(fr);
        String Aline;
        int NumberOfLines = 0;
        while ((Aline = txtread.readLine()) != null) {
            NumberOfLines++;

            if (Aline.equals(whattofind)) {
                txtread.close();
                return true;
            }

        }
        txtread.close();
        return false;
    }

    /**
     *
     * @param path
     * @param whattofind
     * @return Returns Which Number Lines *Hence FindLinetxt...*
     * @throws IOException
     */
    public static int findLineNumberInTxt(String path, String whattofind) throws IOException { 
        FileReader fr = new FileReader(path);
        BufferedReader txtread = new BufferedReader(fr);
        String Aline;
        int NumberOfLines = 0;
        while ((Aline = txtread.readLine()) != null) {
            NumberOfLines++;

            if (Aline.equals(whattofind)) {
                txtread.close();

                return NumberOfLines++;
            }

        }
        txtread.close();

        return -1;

    }

    public static Boolean isStringInTxt(String path, String whattofind) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader txtread = new BufferedReader(fr);
        String Aline;
        int NumberOfLines = 0;
        while ((Aline = txtread.readLine()) != null) {
            NumberOfLines++;

            if (Aline.contains(whattofind)) {
                txtread.close();

                return true;
            }

        }
        txtread.close();

        return false;
    }

    /**
     *
     * @param path
     * @param whattofind
     * @return Returns Number of Lines *Hence FindLinetxt...*
     * @throws IOException
     */
    public static int findLineContaining(String path, String whattofind) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader txtread = new BufferedReader(fr);
        String Aline;
        int NumberOfLines = 0;
        while ((Aline = txtread.readLine()) != null) {
            NumberOfLines++;

            if (Aline.contains(whattofind) && !Aline.startsWith("//")) {
                txtread.close();

                return NumberOfLines++;
            }

        }
        txtread.close();

        return -1;
    }

    public static String getLine(String path, int whichline) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader txtread = new BufferedReader(fr);
        for (int i = 1; i < whichline; i++) {
            txtread.readLine();
        }
        String toReturn = txtread.readLine();
        txtread.close();
        return toReturn;
    }
    
    public static String getLoginPath() {return loginPath;}
    public static String getOptionsFilePath() {return optionsFilePath;}
    
    public static ArrayList<String> helpSearchResults(List<String> input) throws IOException{
        File helpHome = new File(helpFolder);
        List<File> files = Arrays.asList(helpHome.listFiles());
        Map<File, Integer> scores = new HashMap<>();
        ArrayList<String> searchResults = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            String title, body;
            int score = 0;
            try { title = getTitle(files.get(i));
            } catch (IOException IOE) { title = "";}
            try { body = getBody(files.get(i));
            } catch (IOException IOE) { body = "";}
            for (String aninput : input) {
                if (title.toLowerCase().contains(aninput.toLowerCase())) {
                    score += 5;
                }
                if (body.toLowerCase().contains(aninput.toLowerCase())) {
                    score += 1;
                }
            }
            scores.put(files.get(i), score);
        }
        ArrayList<Integer> orderedVals = new ArrayList<>(scores.values());
        Collections.sort(orderedVals);
        int max = Math.max(Collections.max(orderedVals), 1);
        int summaryLength = 100;
        for (int i = 0; i < files.size(); i++) {
            int score = scores.get(files.get(i));
            if (score >=  1 && max - score < Math.min(2, score)) {// /Users/ryerrabelli/Physics/src/physics/DbFiles/Help/
                String represent = "<a href=\"file://" + helpFolder + files.get(i).getName() + "\"><strong>"+getTitle(files.get(i))+"</strong></a>";
                String body = getBody(files.get(i));
                if (body.length() > summaryLength+3) body = body.substring(0, summaryLength) + "...";
                represent = represent + newLnHTML + "<em>" +body + "</em>";
                searchResults.add(represent);
            }
        }
        return searchResults;
    }
    public static String getTitle(File file) throws IOException{
        return getSection(file, "<TITLE>", "</TITLE>");
    }
    public static String getBody(File file) throws IOException{
        return getSection(file, "<BODY>", "</BODY>");
    }
    public static String getSection(File file, String startStr, String endStr) throws IOException {// Must be on their own line. Start and end will not be included
        FileReader fr = new FileReader(file.getPath());
        BufferedReader br = new BufferedReader(fr);
        String Aline, section = null;
        int startNum, endNum, NumberOfLines=0;
        while ((Aline = br.readLine()) != null) {
            //Aline = Aline.trim();
            NumberOfLines++;
            if (Aline.contains(startStr)) {
                section = Aline.substring(Aline.indexOf(startStr)+startStr.length());
                startNum = NumberOfLines;
                if (section.contains(endStr)) {
                    section = section.substring(0, section.indexOf(endStr));
                    endNum = NumberOfLines;
                } else while (true) {
                        Aline = br.readLine();
                        NumberOfLines++;
                        if (Aline == null || Aline.contains(endStr)) {
                            section = section + Aline.substring(0, Aline.indexOf(endStr));
                            endNum = NumberOfLines;
                            break;
                        } else {
                            section = section + Aline.trim();
                        }
                    }
            }
        }
        return section;
    }
    
    /*public static void println(Object toPrint, Object toPrint2) {
        print(toPrint + ", ");
        println(toPrint2);
    }*/
            
    /*public static void println(Object toPrint) {
        System.out.println(Physics.toStringOrNull(toPrint));
    }*/
    public static void println() {
        System.out.println();
    }
    public static void print(Object toPrint) {
        System.out.print(toPrint);
    }
    public static void print(Object toPrint, Object toPrint2) {
        print(toPrint + ", ");
        print(toPrint2);
    }
}