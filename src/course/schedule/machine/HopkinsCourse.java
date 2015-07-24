/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;
import java.util.ArrayList;
import java.util.Set;
/**
 *
 * @author ryerrabelli
 */
public class HopkinsCourse extends GenericCourse {
    //  school.deptNum.courseNum
    //         AS.030.205
    protected RequiredCourseSet preReqs;
    
    public HopkinsCourse(String courseTitle, int credits, String area, boolean isDesign, boolean isWritingIntensive){
        super(courseTitle, credits, area);
        preReqs = new RequiredCourseSet(0);
    }
    public HopkinsCourse(String courseTitle, int credits, String area){
        super(courseTitle, credits, area);
        preReqs = new RequiredCourseSet(0);
    }
    
    public boolean canTake(Set<HopkinsCourse> coursesTaken) {
        return preReqs.isFulfilled(coursesTaken);
    }
    
    public boolean addPreReq(Requirable preReq)
    {
        preReqs.add(preReq);
        return true;
    }
    
    public String getCourseNum()
    {
        return courseNum;
    }
    
    public String getDeptNum()
    {
        return deptNum;
    }
    
    public int getCredits()
    {
        return credits;
    }
    
    public int getSchool() { 
        return school; 
    }
    
    public String getArea()
    { return area; }
    
    @Override
    public String toString()
    {
        return getDeptNum()+"."+getCourseNum()+ " " + getCredits() + " " + getArea();
    }
}
