/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.io.IOException;
import java.util.ArrayList;
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
                ArrayList<String> set1 = new ArrayList<String>(ManageTxtFiles.getRequiredCourses("chemBE"));
                ArrayList<String> set2 = new ArrayList<String>(ManageTxtFiles.getRequiredCourses("premed"));
                HashSet<String> combined = new HashSet<String>();
                ArrayList<String> specials = new ArrayList<String>();
                for (int i = 0; i < set1.size(); i++) {
                    if (set1.get(i).endsWith("0") || set1.get(i).contains("|") || set1.get(i).toLowerCase().contains("or")) {specials.add(set1.get(i));}
                    else combined.add(set1.get(i));
                }
                for (int i = 0; i < set2.size(); i++) {
                    if (set2.get(i).endsWith("0") || set2.get(i).contains("|")|| set1.get(i).toLowerCase().contains("or")) {
                        if (!specials.contains(set2.get(i))) specials.add(set2.get(i));
                    } else combined.add(set2.get(i));
                }
                special:
                for (Iterator<String> iterator = specials.iterator(); iterator.hasNext();) {
                    String special = iterator.next();
                    if (special.contains("|") || special.toLowerCase().contains("or")) {
                        String[] specialparts = special.split("\\||OR|or|Or|oR");
                        boolean contains = false;;
                        for (String specialpart : specialparts) {
                            if (combined.contains(specialpart.trim())) {
                                iterator.remove();
                                continue special;
                            }
                        }
                        if (!contains) combined.add(special);
                    } else if (special.endsWith("00")) {
                        while (special.endsWith("0")) special = special.substring(0, special.length()-1);
                        special = ".*"+special.substring(0, special.length()-1) + "[" + special.substring(special.length()-1) + "-9].*";
                        for (String str : combined) {
                            if (str.matches(special)) {
                                iterator.remove();
                                continue special;
                            }
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
}
