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
                
                if (classTimes.trim().length()==0)
                {
                    
                }
                else
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
    }
    
    // returns true if there are conflicts
     public static boolean conflicts(Schedule first, Schedule second) 
    {
         boolean result = false;
        if (first.getHasMultipleTimes() && second.getHasMultipleTimes())
        {
            if (checkDays(first.getDays1AsString(),second.getDays1AsString()))
            {
                result =  checkTimes(first.getTimes1(), second.getTimes1());
                if (result)
                    return true;
            }
            if (checkDays(first.getDays1AsString(),second.getDays2AsString()))
            {
                result = checkTimes(first.getTimes1(), second.getTimes2());
                if (result)
                    return true;
            }
            if (checkDays(first.getDays2AsString(),second.getDays1AsString()))
            {
                result = checkTimes(first.getTimes2(), second.getTimes1());
                if (result)
                    return true;
            }
            if (checkDays(first.getDays2AsString(),second.getDays2AsString()))
            {
                result = checkTimes(first.getTimes2(), second.getTimes2());
                if (result)
                    return true;
            }
        }
        else if (first.getHasMultipleTimes())
        {
            if (checkDays(first.getDays1AsString(),second.getDays1AsString()))
            {
                result =  checkTimes(first.getTimes1(), second.getTimes1());
                if (result)
                    return true;
            }
            
             if (checkDays(first.getDays2AsString(),second.getDays1AsString()))
            {
                result = checkTimes(first.getTimes2(), second.getTimes1());
                if (result)
                    return true;
            }
        }
        else if (second.getHasMultipleTimes())
        {
             if (checkDays(first.getDays1AsString(),second.getDays1AsString()))
            {
                result =  checkTimes(first.getTimes1(), second.getTimes1());
                if (result)
                    return true;
            }
            
             if (checkDays(first.getDays1AsString(),second.getDays2AsString()))
            {
                result = checkTimes(first.getTimes1(), second.getTimes2());
                if (result)
                    return true;
            }
        }
        else
        {
            if (checkDays(first.getDays1AsString(),second.getDays1AsString()))
            {
                result =  checkTimes(first.getTimes1(), second.getTimes1());
                if (result)
                    return true;
            }
        }
        return result;
    }
     
     // conflict can also take 3 schedule
     public static boolean conflicts(Schedule first, Schedule second, Schedule third)
     {
         if (conflicts(first,second))
             return true;
         if (conflicts(second, third))
             return true;
         if (conflicts(first,third))
             return true;
         return false;
     }
     
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
            if (temp == 12)
                result[i] = Double.parseDouble(firstpart + secondpart +"." + thirdpart + fourthpart);
            else
            {
            String temp0 = temp + 12 + "."+ thirdpart + fourthpart ;
            result[i] = Double.parseDouble(temp0);
            }
            }
        }
        }
        return result;
    }
   
    
    public static boolean checkTimes(String time1, String time2)
    {
        double[] first = convertToMilitaryTime(time1);
        double[] second = convertToMilitaryTime(time2);
        if (first[0] <= second[1] && first[0] >= second[0]) 
            return true;
        if (second[0] <= first[1] && second[0] >= first[0])
                return true;
        return false;
    }
     
    
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
        Schedule a = new Schedule("TTh 11:00AM - 12:00PM");
        Schedule b = new Schedule("TTh 12:00PM - 2:00PM");
        Schedule c = new Schedule("TTh 3:00PM - 4:00PM");
        String time1 = "2:00PM - 4:00PM";
        String time2 = "1:30PM - 2:45PM";
       
          System.out.println(conflicts(a,b,c));
           
       
    }
}
