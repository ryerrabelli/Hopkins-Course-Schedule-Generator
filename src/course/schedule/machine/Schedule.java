/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
/**
 * This is a schedule object. It holds the weekly course times for a HopkinsClass.
 * @author benka
 */
public class Schedule {
    
    private String days1;
    private String times1;
    private String days2;
    private String times2;
    private boolean hasMultipleTimes = false;
    
    public Schedule(String classTimes)
            {
        int changePoint = classTimes.indexOf(" "); // marks point between days and times
        days1 = classTimes.substring(0,changePoint);
        classTimes = classTimes.substring(changePoint+1);
        changePoint = classTimes.indexOf(",");
        if (changePoint == -1)
        {
            times1 = classTimes;
            times2 = "NA";
            days2 = "NA";
        }
        else
        {
            times1 = classTimes.substring(0,changePoint);
            classTimes = classTimes.substring(changePoint+2);
            changePoint = classTimes.indexOf(" ");
            days2 = classTimes.substring(0,changePoint);
            times2 = classTimes.substring(changePoint+1);
            hasMultipleTimes = true;
        }
    }
    
   
    
    public String getDays1() { return days1; }
    public String getTimes1() { return times1; }
    public String getDays2() { return days2; }
    public String getTimes2() { return times2; }
    public boolean getHasMultipleTimes() { return hasMultipleTimes; }
    
    @Override
    public String toString()
    {
        String result = days1 + " " + times1;
        if (this.getHasMultipleTimes())
            result+= ", " + days2 + " " + times2;
        return result;
    }
    
    
    public boolean conflicts(Schedule first, Schedule second) // not completed
    {
        if (first.getHasMultipleTimes() && second.getHasMultipleTimes())
        {
            if (checkDays(first.getDays1(),second.getDays1()))
                return checkTimes(first.getTimes1(), second.getTimes1());
            else if (checkDays(first.getDays1(),second.getDays2()))
                return checkTimes(first.getTimes1(), second.getTimes2());
            else if (checkDays(first.getDays2(),second.getDays1()))
                return checkTimes(first.getTimes2(), second.getTimes1());
            else if (checkDays(first.getDays2(),second.getDays2()))
                return checkTimes(first.getTimes2(), second.getTimes2());
        }
        else if (first.getHasMultipleTimes())
        {
            
        }
        else if (second.getHasMultipleTimes())
        {
            
        }
        else
        {
            
        }
        return false;
    }
    
    public static boolean checkTimes(String time1, String time2) // not completed
    {
        return false;
    }
    
    public static boolean checkDays(String day1, String day2) // not completed
    {
        String[] one = day1.split("");
        String[] two = day2.split("");
        List<String> first = Arrays.asList(one);
        List<String> second = Arrays.asList(two);
        for (int i = 1; i < first.size(); i++) // checks if Thursday is in array, if so, combines "T" and "h"
        {
            if ("Th".equals(first.get(i-1) + first.get(i)))
            {
                first.set(i-1,"Th");
                first.remove(i);
                break;
            }
        }
        
        for (int i = 1; i < second.size(); i++) // checks if Thursday is in array, if so, combines "T" and "h"
        {
            if ("Th".equals(second.get(i-1) + second.get(i)))
            {
                second.set(i-1,"Th");
                second.remove(i);
                break;
            }
        }
        for (String a: first)
        {
            if (second.contains(a))
                return true;
        }
        return false;
    }
    
    public static void main(String[] args)
    {
        Schedule a = new Schedule("MW 1:30PM - 2:45PM");
        Schedule b = new Schedule("MW 3:00PM - 4:15PM, F 3:00PM - 3:50PM");
        String c = "MWF";
        String d = "TThF";
        System.out.println(checkDays(c,d));
    }
}
