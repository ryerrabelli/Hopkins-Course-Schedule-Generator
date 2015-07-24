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
    protected int school = 0; //0 - not determined 1-krieger 2-whiting 3- Peabody 4-other jhu   5-transfer
    protected Boolean isDesign = null;
    protected Boolean isWritingIntensive = null;
    protected String area = ""; 
    int identificationNum;
    
    public GenericCourse(String courseTitle, int credits, String area){
        this.credits = credits;
        this.area = area.toUpperCase().trim();
        String[] courseParts = courseTitle.split("\\Q.\\E", 3);
        if (courseParts.length == 3) {
            courseParts[0] = courseParts[0].trim().toUpperCase();
            switch (courseParts[0]) {
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
    }
    
    public boolean isSatisfiedBy(HopkinsCourse realCourse) {
        try {
            if (deptNum.isEmpty() || !deptNum.matches(realCourse.deptNum)) return false;
            if (courseNum.length() < 3 || Integer.parseInt(realCourse.courseNum) < Integer.parseInt(courseNum.substring(0,3))) return false;
            if (realCourse.credits < this.credits) return false;
            if (school > 0 && realCourse.school != this.school) return false;
        } catch (NumberFormatException NFE) { 
            System.out.println("Error: Dept number is not a number");
            return false;
        }
        return true;
    }
    
}
