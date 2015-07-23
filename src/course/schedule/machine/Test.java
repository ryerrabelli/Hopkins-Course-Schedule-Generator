/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.io.IOException;
import java.util.HashSet;
import scheduleGenerator.DbFiles.ManageTxtFiles;

/**
 *just tests our code
 */
public class Test {
    public static void main(String[] args) throws IOException
    {
        
        HopkinsCourseList o = new HopkinsCourseList("chemBE");
        System.out.println(o);
    }
}
