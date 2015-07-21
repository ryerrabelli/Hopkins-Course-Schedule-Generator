/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package java.course.schedule.generator;

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
    
    private int credits = 0;
    private String area = ""; 
    
    public HopkinsCourse(String courseTitle, int credits, String area) {
        this.credits = credits;
        this.area = area.toUpperCase().trim();
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
        
    }
    
}
