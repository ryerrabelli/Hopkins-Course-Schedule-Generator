/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.util.ArrayList;
import scheduleGenerator.DbFiles.ManageTxtFiles;
import java.util.HashSet;
/**
 *
 */
public class HopkinsCourseList {
    private HashSet<HopkinsCourse> courseList;
    
    public HopkinsCourseList(String path)
    {
        HashSet<String> courses =  ManageTxtFiles.getRequiredCourses(path);
        courseList = new HashSet<HopkinsCourse>();
        convertStringtoCourse(courses,courseList);
    }
    
    public void convertStringtoCourse(HashSet<String> courses, HashSet<HopkinsCourse> courseList)
    {
        // needs completion
    }
    
    
}
