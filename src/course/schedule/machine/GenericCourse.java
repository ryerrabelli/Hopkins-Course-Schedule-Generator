/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.util.Set;

/**
 *
 * @author rahulyerrabelli
 */
public class GenericCourse extends Course {

    int identificationNum;
    
    public GenericCourse(String courseTitle, float credits, String area){
        super(courseTitle, credits, area);
    }
    
    private boolean isCompatibleWith(GenericCourse otherCourse) { 
        if (!this.deptNum.isEmpty() && !otherCourse.deptNum.isEmpty() && !this.deptNum.equals(otherCourse.deptNum)) return false;
        if (courseNum.length()>=3  && otherCourse.courseNum.length()>=3 && !this.courseNum.equals(otherCourse.courseNum)) return false;
        return true;
    }
    
    boolean isSatisfiedBy(HopkinsCourse realCourse) {
        try {
            if (deptNum.isEmpty() || !deptNum.matches(realCourse.deptNum)) return false;
            if (courseNum.length() == 3 && !courseNum.equalsIgnoreCase(realCourse.getCourseNum())) return false;
            if (courseNum.length() < 3) {
                if (Integer.valueOf(courseNum.charAt(0)) > Integer.valueOf(realCourse.getCourseNum().charAt(0))) return false;
            }
            if (realCourse.credits < this.credits) return false;
            if (school > 0 && realCourse.school != this.school) return false;
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
        if (this.credits > genCourse.credits) return false;
        if (school > 0 && school != genCourse.school) return false;
        return true;
    }
    
}
