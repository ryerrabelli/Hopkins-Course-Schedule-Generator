/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * This represents not just a course at JHU, but a specific class with a section, time, day, semester, year
 */
package course.schedule.machine;

/**
 *
 * @author rahulyerrabelli
 */
public class HopkinsClass {
    protected HopkinsCourse course;
    private int section, year;
    private Schedule schedule;
    private Semester semester;
    public enum Semester {INTERSESSION, SPRING, SUMMER, FALL;}
    public HopkinsClass(HopkinsCourse course, int section, Schedule schedule, Semester semester, int year) {
        this.course = course;
        this.section = section;
        this.schedule = schedule;
        this.semester = semester;
        this.year = year;
    }
    
    public int getSection() { return section; }
    public Schedule getSchedule() { return schedule; }   
    public Semester getSemester() { return semester;    }
    public int getYear() { return year; }
    
    @Override
    public String toString() {
        return course.getDeptNum()+"." + course.getCourseNum() + getSection() + " " + " " + course.getCredits() + " " + course.getArea()+ " " + getSchedule() + getSemester() + getYear();
    }
}

