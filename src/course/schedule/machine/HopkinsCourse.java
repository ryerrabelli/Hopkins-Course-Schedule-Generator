/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;
import course.schedule.machine.HopkinsClass.Semester;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author ryerrabelli
 */
public class HopkinsCourse extends Course {
    //  school.deptNum.courseNum
    //         AS.030.205
    protected RequiredCourseSet preReqs = new RequiredCourseSet(0);
    protected RequiredCourseSet coReqs = new RequiredCourseSet(0);
    protected String verbalName;
    protected HashMap<Integer, HopkinsClass> HopkinsClasses = new HashMap<Integer, HopkinsClass>();
    protected boolean isDesign = false;
    protected boolean isWritingIntensive = false;
    protected boolean isLab = false;
    protected float priority = 0f;
    protected static HashMap<String, HopkinsCourse> allCourses = new HashMap<String, HopkinsCourse>();
    protected static HashMap<String, HashSet<HopkinsCourse>> tagMap = new HashMap<String, HashSet<HopkinsCourse>>();
    
    public HopkinsCourse(String courseTitle, String verbalName, String area, boolean isWritingIntensive, boolean isDesign, boolean isLab, float creditsWorth, Semester semester, int year) {
        this.credits = credits;
        this.area = area.toUpperCase().trim();
        String[] courseParts = courseTitle.split("\\Q.\\E", 3);
        if (courseParts.length == 3) {
            courseParts[0] = courseParts[0].trim().toUpperCase();
            if (courseParts[0].length() == 0) school = 0;
            else switch (courseParts[0]) {
                    case "AS": school = 1;
                    case "EN": school = 2;
                    case "PP":
                    case "PR": school = 3;
                    case "TR": school = 5;
                    default: school = 4;
                }
            /*Before java 1.7 if (courseParts[0].equals("AS")) school = 1;
            else if (courseParts[0].equals("EN")) school = 2;
            else if (courseParts[0].equals("PP") || courseParts[0].equals("PY")) school = 3;
            else if (courseParts[0].equals("TR")) school = 5;
            else school = 4; */
            deptNum = courseParts[1].trim();
            courseNum = courseParts[2].trim();
        } else if (courseParts.length == 2) {
            deptNum = courseParts[0].trim();
            courseNum = courseParts[1].trim();
            try {
                school = Integer.parseInt(deptNum) >= 500 ? 2 : 1;
            } catch (NumberFormatException ex) { System.out.println("Error: deptNum is not a number");}
        } else System.out.println("Course title does not have enough parts: " + courseTitle);
        // MUST FINISH LATER
        
    }
    
    public static void createNewTag(String tagName, HashSet<HopkinsCourse> tagElements) {
        tagMap.put(tagName, tagElements);
    }
    public static HashSet<HopkinsCourse> getCoursesWithTag(String tagName) {
        HashSet<HopkinsCourse> toReturn = tagMap.get(tagName);
        return toReturn == null ? new HashSet<HopkinsCourse>() : toReturn;
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
        switch (hClass.getSemester()) {
            case INTERSESSION:
                section += 200;
                break;
            case SPRING:
                section += 100;
                break;
            case SUMMER:
                section += 300;
                break;          
            case FALL:
                break;                
        }
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
