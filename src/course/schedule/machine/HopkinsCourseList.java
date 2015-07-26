/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.util.ArrayList;
import scheduleGenerator.DbFiles.ManageTxtFiles;
import java.util.HashSet;
import java.io.IOException;
/**
 *
 */
public class HopkinsCourseList {
    private HashSet<HopkinsClass> courseList;
    
    public HopkinsCourseList(String path) throws IOException
    {
        HashSet<Requirable> courses =  ManageTxtFiles.getRequiredCourses(path);
        courseList = new HashSet<HopkinsClass>();
        //convertStringtoHopkinsClass(courses,courseList);
    }
    
    public static void convertStringtoHopkinsClass(HashSet<String> courses, HashSet<HopkinsClass> courseList)
    {
        // needs completion
        for (String str: courses)
        {
            String separate = "==";
            String courseTitle = str.substring(0,str.indexOf(separate));
            str = str.substring(str.indexOf(separate) + separate.length());
            //prereqs will need to be handled in a separate way
            int credit = Integer.parseInt(str.substring(0,str.indexOf(separate)));
            str = str.substring(str.indexOf(separate) + separate.length());
            String area = str.substring(0,str.indexOf(separate));
            str = str.substring(str.indexOf(separate) + separate.length());
            int section = Integer.parseInt(str.substring(0,str.indexOf(separate)));
            str = str.substring(str.indexOf(separate) + separate.length());
            Schedule schedule = new Schedule(str);
            HopkinsClass sample = new HopkinsClass(courseTitle,section,schedule,HopkinsClass.Semester.FALL, 2015);
            courseList.add(sample);
        }
    }
    
    public String toString()
    {
        return courseList.toString();
    }
    
    
}
