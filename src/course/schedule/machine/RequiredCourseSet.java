/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author ryerrabelli
 */
public class RequiredCourseSet extends HashSet<Requirable>  implements Requirable {
    protected int numRequired = 0; //0 means all courses are required, negative number means counting from the total num of reqs
    
    public RequiredCourseSet(int numRequired) {
        this.numRequired = numRequired;
    }
    public RequiredCourseSet(HashSet<Requirable> reqCourses, int numRequired) {
        this.numRequired = numRequired;
        this.addAll(reqCourses);
        
    }
    public RequiredCourseSet(Object req1, Object req2, int numRequired) {
        this.numRequired = numRequired;
        this.add(new HopkinsCourse(req1.toString(), 4, "N"));
        this.add(new HopkinsCourse(req2.toString(), 4, "N"));
    }
    //Not finished, will duplicate
    public RequiredCourseSet(RequiredCourseSet reqCourseSet) {
        for (Iterator<Requirable> i = reqCourseSet.iterator(); i.hasNext(); ) {
            Requirable requirement = i.next();
            
        }
    }
    
    
    public void addReq(GenericCourse course) {
        this.add(course);
    }
    public void addReq(RequiredCourseSet reqCourses) {
        this.add(reqCourses);
    }
    public void addReq(HashSet<Requirable> reqCourses, int numReq) {
        this.add(new RequiredCourseSet(reqCourses, numReq));
    }
    
    public boolean isFulfilled(Set<HopkinsCourse> coursesTaken) {
        int reqsMet = 0;
        int reqsNeeded = numRequired <= 0 ? this.size() + numRequired : numRequired; 
        for (Iterator<Requirable> i = this.iterator(); i.hasNext();) {
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
