/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author rahulyerrabelli
 */
public class GenericCourse extends Course {

    int identificationNum;
    
    protected HashSet<String> tags = new HashSet<>();
    
    public GenericCourse(String input) {
        String[] inputHalves = input.split("\\.", 2);
        deptNum = inputHalves[0].equals("000") ? "" : inputHalves[0];
        if (inputHalves[1].contains("<")) {
            courseNum = inputHalves[1].substring(0, inputHalves[1].indexOf("<"));
            String inner = inputHalves[1].substring(inputHalves[1].indexOf("<")+1,inputHalves[1].indexOf(">"));
            String[] innerParts = inner.split(",");
            for (String innerPart : innerParts) {
                if (innerPart.contains("=")) {
                    String preEq = innerPart.split("=",2)[0].trim();
                    String postEq = innerPart.split("=",2)[1].trim();
                    switch (preEq.toLowerCase()) {
                        case "area":
                        case "areas": 
                            area = postEq;
                            break;
                        case "credit":
                        case "credits":
                            credits = Float.parseFloat(postEq);
                            break;
                        case "tag":
                        case "tags":
                            tags.add(postEq.toLowerCase());
                    }
                } else {
                    if (innerPart.matches("-?\\d+(\\.\\d+)?")) credits = Float.parseFloat(innerPart);
                    else tags.add(innerPart.toLowerCase().trim());
                }
            }
        } else {
            this.credits = 0;
            courseNum = inputHalves[1];
        }
    }
    
    
 /*   public GenericCourse(String courseTitle, float credits, String area){
        super(courseTitle, credits, area);
    } */
    
    private boolean isCompatibleWith(GenericCourse otherCourse) { 
        if (!this.deptNum.isEmpty() && !otherCourse.deptNum.isEmpty() && !this.deptNum.equals(otherCourse.deptNum)) return false;
        if (courseNum.length()>=3  && otherCourse.courseNum.length()>=3 && !this.courseNum.equals(otherCourse.courseNum)) return false;
        return true;
    }
    
    boolean isSatisfiedBy(HopkinsCourse realCourse) {
        try {
            if (!deptNum.isEmpty() && !deptNum.matches(realCourse.deptNum)) return false;
            if (courseNum.length() == 3 && !courseNum.equalsIgnoreCase(realCourse.getCourseNum())) return false;
            if (courseNum.length() < 3) {
                if (Integer.valueOf(courseNum.charAt(0)) > Integer.valueOf(realCourse.getCourseNum().charAt(0))) return false;
            }
            if (realCourse.credits < this.credits) return false;
            if (school > 0 && realCourse.school != this.school) return false;
            for (String tag : tags) {
                if (!HopkinsCourse.getCoursesWithTag(tag).contains(realCourse)) return false;
            }
        } catch (NumberFormatException NFE) { 
            System.out.println("Error: Dept number is not a number");
            return false;
        }
        return true;
    }

    @Override
    public boolean isFulfilled(Set<HopkinsCourse> coursesTaken) { // NOTE CURRENTLY DOES NOT WORK WITH MULTIPLE GENERIC IDENTICAL GENERIC COURSES
        for (HopkinsCourse course : coursesTaken) {
            if (this.isSatisfiedBy(course)) return true;
        }
        return false;
    }
    
    public boolean isSubsetOf(GenericCourse genCourse) { //returns whether this GenericCourse can fit inside the input
        if (!deptNum.isEmpty() && !deptNum.equals(genCourse.deptNum)) return false;
        if (!courseNum.isEmpty()) {
            if (courseNum.length() >= 3 && !courseNum.equalsIgnoreCase(genCourse.courseNum)) return false;
            if (Integer.valueOf(courseNum.charAt(0)) > Integer.valueOf(genCourse.courseNum.charAt(0))) return false;
        }
        if (!genCourse.tags.containsAll(this.tags)) return false;
        if (this.credits > genCourse.credits) return false;
        if (school > 0 && school != genCourse.school) return false;
        return true;
    }
    
    public TreeSet<HopkinsCourse> getPossibleHopkinsCourses() {
        HashSet<HopkinsCourse> allCourseOptions = new HashSet<>(HopkinsCourse.allCourses.values());
        for (String tag : tags) {
            allCourseOptions.retainAll(HopkinsCourse.getCoursesWithTag(tag));
        }
        TreeSet<HopkinsCourse> toReturn = new TreeSet<HopkinsCourse>(new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                String str1 = ((Course) o2).courseNum;
                while (str1.length() < 3) str1 = str1 + " ";
                str1 = str1 + ((Course) o2).deptNum + ((Course) o2).area + ((Course) o2).credits + o2.hashCode();
                String str2 = ((Course) o1).courseNum;
                while (str2.length() < 3) str2 = str2 + " ";
                str2 = str2 + ((Course) o1).deptNum + ((Course) o1).area + ((Course) o1).credits + o1.hashCode();
                return str2.compareTo(str1);
            }
        });
        
        for (HopkinsCourse course: allCourseOptions) {
            if (this.isSatisfiedBy(course))
                toReturn.add(course);
        }
        return toReturn;
    }
    
}
