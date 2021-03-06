/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scheduleGenerator.DbFiles;

/**
 *
 * @author ryerrabelli
 */
import course.schedule.machine.*;
import course.schedule.machine.HopkinsClass.Semester;
import java.awt.Color;
import java.nio.charset.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    
    public static boolean hasCoursesBeenCalled = false;
    
    private static String r = "1";
    
    //School	Class #	Title	Areas	Writing Intensive	Max Seats	Open Seats	Waitlisted	Credits	Term	Location	Day-Times	Instructor(s)	Status	Select
    //Krieger School of Arts and Sciences	AS.020.103 (01)	Freshman Seminar: The Human Microbiome [+]	N	No	18	0	0	2.00	Fall 2015	Homewood Campus	T 4:30PM - 6:20PM	T. Feehery	Waitlist Only	
    
    public static void getAllCourses() throws IOException {
        String path = "./src/scheduleGenerator/DbFiles/ClassStorage";
        FileReader fr = new FileReader(path);
        BufferedReader txtRead = new BufferedReader(fr);
        HashSet<String> precoReqSet = new HashSet<String>();
        String currentLine = "";
        int numberOfLines = 1;
        
        beginningReader:
        while (!(currentLine = txtRead.readLine()).trim().equalsIgnoreCase("start")) numberOfLines++;
        
        hopkinsClassReader:
        while ( (currentLine = txtRead.readLine()) != null) {
            numberOfLines++;
            currentLine = currentLine.trim();
            if (currentLine.isEmpty() || currentLine.startsWith("//")) continue;
            String[] lineParts = currentLine.split("\t| {5,}");
            
            
            String courseNum;
            String verbalName;
            String area;
            boolean isWritingIntensive;
            boolean isDesign;
            boolean isLab;
            float creditsWorth;
            int year;
            Semester semester;
            String location;
            String schedule;
            String instructors;
            String status;
            int section = 0;
            try {
                //lineParts[0] school
                int start = 0;
                if (lineParts[0].startsWith("AS") || lineParts[0].startsWith("EN")) start--;
                try {
                    if (lineParts[start+1].contains("("))
                        section = Integer.parseInt( lineParts[start+1].substring(lineParts[start+1].indexOf("(")+1, lineParts[start+1].indexOf(")")) );
                } catch (IndexOutOfBoundsException | NumberFormatException ex) { System.out.println("error from getting class storage: part 1(" +lineParts[start+1] + ") of line " + numberOfLines); }
                courseNum = lineParts[start+1].substring(0, lineParts[start+1].indexOf("(")).trim();
                if (!courseNum.matches("(AS\\.|EN\\.)?\\d{3}\\.\\d{1,3}")) System.out.println("error from getting class storage. Illegal course number: part 1 of line " + numberOfLines);
                verbalName = lineParts[start+2].replace("[+]", "").trim();
                area = lineParts[start+3].trim().toUpperCase();
                isWritingIntensive = lineParts[start+4].trim().toUpperCase().equalsIgnoreCase("YES");
                String[] term = lineParts[start+9].trim().split(" ",2);
                try {
                    if (lineParts[start+8].contains("-")) creditsWorth = Float.parseFloat( lineParts[start+8].substring(0, lineParts[start+8].indexOf("-")) );
                    else creditsWorth = Float.parseFloat( lineParts[start+8] );
                    year = Integer.parseInt(term[1]);
                } catch (NumberFormatException NFE) { System.out.println("error from getting class storage: part 8-9 of line " + numberOfLines); continue hopkinsClassReader; } 
                try {
                    semester = Semester.valueOf(term[0].toUpperCase());
                } catch (IllegalArgumentException IAE) { System.out.println("error from getting class storage: part 9(" + lineParts[start+9] + ") of line " + numberOfLines); continue hopkinsClassReader; }
                location = lineParts[start+10].trim().toUpperCase();
                schedule = lineParts[start+11].trim().toUpperCase();
                instructors = lineParts[start+12].trim();
                status = lineParts[start+13].trim().toUpperCase();
            } catch (IndexOutOfBoundsException ex) {
                System.out.println("error from getting class storage: not enough lineparts on " + numberOfLines);
                continue hopkinsClassReader;
            }
        
            if (lineParts.length >= 15) {
                
            }
            
            String followingLine;
            
            String precoReq = "";
            
            forwardLooker:
            while (true) {
                txtRead.mark(5);
                char[] forwardLook = new char[5];
                if (txtRead.read(forwardLook, 0, 5) >= 0) {
                    txtRead.reset();
                    String forwardLookStr = new String(forwardLook).toLowerCase();
                    if (forwardLookStr.isEmpty() || forwardLookStr.matches("whiti|krieg|//.*|en.*|as.*")) {
                        break forwardLooker;
                    } else {     
                        precoReq = precoReq + newLn + txtRead.readLine();
                        int i = 2;
                    }
                } else {
                    txtRead.reset();
                    break forwardLooker;
                }
            }
            
            if (courseNum.matches("[a-zA-Z]{2}\\.[0-9]{3}\\.[0-9]+")) courseNum = courseNum.substring(3);
            if (!precoReq.isEmpty()) precoReqSet.add(courseNum + precoReq);
            HopkinsClass thisClass = new HopkinsClass(courseNum, section, new Schedule(schedule), semester, year);
            if (HopkinsCourse.doesCourseExist(courseNum)) {
                HopkinsCourse.getCourse(courseNum).addHopkinsClass(section, thisClass);
            } else  {
                //String courseTitle, String verbalName, String area, boolean isWritingIntensive, boolean isDesign, boolean isLab, float creditsWorth, Semester semester, int year
                HopkinsCourse toAdd = new HopkinsCourse(courseNum, verbalName, area, isWritingIntensive, false, false, creditsWorth, semester, year);
                toAdd.addHopkinsClass(section, thisClass);
                HopkinsCourse.putCourse(courseNum, toAdd);
            }
        }
        txtRead.close();
        fr.close();

        //AP chem score of 4
        HopkinsCourse.putCourse("030.100",new HopkinsCourse("030.100","AP Chem Score of 4", "N", false, false , false, 4, Semester.SPRING, 2014));
        
        
        addingRequirements:
        for (Iterator<String> it = precoReqSet.iterator(); it.hasNext();) {
            String[] lines = it.next().split(newLn);
            HopkinsCourse course = HopkinsCourse.getCourse(lines[0]);
            RequiredCourseSet coReqs = null, preReqs = null;
            for (int lineNum = 1; lineNum < lines.length; lineNum++) {
                String line = lines[lineNum];
                    if(line.toLowerCase().contains("req") || line.toLowerCase().contains("rec")) {
                        if (line.toLowerCase().contains("core")) coReqs = RequiredCourseSet.stringToRequiredCourseSet(line);
                        else preReqs = RequiredCourseSet.stringToRequiredCourseSet(line);
                    } else if (line.contains("=") || line.contains("cross")) {

                    }
            }
            if (preReqs != null) {
                for (Requirable req : preReqs) {
                    if (req instanceof HopkinsCourse) {
                        
                    }
                }
                course.addPreReq(preReqs);
            }
            if (coReqs != null) course.addCoReq(coReqs);
            
        }
        hasCoursesBeenCalled = true;
        addTags();
    }
    private static void addTags() throws IOException {
        String path = "./src/scheduleGenerator/DbFiles/TagStorage";
        FileReader fr = new FileReader(path);
        BufferedReader txtRead = new BufferedReader(fr);
        HashSet<String> precoReqSet = new HashSet<String>();
        String currentLine = "";
        int numberOfLines = 1;
        
        beginningReader:
        while (!(currentLine = txtRead.readLine()).trim().equalsIgnoreCase("start")) numberOfLines++;
        
        tagReader:
        while ((currentLine = txtRead.readLine()) != null) {
            numberOfLines++;
            currentLine = currentLine.trim();
            if (currentLine.isEmpty() || currentLine.startsWith("//")) continue;
            numberOfLines++;
            currentLine = currentLine.trim();
            if (currentLine.isEmpty() || currentLine.startsWith("//")) continue;
            String[] lineParts = currentLine.split("=", 2);
            String tagName = lineParts[0].toLowerCase().trim();
            HashSet<HopkinsCourse> taggedCourses = new HashSet<>();
            for (String course : lineParts[1].split(",")) {
                if (!course.trim().startsWith("//")) {
                    HopkinsCourse toAdd = HopkinsCourse.getCourse(course);
                    if (toAdd == null) System.out.println("Note tagged course \"" + course.trim() + "\" is can't be found");
                    else taggedCourses.add(toAdd);
                }
            }
            HopkinsCourse.createNewTag(tagName, taggedCourses);
        }
    }
    
    public static  RequiredCourseSet getRequiredCourses(String category) throws IOException {
        String standardFilePath = "./src/scheduleGenerator/DbFiles/";
        category = category.toLowerCase();
        category = category.replace(".", "/");
        category = category.replace(" ", "_");
        category = category.replace("or/", "ors/");
        RequiredCourseSet fromMap = RequiredCourseSet.categoryRequiredCourseSets.get(category.toLowerCase().trim().replace(" ", "_"));
        if (fromMap != null) return fromMap.duplicate();
        
        String path = standardFilePath + category;
        FileReader fr;
        try {
            fr = new FileReader(path);
            BufferedReader txtread = new BufferedReader(fr);
            String Aline;
            int NumberOfLines = 0;
            RequiredCourseSet reqs = new RequiredCourseSet(0);
            boolean started = false;
            while ((Aline = txtread.readLine()) != null) {
                if (Aline.isEmpty() || Aline.startsWith("//")) continue;
                if (started) {
                    NumberOfLines++;
                    reqs.add(RequiredCourseSet.stringToRequirable(Aline));
                } else if (Aline.trim().equalsIgnoreCase("start")) {
                    started = true;
                }

            }
            txtread.close();
            fr.close();
            return reqs;
        } catch (FileNotFoundException ex) {
            System.out.println("FILE NOT FOUND: " + path);
            return new RequiredCourseSet(0);
        }
    }
    
    
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
        br.close();
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
