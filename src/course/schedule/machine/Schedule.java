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
import course.schedule.machine.HopkinsClass.Semester;
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
         for (int i = 0; i < listOfSchedules.size(); i++)
         {
             for (int j = i+1; j < listOfSchedules.size(); j++)
             {
                 
                 if (conflicts(listOfSchedules.get(i),listOfSchedules.get(j)))
                     return true;
             }
         }
         return false;
        /** Schedule first, second,  third,  fourth, fifth;
         first = listOfSchedules.get(0);
         second = listOfSchedules.get(1);
         third = listOfSchedules.get(2);
         fourth = listOfSchedules.get(3);
         fifth = listOfSchedules.get(4);
         //ArrayList<Object> result = new ArrayList<Object>();
         if (conflicts(first,second))
         {
             result.set(0,true);
             ArrayList<Integer> con = new ArrayList<Integer>();
             con.add(0);
             con.add(1);
             result.add(con);
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
         **/
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
    
    // returns true if the course has a class in the right semester, else returns false
    public static boolean hasSemester(HopkinsCourse course, Semester semester)
    {
        HashMap<Integer, HopkinsClass> a = course.HopkinsClasses;
        for (Iterator<HopkinsClass> it1 = a.values().iterator(); it1.hasNext();)
        {
            if (it1.next().getSemester().equals(semester))
                return true;
        }
        return false;
            
    }
    
    // returns an array that finds the combination of courses that are wihin 1.5 credits of desired credit amount
    public static ArrayList<HopkinsCourse> desiredCreditChecker(ArrayList<HopkinsCourse> priorityCourse, ArrayList<HopkinsCourse> course, double desiredCredits, int tracker, Semester currentSemester)
    {
        if (hasSemester(priorityCourse.get(tracker),currentSemester))
            course.add(priorityCourse.get(tracker));
        double sumOfCredits = 0;
        for (HopkinsCourse a : course)
            sumOfCredits += a.getCredits();
        if (Math.abs(sumOfCredits - desiredCredits) <= 1.5 )
        {
            return course;
        }
        if (sumOfCredits < desiredCredits)
        {
            tracker++;
            return desiredCreditChecker(priorityCourse,course,desiredCredits,tracker, currentSemester);
        }
        if (sumOfCredits > desiredCredits )
            {
                
                course.remove(course.size()-1);
                tracker++;
            return desiredCreditChecker(priorityCourse,course,desiredCredits,tracker, currentSemester);
            }
        return null;
    }        
    
    
    public static HashMap<Integer,HopkinsClass> removeWrongSemesters(HashMap<Integer,HopkinsClass> classes, Semester semester)
    {
         HashMap<Integer,HopkinsClass> b = new HashMap<Integer,HopkinsClass>();
        for (Iterator<HopkinsClass> it = classes.values().iterator(); it.hasNext();)
        {
            HopkinsClass a = it.next();
            if (a.getSemester().equals(semester))
            {
                b.put(a.getSection(),a);
            }
            else if(!(a.getSemester().equals(semester)))
            {
                classes.remove(a);
            }
        }
        //System.out.println(b);
        return b;
    }
    
    /**public static ArrayList<HopkinsClass> matchTimesRecursively(ArrayList<HopkinsCourse> topCourses, ArrayList<HopkinsClass> result, Semester currentSemester, int tracker, int size, int times)
    {
        //System.out.println(result);
        HopkinsCourse currentCourse = topCourses.get(tracker);
        removeWrongSemesters(currentCourse.HopkinsClasses, currentSemester);
        Iterator<HopkinsClass> it = currentCourse.HopkinsClasses.values().iterator();
        HopkinsClass current = null;
        for (int i = 0; i < times; i++)
        {
            current = it.next();
            System.out.println(current);
        }
        result.add(current);
        boolean test = classConflicts(result);
        if (!classConflicts(result))
        {
            tracker++;
        }
        else
        {
            //System.out.println(currentCourse.HopkinsClasses);
            //currentCourse.HopkinsClasses.remove(current);
            //System.out.println(currentCourse.HopkinsClasses);
           // System.out.println();
            times++;
        }
        if (result.size() < size)
            return matchTimesRecursively(topCourses, result, currentSemester,tracker, size,1);
        if (result.size() == size)
            return result;
        
        return null;
    }**/
    
    
    public static ArrayList<HopkinsClass> matchTimesLoop9 (ArrayList<HopkinsCourse> topFive)
    {
         ArrayList<HopkinsClass> schedule = new ArrayList<HopkinsClass>();
         ArrayList<Schedule> possibleSchedule = new ArrayList<Schedule>();
       for(int i = 0; i < 9; i++)
        {
            schedule.add(null);
            possibleSchedule.add(null);
        }
       myLoop:
       for (Iterator<HopkinsClass> it1 = topFive.get(0).HopkinsClasses.values().iterator(); it1.hasNext();)
        {
            
            HopkinsClass first = it1.next();
              for (Iterator<HopkinsClass> it2 = topFive.get(1).HopkinsClasses.values().iterator(); it2.hasNext();)
              {
                  HopkinsClass second = it2.next();
                    for (Iterator<HopkinsClass> it3 = topFive.get(2).HopkinsClasses.values().iterator(); it3.hasNext();)
                    {
                        HopkinsClass third = it3.next();
                          for (Iterator<HopkinsClass> it4 = topFive.get(3).HopkinsClasses.values().iterator(); it4.hasNext();)
                          {
                              HopkinsClass fourth = it4.next();
                                for (Iterator<HopkinsClass> it5 = topFive.get(4).HopkinsClasses.values().iterator(); it5.hasNext();)
                                {
                                    HopkinsClass fifth = it5.next();
                                        for (Iterator<HopkinsClass> it6 = topFive.get(5).HopkinsClasses.values().iterator(); it6.hasNext();) 
                                        {
                                            HopkinsClass sixth = it6.next();
                                            for (Iterator<HopkinsClass> it7 = topFive.get(6).HopkinsClasses.values().iterator(); it7.hasNext();) 
                                            {
                                                HopkinsClass seventh= it7.next();
                                                for (Iterator<HopkinsClass> it8 = topFive.get(7).HopkinsClasses.values().iterator(); it8.hasNext();)
                                                {
                                                    HopkinsClass eigth = it8.next();
                                                    for (Iterator<HopkinsClass> it9 = topFive.get(8).HopkinsClasses.values().iterator(); it9.hasNext();)
                                                        {
                                                        HopkinsClass ninth = it9.next();
                                    
                                    
                                    
                                    
                                    Schedule one, two, three, four, five,six,seven,eight,nine;
                                    possibleSchedule.set(0,first.getSchedule());
                                    possibleSchedule.set(1,second.getSchedule());
                                    possibleSchedule.set(2,third.getSchedule());
                                    possibleSchedule.set(3,fourth.getSchedule());
                                    possibleSchedule.set(4,fifth.getSchedule());
                                    possibleSchedule.set(5,sixth.getSchedule());
                                    possibleSchedule.set(6,seventh.getSchedule());
                                    possibleSchedule.set(7,eigth.getSchedule());
                                    possibleSchedule.set(8,ninth.getSchedule());
                                    schedule.set(0,first);
                                    schedule.set(1,second);
                                    schedule.set(2,third);
                                    schedule.set(3,fourth);
                                    schedule.set(4,fifth);
                                    schedule.set(5,sixth);
                                    schedule.set(6,seventh);
                                    schedule.set(7,eigth);
                                    schedule.set(8,ninth);
                                    
                                  if (!conflicts(possibleSchedule))
                                    {
                                        break myLoop;
                                    }
                                    
                                 
                                    
                                    
                                }
        }
        }
    }
}
                    }
              }
        }
        }
       return schedule;
    }
    
    
    public static ArrayList<HopkinsClass> matchTimesLoop8 (ArrayList<HopkinsCourse> topFive)
    {
         ArrayList<HopkinsClass> schedule = new ArrayList<HopkinsClass>();
         ArrayList<Schedule> possibleSchedule = new ArrayList<Schedule>();
       for(int i = 0; i < 8; i++)
        {
            schedule.add(null);
            possibleSchedule.add(null);
        }
       for (Iterator<HopkinsClass> it1 = topFive.get(0).HopkinsClasses.values().iterator(); it1.hasNext();)
        {
            
            HopkinsClass first = it1.next();
              for (Iterator<HopkinsClass> it2 = topFive.get(1).HopkinsClasses.values().iterator(); it2.hasNext();)
              {
                  HopkinsClass second = it2.next();
                    for (Iterator<HopkinsClass> it3 = topFive.get(2).HopkinsClasses.values().iterator(); it3.hasNext();)
                    {
                        HopkinsClass third = it3.next();
                          for (Iterator<HopkinsClass> it4 = topFive.get(3).HopkinsClasses.values().iterator(); it4.hasNext();)
                          {
                              HopkinsClass fourth = it4.next();
                                for (Iterator<HopkinsClass> it5 = topFive.get(4).HopkinsClasses.values().iterator(); it5.hasNext();)
                                {
                                    HopkinsClass fifth = it5.next();
                                        for (Iterator<HopkinsClass> it6 = topFive.get(5).HopkinsClasses.values().iterator(); it6.hasNext();) 
                                        {
                                            HopkinsClass sixth = it6.next();
                                            for (Iterator<HopkinsClass> it7 = topFive.get(6).HopkinsClasses.values().iterator(); it7.hasNext();) 
                                            {
                                                HopkinsClass seventh= it7.next();
                                                for (Iterator<HopkinsClass> it8 = topFive.get(7).HopkinsClasses.values().iterator(); it8.hasNext();)
                                                {
                                                    HopkinsClass eigth = it8.next();
                                                   
                                    
                                    
                                    
                                    
                                    Schedule one, two, three, four, five,six,seven,eight;
                                    possibleSchedule.set(0,first.getSchedule());
                                    possibleSchedule.set(1,second.getSchedule());
                                    possibleSchedule.set(2,third.getSchedule());
                                    possibleSchedule.set(3,fourth.getSchedule());
                                    possibleSchedule.set(4,fifth.getSchedule());
                                    possibleSchedule.set(5,sixth.getSchedule());
                                    possibleSchedule.set(6,seventh.getSchedule());
                                    possibleSchedule.set(7,eigth.getSchedule());
                                    schedule.set(0,first);
                                    schedule.set(1,second);
                                    schedule.set(2,third);
                                    schedule.set(3,fourth);
                                    schedule.set(4,fifth);
                                    schedule.set(5,sixth);
                                    schedule.set(6,seventh);
                                    schedule.set(7,eigth);
                                    
                                    if (conflicts(possibleSchedule))
                                            {
                                                
                                            }
                                    else
                                    {
                                        return schedule;
                                    }
                                    
                                 
                                    
                                    
                                }
        }
        }
    }
}
                    
              }
        }
        }
       return null;
    }
    
    
     public static ArrayList<HopkinsClass> matchTimesLoop7 (ArrayList<HopkinsCourse> topFive)
    {
         ArrayList<HopkinsClass> schedule = new ArrayList<HopkinsClass>();
         ArrayList<Schedule> possibleSchedule = new ArrayList<Schedule>();
       for(int i = 0; i < 7; i++)
        {
            schedule.add(null);
            possibleSchedule.add(null);
        }
       myLoop:
       for (Iterator<HopkinsClass> it1 = topFive.get(0).HopkinsClasses.values().iterator(); it1.hasNext();)
        {
            HopkinsClass first = it1.next();
              for (Iterator<HopkinsClass> it2 = topFive.get(1).HopkinsClasses.values().iterator(); it2.hasNext();)
              {
                  HopkinsClass second = it2.next();
                    for (Iterator<HopkinsClass> it3 = topFive.get(2).HopkinsClasses.values().iterator(); it3.hasNext();)
                    {
                        HopkinsClass third = it3.next();
                          for (Iterator<HopkinsClass> it4 = topFive.get(3).HopkinsClasses.values().iterator(); it4.hasNext();)
                          {
                              HopkinsClass fourth = it4.next();
                                for (Iterator<HopkinsClass> it5 = topFive.get(4).HopkinsClasses.values().iterator(); it5.hasNext();)
                                {
                                    HopkinsClass fifth = it5.next();
                                        for (Iterator<HopkinsClass> it6 = topFive.get(5).HopkinsClasses.values().iterator(); it6.hasNext();) 
                                        {
                                            HopkinsClass sixth = it6.next();
                                            for (Iterator<HopkinsClass> it7 = topFive.get(6).HopkinsClasses.values().iterator(); it7.hasNext();) 
                                            {
                                                HopkinsClass seventh= it7.next();
                                   
                                    Schedule one, two, three, four, five,six,seven;
                                    possibleSchedule.set(0,first.getSchedule());
                                    possibleSchedule.set(1,second.getSchedule());
                                    possibleSchedule.set(2,third.getSchedule());
                                    possibleSchedule.set(3,fourth.getSchedule());
                                    possibleSchedule.set(4,fifth.getSchedule());
                                    possibleSchedule.set(5,sixth.getSchedule());
                                    possibleSchedule.set(6,seventh.getSchedule());
                                    //System.out.println(possibleSchedule);
                                    //System.out.println("************************");
                                    schedule.set(0,first);
                                    schedule.set(1,second);
                                    schedule.set(2,third);
                                    schedule.set(3,fourth);
                                    schedule.set(4,fifth);
                                    schedule.set(5,sixth);
                                    schedule.set(6,seventh);
                                    
                                    if (!conflicts(possibleSchedule))
                                            {
                                        break myLoop;
                                    }
                                    
                                 
                                    
                                    
                                }
        }
        }
    }
}     
        }
        }
       return schedule;
    }
    
     
     public static ArrayList<HopkinsClass> matchTimesLoop6 (ArrayList<HopkinsCourse> topFive)
    {
         ArrayList<HopkinsClass> schedule = new ArrayList<HopkinsClass>();
         ArrayList<Schedule> possibleSchedule = new ArrayList<Schedule>();
       for(int i = 0; i < 6; i++)
        {
            schedule.add(null);
            possibleSchedule.add(null);
        }
       for (Iterator<HopkinsClass> it1 = topFive.get(0).HopkinsClasses.values().iterator(); it1.hasNext();)
        {
            
            HopkinsClass first = it1.next();
              for (Iterator<HopkinsClass> it2 = topFive.get(1).HopkinsClasses.values().iterator(); it2.hasNext();)
              {
                  HopkinsClass second = it2.next();
                    for (Iterator<HopkinsClass> it3 = topFive.get(2).HopkinsClasses.values().iterator(); it3.hasNext();)
                    {
                        HopkinsClass third = it3.next();
                          for (Iterator<HopkinsClass> it4 = topFive.get(3).HopkinsClasses.values().iterator(); it4.hasNext();)
                          {
                              HopkinsClass fourth = it4.next();
                                for (Iterator<HopkinsClass> it5 = topFive.get(4).HopkinsClasses.values().iterator(); it5.hasNext();)
                                {
                                    HopkinsClass fifth = it5.next();
                                        for (Iterator<HopkinsClass> it6 = topFive.get(5).HopkinsClasses.values().iterator(); it6.hasNext();) 
                                        {
                                            HopkinsClass sixth = it6.next();
                                            
                                             
                                    Schedule one, two, three, four, five,six;
                                    possibleSchedule.set(0,first.getSchedule());
                                    possibleSchedule.set(1,second.getSchedule());
                                    possibleSchedule.set(2,third.getSchedule());
                                    possibleSchedule.set(3,fourth.getSchedule());
                                    possibleSchedule.set(4,fifth.getSchedule());
                                    possibleSchedule.set(5,sixth.getSchedule());
                                    schedule.set(0,first);
                                    schedule.set(1,second);
                                    schedule.set(2,third);
                                    schedule.set(3,fourth);
                                    schedule.set(4,fifth);
                                    schedule.set(5,sixth);
                                    
                                    if (conflicts(possibleSchedule))
                                            {
                                                
                                            }
                                    else
                                    {
                                        return schedule;
                                    }
                                    
                                 
                                    
                                    
                                }
        }
        }
    }
}
        }
       return null;
    }
     
     public static ArrayList<HopkinsClass> matchTimesLoop5 (ArrayList<HopkinsCourse> topFive)
    {
         ArrayList<HopkinsClass> schedule = new ArrayList<HopkinsClass>();
         ArrayList<Schedule> possibleSchedule = new ArrayList<Schedule>();
       for(int i = 0; i < 5; i++)
        {
            schedule.add(null);
            possibleSchedule.add(null);
        }
       for (Iterator<HopkinsClass> it1 = topFive.get(0).HopkinsClasses.values().iterator(); it1.hasNext();)
        {
            
            HopkinsClass first = it1.next();
              for (Iterator<HopkinsClass> it2 = topFive.get(1).HopkinsClasses.values().iterator(); it2.hasNext();)
              {
                  HopkinsClass second = it2.next();
                    for (Iterator<HopkinsClass> it3 = topFive.get(2).HopkinsClasses.values().iterator(); it3.hasNext();)
                    {
                        HopkinsClass third = it3.next();
                          for (Iterator<HopkinsClass> it4 = topFive.get(3).HopkinsClasses.values().iterator(); it4.hasNext();)
                          {
                              HopkinsClass fourth = it4.next();
                                for (Iterator<HopkinsClass> it5 = topFive.get(4).HopkinsClasses.values().iterator(); it5.hasNext();)
                                {
                                    HopkinsClass fifth = it5.next();
                                       
                                    Schedule one, two, three, four, five;
                                    possibleSchedule.set(0,first.getSchedule());
                                    possibleSchedule.set(1,second.getSchedule());
                                    possibleSchedule.set(2,third.getSchedule());
                                    possibleSchedule.set(3,fourth.getSchedule());
                                    possibleSchedule.set(4,fifth.getSchedule());
                                    schedule.set(0,first);
                                    schedule.set(1,second);
                                    schedule.set(2,third);
                                    schedule.set(3,fourth);
                                    schedule.set(4,fifth);
                                    
                                    if (conflicts(possibleSchedule))
                                            {
                                                
                                            }
                                    else
                                    {
                                        return schedule;
                                    }
                                    
                                }
        }
        }
    }
}
       return null;
    }
     
    public static ArrayList<HopkinsClass> matchTimesLoop4 (ArrayList<HopkinsCourse> topFive)
    {
         ArrayList<HopkinsClass> schedule = new ArrayList<HopkinsClass>();
         ArrayList<Schedule> possibleSchedule = new ArrayList<Schedule>();
       for(int i = 0; i < 4; i++)
        {
            schedule.add(null);
            possibleSchedule.add(null);
        }
       for (Iterator<HopkinsClass> it1 = topFive.get(0).HopkinsClasses.values().iterator(); it1.hasNext();)
        {
            
            HopkinsClass first = it1.next();
              for (Iterator<HopkinsClass> it2 = topFive.get(1).HopkinsClasses.values().iterator(); it2.hasNext();)
              {
                  HopkinsClass second = it2.next();
                    for (Iterator<HopkinsClass> it3 = topFive.get(2).HopkinsClasses.values().iterator(); it3.hasNext();)
                    {
                        HopkinsClass third = it3.next();
                          for (Iterator<HopkinsClass> it4 = topFive.get(3).HopkinsClasses.values().iterator(); it4.hasNext();)
                          {
                              HopkinsClass fourth = it4.next();
                                    
                                    Schedule one, two, three, four;
                                    possibleSchedule.set(0,first.getSchedule());
                                    possibleSchedule.set(1,second.getSchedule());
                                    possibleSchedule.set(2,third.getSchedule());
                                    possibleSchedule.set(3,fourth.getSchedule());
                                    schedule.set(0,first);
                                    schedule.set(1,second);
                                    schedule.set(2,third);
                                    schedule.set(3,fourth);
                                    
                                    if (conflicts(possibleSchedule))
                                            {
                                                
                                            }
                                    else
                                    {
                                        return schedule;
                                    }
                                     
                                }
        }
        }
    }
       return null;
    }
    
     public static ArrayList<HopkinsClass> matchTimesLoop3 (ArrayList<HopkinsCourse> topFive)
    {
         ArrayList<HopkinsClass> schedule = new ArrayList<HopkinsClass>();
         ArrayList<Schedule> possibleSchedule = new ArrayList<Schedule>();
       for(int i = 0; i < 3; i++)
        {
            schedule.add(null);
            possibleSchedule.add(null);
        }
       for (Iterator<HopkinsClass> it1 = topFive.get(0).HopkinsClasses.values().iterator(); it1.hasNext();)
        {
            
            HopkinsClass first = it1.next();
              for (Iterator<HopkinsClass> it2 = topFive.get(1).HopkinsClasses.values().iterator(); it2.hasNext();)
              {
                  HopkinsClass second = it2.next();
                    for (Iterator<HopkinsClass> it3 = topFive.get(2).HopkinsClasses.values().iterator(); it3.hasNext();)
                    {
                        HopkinsClass third = it3.next();
                         
                                    
                                    Schedule one, two, three;
                                    possibleSchedule.set(0,first.getSchedule());
                                    possibleSchedule.set(1,second.getSchedule());
                                    possibleSchedule.set(2,third.getSchedule());
                                    schedule.set(0,first);
                                    schedule.set(1,second);
                                    schedule.set(2,third);
                                    
                                    if (conflicts(possibleSchedule))
                                            {
                                               
                                            }
                                    else
                                    {
                                        return schedule;
                                    }
                                     
                                }
        }
        }
       return null;
    }
     
  
     
    public static ArrayList<HopkinsClass> matchTimes( ArrayList<HopkinsCourse> priorityCourses, double desiredCredits, Semester currentSemester)
    {
       
        ArrayList<HopkinsCourse> course = new ArrayList<HopkinsCourse>();
      // int size = desiredCreditChecker(priorityCourses,course, desiredCredits,tracker, currentSemester).size();
        boolean conflicts = true;
        int tracker = 0;
         ArrayList<HopkinsClass> schedule = new ArrayList<HopkinsClass>();
         myLoop:
         while (conflicts)  
        {
           
            ArrayList<HopkinsCourse> topFive = desiredCreditChecker(priorityCourses,course, desiredCredits,tracker, currentSemester);
            int size = topFive.size();
            for (int i = 0; i < size; i++)
            {
                //System.out.println(topFive.get(i).HopkinsClasses);
                //HashMap<Integer, HopkinsClass> a = removeWrongSemesters(topFive.get(i).HopkinsClasses, currentSemester);
                topFive.get(i).HopkinsClasses = removeWrongSemesters(topFive.get(i).HopkinsClasses, currentSemester);
                 //System.out.println(topFive.get(i).HopkinsClasses);
                  //System.out.println();
            }
            if (size == 9)
                schedule = matchTimesLoop9(topFive);
            else if (size == 8)
                schedule = matchTimesLoop8(topFive);
            else if (size == 7)
            {
               //System.out.println(size == 7);
              schedule = matchTimesLoop7(topFive);
              //System.out.println(matchTimesLoop7(topFive));
              
            }
            else if (size == 6)
                schedule = matchTimesLoop6(topFive);
            else if (size == 5)
                schedule = matchTimesLoop5(topFive);
            else if (size == 4)
                schedule = matchTimesLoop4(topFive);
            else if (size == 3)
                schedule = matchTimesLoop3(topFive);
           
            tracker++;
            boolean containsNull = false;
                                AnotherLoop:
                                for (HopkinsClass a: schedule)
                                {
                                    if (a == null)
                                    {
                                        containsNull = true;
                                        break AnotherLoop;
                                    }
                                }
                                if (!(containsNull))
                                        break myLoop;
        }
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
    }**/
                                  
    public static void main(String[] args)
    {
        
        Schedule a = new Schedule("TTh 11:00AM - 12:00PM");
        Schedule b = new Schedule("TTh 12:15PM - 2:00PM");
        Schedule c = new Schedule("TTh 3:00PM - 4:20PM");
        Schedule d = new Schedule("Tth 4:30PM - 5:20PM");
        Schedule e = new Schedule("Tth 8:30PM - 10:20PM");
        ArrayList<Schedule> schedule = new ArrayList<Schedule>();
        schedule.add(a);
        schedule.add(b);
        schedule.add(c);
        schedule.add(d);
        schedule.add(e);
        System.out.println(conflicts(schedule));
    }
       
          
           

    }

