/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
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
     
     // conflict can also take 3 schedules
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
     
     public static boolean conflicts(ArrayList<Schedule> listOfSchedules)
     {
         Schedule first, second,  third,  fourth, fifth;
         first = listOfSchedules.get(0);
         second = listOfSchedules.get(1);
         third = listOfSchedules.get(2);
         fourth = listOfSchedules.get(3);
         fifth = listOfSchedules.get(4);
         //ArrayList<Object> result = new ArrayList<Object>();
         if (conflicts(first,second))
         {
             /**result.set(0,true);
             ArrayList<Integer> con = new ArrayList<Integer>();
             con.add(0);
             con.add(1);
             result.add(con);**/
             return true;
         }
         if (conflicts(first,third))
         {
             return true;
         }
         if (conflicts(first,fourth))
         {
             return true;
         }
         if (conflicts(first,fifth))
         {
             return true;
         }
         if (conflicts(second,third))
         {
             return true;
         }
         if (conflicts(second,fourth))
         {
             return true;
         }
         if (conflicts(second,fifth))
         {
             return true;
         }
         if (conflicts(third,fourth))
         {
             return true;
         }
         if (conflicts(third,fifth))
         {
             return true;
         }
         if (conflicts(fourth,fifth))
         {
             return true;
         }
        
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
            String secondpart = times[i].substring(2,3);
            String thirdpart = times[i].substring(3,4);
            result[i] = Double.parseDouble(firstpart + "." + secondpart + thirdpart);
            }
            else if (times[i].substring(0,5).matches("\\d\\d:\\d\\d"))
            {
                 String firstpart = times[i].substring(0,1);
            String secondpart = times[i].substring(1,2);
            String thirdpart = times[i].substring(3,4);
            String fourthpart = times[i].substring(4,5);
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
    
    public static ArrayList<HopkinsClass> matchTimes( ArrayList<HopkinsCourse> priorityCourses)
    {
        //try{
        ArrayList<HopkinsClass> schedule = new ArrayList<HopkinsClass>();
         ArrayList<Schedule> possibleSchedule = new ArrayList<Schedule>();
        boolean conflicts = true;
        int tracker = 0;
        for(int i = 0; i < 5; i++)
        {
            schedule.add(null);
            possibleSchedule.add(null);
        }
        while (conflicts)  
        {
            System.out.println(tracker);
         ArrayList<HopkinsCourse> topFive = new ArrayList<HopkinsCourse>();
         int size1, size2, size3, size4, size5;
        for (int i = tracker; i < tracker+5; i++)
        {
            if (tracker <= priorityCourses.size())
            {

           topFive.add(priorityCourses.get(i));  
           // System.out.println(priorityCourses.get(i));
            }
            else
                return null;
        }   
        size1 = topFive.get(0).HopkinsClasses.size();
        //System.out.println("Test: " + size1);
        size2 = topFive.get(1).HopkinsClasses.size();
        size3 = topFive.get(2).HopkinsClasses.size();
        size4 = topFive.get(3).HopkinsClasses.size();
        size5 = topFive.get(4).HopkinsClasses.size();
        for (int a = 1; a <= size1; a++)
        {
              for (int b = 1; b <=size2; b++)
              {
                    for (int c = 1; c <= size3; c++)
                    {
                          for (int d = 1; d <= size4; d++)
                          {
                                for (int e = 1; e <= size5; e++)
                                {
                                    if (topFive.get(0).HopkinsClasses.get(a).getSection() < 100)
                                    schedule.set(0,topFive.get(0).HopkinsClasses.get(a));
                                  //System.out.println(topFive.get(0).HopkinsClasses.get(a).getSchedule());
                                    if (topFive.get(1).HopkinsClasses.get(b).getSection() < 100)
                                    schedule.set(1,topFive.get(1).HopkinsClasses.get(b));
                                  //System.out.println(topFive.get(1).HopkinsClasses.get(a).getSchedule());
                                    if (topFive.get(2).HopkinsClasses.get(c).getSection() < 100)
                                    schedule.set(2,topFive.get(2).HopkinsClasses.get(c));
                                  //System.out.println(topFive.get(3).HopkinsClasses.get(a).getSchedule());
                                    if (topFive.get(3).HopkinsClasses.get(d).getSection() < 100)
                                    schedule.set(3,topFive.get(3).HopkinsClasses.get(d));
                                    
                                //.out.println(topFive.get(3).HopkinsClasses);
                                    if (topFive.get(4).HopkinsClasses.get(e).getSection() < 100)
                                    schedule.set(4,topFive.get(4).HopkinsClasses.get(e));
                                   
                                  // System.out.println(topFive.get(4).HopkinsClasses.get(a).getSchedule());
                                    for (int i = 0; i < 5; i++)
                                    {
                                        if (schedule.get(i) != null)
                                        possibleSchedule.set(i,schedule.get(i).getSchedule());
                                        
                                    }
                                    conflicts = conflicts(possibleSchedule);
                                     //System.out.println(conflicts);
                                    if (!conflicts)
                                        break;
                                    
                                }
        }
        }
    }
}
          
       tracker++;
        //}
            
               
              
        }
       // catch(Error e){
         //   return null;
        //}
          return schedule;
    }
    
    
    /**public static boolean matchFiveClasses(ArrayList<HopkinsCourse> topFive)
    {
       
        for (HopkinsCourse currentCourse: topFive)
        {
        
            HashMap<Integer, HopkinsClass> possibleClasses = currentCourse.HopkinsClasses;
           possibleSchedule.add(possibleClasses.get(1).getSchedule());
           possibleClassList.add(possibleClasses.get(1));
          // Iterator<HopkinsClass> iter2 = possibleClasses.values().iterator();
          //while (iter2.hasNext()) {
            //    String course2=iter2.next();}
        //for (HopkinsClass c : possibleClasses)
        }
        // conflicts = conflicts(possibleSchedule);
        return true;
    }
    public static void main(String[] args)
    {
        ArrayList<HopkinsCourse> courses = new ArrayList<HopkinsCourse>();
        
       
        
       /** HopkinsClass.Semester semester = HopkinsClass.Semester.FALL;
         HopkinsCourse first = new HopkinsCourse("020.103","Freshman Seminar: The Human Microbiome","N",false,2,semester,2015); 
         HopkinsCourse second = new HopkinsCourse("300.102","Chem","Q",false,4,semester,2015); 
         HopkinsCourse third = new HopkinsCourse("300.103","Chem","Q",false,4,semester,2015); 
         HopkinsCourse fourth = new HopkinsCourse("300.104","Chem","Q",false,4,semester,2015); 
         HopkinsCourse fifth = new HopkinsCourse("300.105","Chem","Q",false,4,semester,2015); 
         HopkinsCourse sixth = new HopkinsCourse("300.106","Chem","Q",false,4,semester,2015); 
         courses.add(first);
          courses.add(second);
           courses.add(third);
            courses.add(fourth);
             courses.add(fifth);
              courses.add(sixth);
              matchTimes(courses);
        Schedule a = new Schedule("TTh 11:00AM - 12:00PM");
        Schedule b = new Schedule("TTh 12:15PM - 2:00PM");
        Schedule c = new Schedule("TTh 3:00PM - 4:00PM");
        Schedule d = new Schedule("M 1:30PM - 4:20PM");
        Schedule e = new Schedule("W 1:30PM - 2:20PM");
       ArrayList<Schedule> schedule = new ArrayList<Schedule>();
       schedule.add(a);
       schedule.add(b);
       schedule.add(c);
       schedule.add(d);
       schedule.add(e);
        **/
       
          
           

    }

