/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author ryerrabelli
 */
public class Major {
    private ArrayList<HopkinsCourse> requiredCourses;
            
    public Major() {
        requiredCourses = new ArrayList<HopkinsCourse>();
    }
    
    public void addCourse(HopkinsCourse added)
    {
    requiredCourses.add(added);
    }
    
    public ArrayList<HopkinsCourse> getRequiredCourses()
    { return requiredCourses; }
    
    public String toString()
    {
        String result = "";
      for (int i = 0; i < getRequiredCourses().size(); i++)
      {
      result += getRequiredCourses().get(i).toString() + "\n";
      }
      return result;
    }
}
