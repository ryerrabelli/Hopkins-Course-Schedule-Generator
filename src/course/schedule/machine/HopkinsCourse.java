/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;
import course.schedule.machine.HopkinsClass.Semester;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
/**
 *
 * @author ryerrabelli
 */
public class HopkinsCourse extends GenericCourse {
    //  school.deptNum.courseNum
    //         AS.030.205
    protected RequiredCourseSet preReqs = new RequiredCourseSet(0);
    protected RequiredCourseSet coReqs = new RequiredCourseSet(0);
    protected String verbalName;
    protected HashMap<Integer, HopkinsClass> HopkinsClasses = new HashMap<Integer, HopkinsClass>();
    protected boolean isDesign = false;
    protected boolean isWritingIntensive = false;
    protected float priority = 0f;
    protected static HashMap<String, HopkinsCourse> allCourses = new HashMap<String, HopkinsCourse>();
    
    public HopkinsCourse(String courseTitle, String verbalName, String area, boolean isWritingIntensive, float creditsWorth, Semester semester, int year) {
        super(courseTitle, creditsWorth, area);
        
    }
    
    /*public HopkinsCourse(String courseTitle, int credits, String area, boolean isDesign, boolean isWritingIntensive){
        super(courseTitle, credits, area);
    }
    public HopkinsCourse(String courseTitle, int credits, String area){
        super(courseTitle, credits, area);
    }
        
    public HopkinsCourse (String courseTitle) {
        super(courseTitle, -1, "");
    }*/
    
    public HopkinsClass addHopkinsClass(int section, HopkinsClass hClass) {
        return HopkinsClasses.put(section, hClass);
    }
    
    public static HopkinsCourse getCourse(String courseTitle) {
        courseTitle = courseTitle.trim().toUpperCase();
        if (courseTitle.matches("[A-Z]{2}\\.[0-9]{3}\\.[0-9]{3,}")) {
            courseTitle = courseTitle.substring(3);
        }
        return allCourses.get(courseTitle);
    }
    public static boolean doesCourseExist(String courseTitle) {
        courseTitle = courseTitle.trim().toUpperCase();
        if (courseTitle.matches("[A-Z]{2}\\.[0-9]{3}\\.[0-9]{3,}")) {
            courseTitle = courseTitle.substring(3);
        }
        return allCourses.containsKey(courseTitle);
    }
    public static void putCourse(String courseTitle, HopkinsCourse course) {
        courseTitle = courseTitle.trim().toUpperCase();
        if (courseTitle.matches("[A-Z]{2}\\.[0-9]{3}\\.[0-9]{3,}")) {
            courseTitle = courseTitle.substring(3);
        }
        allCourses.put(courseTitle, course);
    }
    
    
    public boolean canTake(Set<HopkinsCourse> coursesTaken) {
        return preReqs.isFulfilled(coursesTaken);
    }
    
    @Override
    public boolean isFulfilled(Set<HopkinsCourse> coursesTaken) {
        return coursesTaken.contains(this);
    }
    
    public boolean addPreReq(Requirable preReq)
    {
        preReqs.add(preReq);
        return true;
    }
    public boolean addCoReq(Requirable preReq)
    {
        coReqs.add(preReq);
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
    
    public float getCredits()
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
        //return HopkinsClasses.toString();
        return getDeptNum()+"."+getCourseNum();
    }
}
