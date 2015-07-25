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
    
    private String days1AsString;
    private String times1;
    private String days2AsString;
    private String times2;
    private ArrayList<Integer> days1AsInt;
    private ArrayList<Integer> days2AsInt;
    private boolean hasMultipleTimes = false;
    
    public Schedule(String classTimes)
            {
        int changePoint = classTimes.indexOf(" "); // marks point between days and times
        days1AsString = classTimes.substring(0,changePoint);
        days1AsInt = convertDaysToInt(days1AsString);
        classTimes = classTimes.substring(changePoint+1);
        changePoint = classTimes.indexOf(",");
        if (changePoint == -1)
        {
            times1 = classTimes;
            times2 = "NA";
            days2AsString = "NA";
        }
        else
        {
            times1 = classTimes.substring(0,changePoint);
            classTimes = classTimes.substring(changePoint+2);
            changePoint = classTimes.indexOf(" ");
            days2AsString = classTimes.substring(0,changePoint);
            times2 = classTimes.substring(changePoint+1);
            hasMultipleTimes = true;
        }
    }
    /** public boolean conflicts(Schedule first, Schedule second) // not completed
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
    * */
   public static ArrayList<Integer> convertDaysToInt(String days)
   {
       ArrayList<Integer> result = new ArrayList<Integer>();
       days = days.toUpperCase();
       if (days.contains("M"))
           result.add(1);
       if (days.contains("T"))
       {
           if (!(days.contains("TH")))
                   result.add(2);
           else if (days.contains("TH"))
           {
               int index = days.indexOf("TH");
               String firstpart = days.substring(0,index);
               String secondpart = days.substring(index);
               if (firstpart.contains("T"))
                   result.add(2);
               result.add(4);
           }
           
       }
       if (days.contains("W"))
           result.add(3);
       if (days.contains("F"))
           result.add(5);
      
    
       return result;
   }
   
   
    
    public String getDays1AsString() { return days1AsString; }
    public ArrayList<Integer> getDays1AsInt() { return days1AsInt; }
    public String getDays2AsString() { return days2AsString; }
    public ArrayList<Integer> getDays2AsInt() { return days2AsInt; }
    public String getTimes1() { return times1; }
    public String getTimes2() { return times2; }
    public boolean getHasMultipleTimes() { return hasMultipleTimes; }
    
    @Override
    public String toString()
    {
        String result = days1AsString + " " + times1;
        if (this.getHasMultipleTimes())
            result+= ", " + days2AsString + " " + times2;
        return result;
    }
    
    // converts a time String into a double and into military time(ex: 9:00 am--> 9.00, 9:00 pm --> 21:00)
    public static double[] convertToMilitaryTime(String time)
    {
        
        time = time.toUpperCase();
        String[] times = time.split(" - ");
        double[] result = new double[times.length];
        for (int i = 0; i < times.length; i++)
        {
        if (times[i].contains("AM"))
        {
            if (times[i].substring(0,4).matches("\\d:\\d\\d"))
            {
            String firstpart = times[i].substring(0,1);
            String secondpart = time.substring(2,3);
            String thirdpart = time.substring(3,4);
            result[i] = Double.parseDouble(firstpart + "." + secondpart + thirdpart);
            }
            else if (times[i].substring(0,5).matches("\\d\\d:\\d\\d"))
            {
                 String firstpart = times[i].substring(0,1);
            String secondpart = time.substring(1,2);
            String thirdpart = time.substring(3,4);
            String fourthpart = time.substring(4,5);
            result[i] = Double.parseDouble(firstpart + secondpart +"." + thirdpart + fourthpart);
            
            }
        }
        else if (times[i].contains("PM"))
        {
             if (times[i].substring(0,4).matches("\\d:\\d\\d"))
            {
            String firstpart = times[i].substring(0,1);
            String secondpart = times[i].substring(2,3);
            String thirdpart = times[i].substring(3,4);
            int temp = Integer.parseInt(firstpart);
            String temp0 = temp + 12 + "."+ secondpart + thirdpart ;
            result[i] = Double.parseDouble(temp0);
            }
            else if (times[i].substring(0,5).matches("\\d\\d:\\d\\d"))
            {
            String firstpart = times[i].substring(0,1);
            String secondpart = times[i].substring(1,2);
            String thirdpart = times[i].substring(3,4);
            String fourthpart = times[i].substring(4,5);
            String temp1 = firstpart + secondpart;
            int temp = Integer.parseInt(temp1);
            String temp0 = temp + 12 + "."+ thirdpart + fourthpart ;
            result[i] = Double.parseDouble(temp0);
            
            }
        }
        }
        return result;
    }
   
    /**
    public static boolean checkTimes(String time1, String time2) // not completed
    {
        double first = convertToMilitaryTime(time1);
        double second = convertToMilitaryTime(time2);
        return false;
    }
    * 
    * *
    * */
    
    // checkDays can take two String days or two ArrayList<Integer>
    public static boolean checkDays(String day1, String day2) 
    {
        ArrayList<Integer> first = convertDaysToInt(day1);
        ArrayList<Integer> second = convertDaysToInt(day2);
        return checkDays(first,second);
    }
    
    public static boolean checkDays(ArrayList<Integer> day1, ArrayList<Integer> day2)
    {
        for (Integer a: day1)
        {
            if (day2.contains(a))
                return true;
        }
        return false;
    }
    
    public static void main(String[] args)
    {
        Schedule a = new Schedule("MW 1:30PM - 2:45PM");
        Schedule b = new Schedule("T 6:30PM - 8:00PM");
        //System.out.println(a);
        //System.out.println(b);
        String time = "9:30AM - 10:30AM";
        int firstpart = Integer.parseInt(time.substring(0,1));
            int secondpart = Integer.parseInt(time.substring(2,3));
            int thirdpart = Integer.parseInt(time.substring(3,4));
            double result = firstpart + secondpart/10.0 + thirdpart/100.0;
            String[] times = time.split(" - ");
            String one  = "9:30PM - 10:30PM";
       double[] fun = convertToMilitaryTime(one);
       for (double l: fun)
           System.out.println(l);
                
    }
}
