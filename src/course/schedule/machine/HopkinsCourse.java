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
    protected String area = ""; 
    protected int section;
    protected String schedule;
    
    public HopkinsCourse(String courseTitle, int credits, String area, int section, String schedule){
        this.credits = credits;
        this.area = area.toUpperCase().trim();
        preReqs = new RequiredCourseSet(0);
        String[] courseParts = courseTitle.split("\\Q.\\E", 3);
        if (courseParts.length == 3) {
            if (courseParts[0].trim().equalsIgnoreCase("EN")) isWhiting = true;
            else isWhiting = false;
            deptNum = courseParts[1].trim();
            courseNum = courseParts[2].trim();
        } else if (courseParts.length == 2) {
            deptNum = courseParts[0].trim();
            courseNum = courseParts[1].trim();
            try {
                isWhiting = Integer.parseInt(deptNum) >= 500;
            } catch (NumberFormatException ex) { System.out.println("Error: deptNum is not a number");}
        } else System.out.println("Course title does not have enough parts: " + courseTitle);
      this.section = section;
      this.schedule = schedule;
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
    
    public boolean getIsWhiting()
    { 
        return isWhiting; 
    }
    
    public String getArea()
    { return area; }
    
    public int getSection()
    { return section; }
    
    public String getSchedule()
    { return schedule; }
    @Override
    public String toString()
    {
    return getDeptNum()+"."+getCourseNum()+ " " + getCredits() + " " + getArea()+ " " + getSection() + " " + getSchedule();
    }
}
