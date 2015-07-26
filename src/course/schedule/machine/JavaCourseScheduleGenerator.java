/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
    
    public static ArrayList<HopkinsCourse> generateBestSchedule(Set<HopkinsCourse> coursesTaken, String...categories) {
        if (categories == null || categories.length ==0) return new ArrayList();
        try {
            RequiredCourseSet leastReqs = ManageTxtFiles.getRequiredCourses(categories[0]);
            for (int i = 1; i < categories.length; i++) {
                leastReqs = generateLeastRequirements(leastReqs, ManageTxtFiles.getRequiredCourses(categories[i]));
            }
            
            getRemainingRequirements:
            for (Iterator<Requirable> it = leastReqs.iterator(); it.hasNext();) {
                Requirable next = it.next();
                if (next.isFulfilled(coursesTaken)) {
                    it.remove();
                }
            }
            
            ArrayList<HopkinsCourse> prioritizedCourses = getPriorities(leastReqs);
            return prioritizedCourses;
        } catch (IOException ex) {
            System.out.println("Categories in generateBestSchedule could not be found: " + categories);
            return null;
        }
    }
    
    public static RequiredCourseSet generateLeastRequirements(RequiredCourseSet category1, RequiredCourseSet category2) {
           // try {

                HashSet<String> combined = new HashSet<String>();
                ArrayList<String> specials = new ArrayList<String>();
                for (Iterator<Requirable> i = category1.iterator();i.hasNext();) {
                    String cat1Str = i.next().toString();
                    if (cat1Str.startsWith("[") && cat1Str.endsWith("]")) cat1Str = cat1Str.substring(1, cat1Str.length()-1);
                    if (cat1Str.startsWith("[") && cat1Str.endsWith("]")) cat1Str = cat1Str.substring(1, cat1Str.length()-1);
                    if (cat1Str.endsWith("0") || cat1Str.contains("|") || cat1Str.toLowerCase().contains("or")) {specials.add(cat1Str);}
                    else combined.add(cat1Str);
                }
                for (Iterator<Requirable> i = category2.iterator(); i.hasNext();) {
                    String cat2Str = i.next().toString();
                    if (cat2Str.startsWith("[") && cat2Str.endsWith("]")) cat2Str = cat2Str.substring(1, cat2Str.length()-1).replace(", ", "and");
                    if (cat2Str.startsWith("{") && cat2Str.endsWith("}")) cat2Str = cat2Str.substring(1, cat2Str.length()-1).replace(", ", "or");
                    if (cat2Str.endsWith("0") || cat2Str.contains("|")|| cat2Str.toLowerCase().contains("or")) {
                        if (!specials.contains(cat2Str)) specials.add(cat2Str);
                    } else combined.add(cat2Str);
                }
                
                special:
                for (Iterator<String> iterator = specials.iterator(); iterator.hasNext();) {
                    String special = iterator.next();
                    if (special.contains("|") || special.toLowerCase().contains("or")) {
                        String[] specialparts = special.split("\\||OR|or|Or|oR");
                        boolean contains = false;
                        for (String specialpart : specialparts) {
                            if (combined.contains(specialpart.trim()) || courseMatches(combined, specialpart.trim())) { //second half of this may take a long time
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
          /*  } catch (IOException ex) {
                return null;
                
            } */
    } 
   
   //not finished
    /*public static int getNumberOfCoursesThatNeed(HopkinsCourse thisCourse) {
        allCourses.add(thisCourse);
        for (Iterator<HopkinsCourse> i = allCourses.iterator(); i.hasNext(); ) {
            HopkinsCourse checkingCourse = i.next();
            
        }
        return 0;
    }*/
   
    public static ArrayList<HopkinsCourse> getPriorities(RequiredCourseSet preReqs) {
        HashMap<HopkinsCourse, Float> priorities = new HashMap<>();
        addPriorities(priorities, preReqs, 1f);
        ArrayList<HopkinsCourse> sortedCourses = new ArrayList<>();
        Iterator<HopkinsCourse> it = priorities.keySet().iterator();
        sortedCourses.add(it.next());
        for (;it.hasNext();) {
            HopkinsCourse currentCourse = it.next();
            float currentPriority = priorities.get(currentCourse);
            boolean hasAdded;
            findSpot:
            for (int i = 0; i < sortedCourses.size();i++) { // Turn into binary sort later
                if (priorities.get(sortedCourses.get(i)) < currentPriority) {
                    sortedCourses.add(i, currentCourse);
                    break findSpot;
                } else if ( i == sortedCourses.size()-1) {
                    sortedCourses.add(currentCourse);
                    break findSpot;
                }
            }
        }
        return sortedCourses;
    }
   private static void addPriorities(Map<HopkinsCourse, Float> toAddTo, RequiredCourseSet preReqs, float factor) {
        if (preReqs == null || preReqs.isEmpty()) return;
        float priority = preReqs.getTrueNumRequired() * factor / preReqs.size();
        for (Requirable preReq : preReqs) {
            if (preReq instanceof HopkinsCourse) {
                addPriorities(toAddTo, ((HopkinsCourse) preReq).preReqs, priority);
                toAddTo.put((HopkinsCourse) preReq, priority);
            } else if (preReq instanceof RequiredCourseSet) {
                addPriorities(toAddTo, (RequiredCourseSet) preReq, priority);
            } // if it is a generic course, don't do anything 
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
