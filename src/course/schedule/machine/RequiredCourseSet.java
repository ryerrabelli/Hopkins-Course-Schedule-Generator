/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author ryerrabelli
 */
public class RequiredCourseSet extends HashSet implements Requirable {
    protected HashSet<Requirable> requiredCourses;
    protected int numRequired = 0; //0 means all courses are required, negative number means counting from the total num of reqs
    
    public RequiredCourseSet(int numRequired) {
        this.numRequired = numRequired;
    }
    public RequiredCourseSet(HashSet<Requirable> reqCourses, int numRequired) {
        this.numRequired = numRequired;
        this.requiredCourses = reqCourses;
    }
    public void addReq(GenericCourse course) {
        requiredCourses.add(course);
    }
    public void addReq(RequiredCourseSet reqCourses) {
        requiredCourses.add(reqCourses);
    }
    public void addReq(HashSet<Requirable> reqCourses, int numReq) {
        requiredCourses.add(new RequiredCourseSet(reqCourses, numReq));
    }
    
    public boolean isFulfilled(Set<HopkinsCourse> coursesTaken) {
        int reqsMet = 0;
        int reqsNeeded = numRequired <= 0 ? requiredCourses.size() + numRequired : numRequired; 
        for (Iterator<Requirable> i = requiredCourses.iterator(); i.hasNext();) {
            Requirable requirement = i.next();
            if (requirement instanceof HopkinsCourse) {
                if (coursesTaken.contains(requirement)) reqsMet++;
            } else if (requirement instanceof GenericCourse) {
                for (HopkinsCourse courseTaken : coursesTaken) {
                    if (((GenericCourse) requirement).isSatisfiedBy(courseTaken) ) reqsMet++;
                }
            } else if (requirement instanceof RequiredCourseSet) {
                if ( ((RequiredCourseSet) requirement).isFulfilled(coursesTaken)) reqsMet++;
            } else System.out.println("ERROR: Requirable is not an instance of GenericCourse or RequiredCourseSet: " + requirement);
        }
        return reqsMet >= reqsNeeded;
    }
}
