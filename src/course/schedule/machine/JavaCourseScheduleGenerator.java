/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import scheduleGenerator.DbFiles.ManageTxtFiles;

/**
 *
 * @author ryerrabelli
 */
public class JavaCourseScheduleGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
   public static HashSet<HopkinsCourse> generateCourseSchedule() {
            try {
                HashSet<String> set1 = ManageTxtFiles.getRequiredCourses("chemBE");
                HashSet<String> set2 = ManageTxtFiles.getRequiredCourses("premed");
                HashSet<String> combined = new HashSet<String>();
                ArrayList<String> specials = new ArrayList<String>();
                for (Iterator<String> i = set1.iterator();i.hasNext();) {
                    String set1str = i.next();
                    if (set1str.endsWith("0") || set1str.contains("|") || set1str.toLowerCase().contains("or")) {specials.add(set1str);}
                    else combined.add(set1str);
                }
                for (Iterator<String> i = set2.iterator(); i.hasNext();) {
                    String set2str = i.next();
                    if (set2str.endsWith("0") || set2str.contains("|")|| set2str.toLowerCase().contains("or")) {
                        if (!specials.contains(set2str)) specials.add(set2str);
                    } else combined.add(set2str);
                }
                
                special:
                for (Iterator<String> iterator = specials.iterator(); iterator.hasNext();) {
                    String special = iterator.next();
                    if (special.contains("|") || special.toLowerCase().contains("or")) {
                        String[] specialparts = special.split("\\||OR|or|Or|oR");
                        boolean contains = false;;
                        for (String specialpart : specialparts) {
                            if (combined.contains(specialpart.trim()) || courseMatches(combined, specialpart.trim())) {
                                contains = true;
                                iterator.remove();
                                continue special;
                            }
                        }
                        if (!contains) combined.add(special);
                    } else if (special.endsWith("00")) {
                        if (courseMatches(combined, special)) {
                            iterator.remove();
                            continue special;
                        }
                    }
                }
                for (String str : specials) combined.add(str);
                ArrayList<String> sortedcombined = new ArrayList<String>(combined);
                Collections.sort(sortedcombined);
                System.out.println("combined: " + sortedcombined);
                System.out.println("	".matches("\\t"));
                /*if (set1.size() > set2.size()) {
                    Iterator<String> iter2 = set2.iterator();
                    while (iter2.hasNext()) {
                        String course2 = iter2.next();
                        if (set1.contains(course2)) {
                            
                        } else {
                            Iterator<String> iter1 = set1.iterator();
                            while (iter1.hasNext()) {
                                String course1 = iter1.next();
                                
                            }
                        }
                        //System.out.println(iter.next());
                    }
                }*/
                return null;
            } catch (IOException ex) {
                return null;
            }
    }
   
   private static boolean courseMatches(Collection<String> courses, String genericCourse) {
       while (genericCourse.endsWith("0")) {
           genericCourse = genericCourse.substring(0, genericCourse.length() - 1);
       }
       genericCourse = ".*" + genericCourse.substring(0, genericCourse.length() - 1) + "[" + genericCourse.substring(genericCourse.length() - 1) + "-9].*";
       for (String course : courses) {
           if (course.matches(genericCourse)) return true;
       }
       return false;
   }
}
