/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

/**
 *
 * @author rahulyerrabelli
 */
public class GenericCourse implements Requirable  {
    protected String deptNum = "";
    protected String courseNum = "";
    protected int credits = 0;
    protected Boolean isWhiting = null;;
    
    public GenericCourse() {
        
    }
    public boolean isSatisfiedBy(HopkinsCourse realCourse) {
        try {
            if (deptNum.isEmpty() || !deptNum.matches(realCourse.deptNum)) return false;
            if (courseNum.length() < 3 || Integer.parseInt(realCourse.courseNum) < Integer.parseInt(courseNum.substring(0,3))) return false;
            if (realCourse.credits < this.credits) return false;
            if (isWhiting != null && realCourse.isWhiting != this.isWhiting) return false;
        } catch (NumberFormatException NFE) { 
            System.out.println("Error: Dept number is not a number");
            return false;
        }
        return true;
    }
    
}
