/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;
import java.util.ArrayList;
/**
 *
 * @author ryerrabelli
 */
public class HopkinsCourse {
    //  school.deptNum.courseNum
    //         AS.030.205
    private String deptNum = "";
    private String courseNum = "";
    private boolean isWhiting;
    private ArrayList<HopkinsCourse> preReqs;
    private int credits = 0;
    private String area = ""; 
    private int section;
    private String schedule;
    
    public HopkinsCourse(String courseTitle, int credits, String area, int section, String schedule) {
        this.credits = credits;
        this.area = area.toUpperCase().trim();
        preReqs = new ArrayList<HopkinsCourse>();
        String[] courseParts = courseTitle.split("\\Q.\\E", 3);
        if (courseParts.length == 3) {
            if (courseParts[0].trim().equalsIgnoreCase("EN")) isWhiting = true;
            else isWhiting = false;
            deptNum = courseParts[1].trim();
            courseNum = courseParts[2].trim();
        } else if (courseParts.length == 2) {
            deptNum = courseParts[0].trim();
            courseNum = courseParts[1].trim();
        } else System.out.println("Course title does not have enough parts: " + courseTitle);
      this.section = section;
      this.schedule = schedule;
    }
    
    public boolean addPreReq(HopkinsCourse preReq)
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
