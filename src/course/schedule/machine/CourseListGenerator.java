/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import scheduleGenerator.DbFiles.ManageTxtFiles;

/**
 *
 * @author ryerrabelli
 */
public class CourseListGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    public static ArrayList<HopkinsCourse> generateBestSchedule(Set<HopkinsCourse> coursesTaken, String...categories) {
        if (categories == null || categories.length ==0) return new ArrayList();
        try {
            //Option 1
            RequiredCourseSet reqs = ManageTxtFiles.getRequiredCourses(categories[0]);
            for (int i = 1; i < categories.length; i++) {
                reqs.addAll(ManageTxtFiles.getRequiredCourses(categories[i]));
            }
            
            HashSet<HashSet<Course>> allPossibilities = getAllCoursePossibilities(new HashSet(), reqs);
            
           TreeSet<HashSet<Course>> subTakenPossibilities = new TreeSet<>(new Comparator() {
           //   Note: this comparator imposes orderings that are inconsistent with equals.
                @Override
                public int compare(Object o1, Object o2) {
                    HashSet<Course> set1 = (HashSet<Course>) o1;
                    HashSet<Course> set2 = (HashSet<Course>) o2;
                    float set1Num = 0;
                    for (Course crs1 : set1) {
                        try {
                            int toAdd = Integer.parseInt(crs1.courseNum);
                            if (toAdd > 0) { while (toAdd < 100) toAdd *= 10; }
                            if (crs1.credits > 0.2) toAdd *= crs1.credits;
                            set1Num += toAdd * crs1.credits;
                        } catch (NumberFormatException nfe) {
                            
                        }
                    }
                    float set2Num = 0;
                    for (Course crs2 : set2) {
                        try {
                            int toAdd = Integer.parseInt(crs2.courseNum);
                            if (toAdd > 0) { while (toAdd < 100) toAdd *= 10; }
                            if (crs2.credits > 0.2) toAdd *= crs2.credits; 
                            set2Num += toAdd;
                        } catch (NumberFormatException nfe) {
                            
                        }
                    }
                    int sign = (int) Math.signum(set1Num - set2Num);
                    if (sign != 0) return sign;
                    //int sizeDif = ((Collection) o1).size() - ((Collection) o2).size();
                    //if (sizeDif != 0) return sizeDif;
                    return o1.hashCode() - o2.hashCode();
                }
            });
            //HashSet<HashSet<Course>> subTakenPossibilities = new HashSet<>(); 
            subtractCompletedAndCantTakeCourses:
            for (Iterator<HashSet<Course>> possibIt = allPossibilities.iterator(); possibIt.hasNext();) {
                HashSet<Course> possibility = possibIt.next();
                HashSet<Course> subTakenPossib = new HashSet<>();
                for (Iterator<Course> it = possibility.iterator(); it.hasNext();) {
                    Course next = it.next();
                    if (!next.isFulfilled(coursesTaken)) {
                        if (next instanceof HopkinsCourse) {
                            if (((HopkinsCourse) next).canTake(coursesTaken)) subTakenPossib.add(next);
                        } else subTakenPossib.add(next);
                    }
                }
               // subTakenPossibilities.first().equals(subTakenPossib);
                subTakenPossibilities.add(subTakenPossib);
            }
            return getPriorities(subTakenPossibilities.first());
        /*    //Option 2
            RequiredCourseSet leastReqs = ManageTxtFiles.getRequiredCourses(categories[0]);
            for (int i = 1; i < categories.length; i++) {
                leastReqs = generateLeastRequirements(new HashSet(), leastReqs, ManageTxtFiles.getRequiredCourses(categories[i]));
            }
            
            getRemainingRequirements:
            for (Iterator<Requirable> it = leastReqs.iterator(); it.hasNext();) {
                Requirable next = it.next();
                if (next.isFulfilled(coursesTaken)) {
                    it.remove();
                }
            }
            
            ArrayList<HopkinsCourse> prioritizedCourses = getPriorities(leastReqs);
            return prioritizedCourses; */
        } catch (IOException ex) {
            System.out.println("Categories in generateBestSchedule could not be found: " + categories);
            return null;
        }
    }
    
    /*public static HashSet<Course> getSetOfCourses(RequiredCourseSet reqs) {
        HashSet<Course> courses = new HashSet<>();
        for (Requirable req : reqs) {
            if (req instanceof Course) courses.add((Course) req);
            else if (req instanceof RequiredCourseSet) {
                courses.addAll(getSetOfCourses((RequiredCourseSet) req));
            }
        }
        return courses;
    } */
    
    /*public static RequiredCourseSet optimizeAdd(RequiredCourseSet[] categories) {
        if (categories.length == 0) return new RequiredCourseSet(0);
        RequiredCourseSet toReturn = categories[0];
        for (RequiredCourseSet topMost : categories) {
            if (topMost.getTrueNumRequired() != topMost.size()) continue;
            for (Requirable inner : topMost) {
                if (inner instanceof GenericCourse) {
                    for (Requirable toReturnReq : toReturn) {
                        if (toReturn Req instanceof )
                    }
                }   
            }
        }
    }*/
    
    public static HashSet<HashSet<Course>> getAllCoursePossibilities(final HashSet<Course> N_alreadyPossibility, RequiredCourseSet category1) {
        HashSet<HashSet<Course>> allPossibilities = new HashSet<>();
        HashSet<Course> alreadyPossibility = (HashSet<Course>) N_alreadyPossibility.clone();
        //RequiredCourseSet category1 = new RequiredCourseSet(0);
        //for (RequiredCourseSet cat : categories) category1.addAll(cat);
        allPossibilities.add(alreadyPossibility);
        if (category1.getNumRequired() == 0) {
            for (Iterator<Requirable> cat1It = category1.iterator(); cat1It.hasNext();) {
                Requirable req1 = cat1It.next();
                category1.getNumRequired();
       //         if (req1 instanceof Course && ((Course) req1).deptNum.equals("110"))
       //             System.out.println("Here");
                
                if (req1 instanceof Course) 
                    alreadyPossibility.add((Course) req1);
                else if (req1 instanceof RequiredCourseSet) {
                    //testing
                    
                    //continue;
                }
            }
            for (Iterator<Requirable> cat1It = category1.iterator(); cat1It.hasNext();) {
                Requirable req1 = cat1It.next();
          //                      if (req1 instanceof Course && ((Course) req1).deptNum.equals("110"))
             //       System.out.println("Here");
                if (req1 instanceof Course) continue;
                else if (req1 instanceof RequiredCourseSet) {
                    if (((RequiredCourseSet) req1).getNumRequired() == 1 || ((RequiredCourseSet) req1).getNumRequired() == 0) {
                     //   for (Iterator<Requirable> optIt = ((RequiredCourseSet) req1).iterator(); optIt.hasNext();) {
                        HashSet<HashSet<Course>> newPossibilities = new HashSet<>();
                        for (Iterator<HashSet<Course>> allPossibIt = allPossibilities.iterator(); allPossibIt.hasNext();) {
                                HashSet<Course> inSetPossib = allPossibIt.next();
                                HashSet<HashSet<Course>> innerAllPossibilities = getAllCoursePossibilities(alreadyPossibility, (RequiredCourseSet) req1);
                                for(HashSet<Course> hsc : innerAllPossibilities) {
                                    HashSet<Course> newVersion = new HashSet<Course>(inSetPossib);
                                     newVersion.addAll(hsc);
                                    newPossibilities.add(newVersion);
                                }
                                //System.out.println("before: " + allPossibilities);
                                //allPossibIt.remove();
                                //toRemove.add(inSetPossib);
                                //System.out.println("after: " +allPossibilities);
                        }

                        /*for (HashSet<Course> hsc : toRemove) {
                            for (Iterator it = allPossibilities.iterator();it.hasNext();) {
                                if (it.next().equals(hsc)) {
                                    System.out.println("Yes it equals");
                                    it.remove();
                                }
                            }
                        } */
                        allPossibilities = newPossibilities;
                       //     Requirable option = optIt.next();
                         //       HashSet<Course> onePossib = new HashSet<Course>(alreadyPossibility);
                           //     onePossib.addAll(option);
                             //   allPossibilities.add(onePossib);
                    }/* else if ( ((RequiredCourseSet) req1).getNumRequired() == 0) {
                            HashSet<HashSet<Course>> newPossibilities = new HashSet<>();
                            for (Iterator<HashSet<Course>> allPossibIt = allPossibilities.iterator(); allPossibIt.hasNext();) {
                                HashSet<Course> inSetPossib = allPossibIt.next();
                                HashSet<HashSet<Course>> innerAllPossibilities = getAllCoursePossibilities(alreadyPossibility, (RequiredCourseSet) req1);
                                newPossib.add(null);
                                for(HashSet<Course> hsc : innerAllPossibilities) {
                                    HashSet<Course> newVersion = new HashSet<Course>(inSetPossib);
                                     newVersion.addAll(hsc);
                                    newPossibilities.add(newVersion);
                                }
                            }
                        }*/
                   // }
                }
            }
        } else if (category1.getNumRequired() == 1) {
            HashSet<HashSet<Course>> newPossibilities = new HashSet<>();
            //allPossibilities.remove(alreadyPossibility);
            for (Iterator<Requirable> cat1It = category1.iterator(); cat1It.hasNext();) {
                Requirable req1 = cat1It.next();
             //                   if (req1 instanceof Course && ((Course) req1).deptNum.equals("110"))
               //     System.out.println("Here");
                if (req1 instanceof Course) {
                    HashSet<Course> newPossib = new HashSet<>(alreadyPossibility);
                    newPossib.add((Course) req1);
                    newPossibilities.add(newPossib);
                } else if (req1 instanceof RequiredCourseSet) {
                    continue;
                }
            }
            for (Iterator<Requirable> cat1It = category1.iterator(); cat1It.hasNext();) {
                Requirable req1 = cat1It.next();
                
              //  if (req1 instanceof Course && ((Course) req1).deptNum.equals("110"))
                //    System.out.println("Here");
                if (req1 instanceof Course) continue;
                else if (req1 instanceof RequiredCourseSet) {
                    for (Requirable req : (RequiredCourseSet) req1) {
                        
                    }
                    ((RequiredCourseSet) req1).getNumRequired();
                    // [030.105, 171.103, 110.106, 110.108, 171.101, 173.112, 030.101, 030.225, 020.373, 020.306, 020.316, 020.363, 020.315, 020.305, 020.303]
                    // [030.105, 171.101, 173.112, 030.101, 110.106, 020.316, 020.303, 020.340, 020.363, 020.305, 020.306, 020.315]
                    
                    //[030.101, 173.112, 171.101, 030.105, 110.108, 020.306, 020.303, 020.316, 020.315, 020.305, 020.363, 020.373] 12
                    //[030.101, 173.112, 171.101, 030.105, 110.106, 020.306, 020.303, 020.316, 020.315, 020.305, 020.363, 030.225, 020.340] 13
                    
                    for(HashSet<Course> hsc : getAllCoursePossibilities(alreadyPossibility, (RequiredCourseSet) req1)) {
                        HashSet<Course> newPossib = new HashSet<>(alreadyPossibility);
                        newPossib.addAll(hsc);
                        newPossibilities.add(newPossib);
                    }
                }
            }
            allPossibilities = newPossibilities;
        }
        return allPossibilities;
    }
    
    
    /*
     * Old:
     * [171.101, 030.105, 171.103, 030.101, 110.108, 110.106, 173.112, 020.315, 020.340, 020.363, 020.316, 020.306, 020.305, 020.303]
     * [171.103, 110.106, 173.112, 030.105, 110.108, 171.101, 030.101, 020.363, 020.315, 020.305, 020.306, 020.340, 020.316, 020.303]
     * 
     * New:
     * [030.105, 030.101, 030.225, 020.363, 020.373, 020.303, 020.305, 020.316, 020.306, 020.315]
     */
    public static RequiredCourseSet generateLeastRequirements(HashSet<Course> coursesBeingTaken, RequiredCourseSet category1, RequiredCourseSet category2) {
        HashSet<Course> inBothCourses = category1.getSetOfCourses();
        HashSet<Course> courses2 = category2.getSetOfCourses();
        inBothCourses.retainAll(courses2);
        RequiredCourseSet combinedReqs = new RequiredCourseSet(0);
        for (Requirable req1: category1) {
            if (req1 instanceof RequiredCourseSet) {
                continue;
            } else if (req1 instanceof GenericCourse) {
                boolean shouldAdd = true;
                for (Iterator<Course> coursesBeingTakenIt = coursesBeingTaken.iterator();coursesBeingTakenIt.hasNext();) {
                    Course courseBeingTaken = coursesBeingTakenIt.next();
                    if (courseBeingTaken instanceof HopkinsCourse) { if (((GenericCourse) req1).isSatisfiedBy((HopkinsCourse) courseBeingTaken)) shouldAdd = false;;
                    } else if (courseBeingTaken instanceof GenericCourse) {
                        if ( ((GenericCourse) req1).isSubsetOf((GenericCourse) courseBeingTaken) ) shouldAdd = false ;
                        else if (((GenericCourse) courseBeingTaken).isSubsetOf((GenericCourse) req1)) coursesBeingTakenIt.remove();
                        else shouldAdd = false;;
                    } else System.out.println("Error. courseBeingTaken is not any of the two categories: " + courseBeingTaken);
                }
                if (shouldAdd) combinedReqs.add(req1);
            } else if (req1 instanceof HopkinsCourse) {
                if (!coursesBeingTaken.contains((HopkinsCourse) req1))
                    combinedReqs.add(req1);
            }
        }
        
        add2ndCategoryDefinites:
        for (Requirable req2: category2) {
            if (req2 instanceof RequiredCourseSet) {
                continue add2ndCategoryDefinites;

            } else if (req2 instanceof HopkinsCourse) {
                if (coursesBeingTaken.contains((HopkinsCourse) req2)) continue add2ndCategoryDefinites;
                boolean shouldAdd = true;
                for (Requirable req1 : category1) {
                    if (req1 instanceof RequiredCourseSet) {
                    } if (req1 instanceof HopkinsCourse) {
                        if (((HopkinsCourse) req2).equals((HopkinsCourse) req1))
                            shouldAdd = false;
                    } else if (req1 instanceof GenericCourse) {
                        if (((GenericCourse) req1).isSatisfiedBy((HopkinsCourse) req2)) {
                            combinedReqs.remove(req1);
                        } else continue;
                    } else System.out.println("Error. Requirement1 is not any of three categories: " + req1);
                }
                if (shouldAdd) combinedReqs.add(req2);
            } else if (req2 instanceof GenericCourse) {
                boolean shouldAdd = true;
                for (Iterator<Course> coursesBeingTakenIt = coursesBeingTaken.iterator();coursesBeingTakenIt.hasNext();) {
                    Course courseBeingTaken = coursesBeingTakenIt.next();
                    if (courseBeingTaken instanceof HopkinsCourse) { if (((GenericCourse) req2).isSatisfiedBy((HopkinsCourse) courseBeingTaken)) shouldAdd = false;;
                    } else if (courseBeingTaken instanceof GenericCourse) {
                        if ( ((GenericCourse) req2).isSubsetOf((GenericCourse) courseBeingTaken) ) shouldAdd = false ;
                        else if (((GenericCourse) courseBeingTaken).isSubsetOf((GenericCourse) req2)) coursesBeingTakenIt.remove();
                        else shouldAdd = false;
                    } else System.out.println("Error. courseBeingTaken is not any of the two categories: " + courseBeingTaken);
                }
                for (Requirable req1 : category1) {
                    if (req1 instanceof RequiredCourseSet) {
                        continue;
                    } else if (req1 instanceof HopkinsCourse) {
                        if (((GenericCourse) req2).isSatisfiedBy((HopkinsCourse) req1));
                    } else if (req1 instanceof GenericCourse) { // should combine GenericCourses at the very end
                        if ( ((GenericCourse) req2).isSubsetOf((GenericCourse) req1)) shouldAdd = false;
                        else if(((GenericCourse) req1).isSubsetOf((GenericCourse) req2)) combinedReqs.remove((GenericCourse) req1);
                    } else System.out.println("Error. Requirement1 is not any of three categories: " + req1);
                }
                if (shouldAdd) combinedReqs.add(req2);
            } else System.out.println("Error. In add2ndCategoryDefinites loop, requirement2 is not any of three categories: " + req2);
        }
        return combinedReqs;
    }

  /*  public static RequiredCourseSet generateLeastRequirements(RequiredCourseSet category1, RequiredCourseSet category2) {
           // try {

            /*    RequiredCourseSet combined = new RequiredCourseSet(0);
                ArrayList<String> specials = new ArrayList<String>();
                for (Iterator<Requirable> i = category1.iterator();i.hasNext();) {
                    String cat1Str = i.next().toString();
                    if (cat1Str.startsWith("[") && cat1Str.endsWith("]")) cat1Str = cat1Str.substring(1, cat1Str.length()-1);
                    if (cat1Str.startsWith("[") && cat1Str.endsWith("]")) cat1Str = cat1Str.substring(1, cat1Str.length()-1);
                    if (cat1Str.endsWith("0") || cat1Str.contains("|") || cat1Str.toLowerCase().contains("or")) {specials.add(cat1Str);}
                    //else combined.add(cat1Str); Need to fix
                }
                for (Iterator<Requirable> i = category2.iterator(); i.hasNext();) {
                    String cat2Str = i.next().toString();
                    if (cat2Str.startsWith("[") && cat2Str.endsWith("]")) cat2Str = cat2Str.substring(1, cat2Str.length()-1).replace(", ", "and");
                    if (cat2Str.startsWith("{") && cat2Str.endsWith("}")) cat2Str = cat2Str.substring(1, cat2Str.length()-1).replace(", ", "or");
                    if (cat2Str.endsWith("0") || cat2Str.contains("|")|| cat2Str.toLowerCase().contains("or")) {
                        if (!specials.contains(cat2Str)) specials.add(cat2Str);
                    }// else combined.add(cat2Str); Need to fix
                }
                
                special:
                for (Iterator<String> iterator = specials.iterator(); iterator.hasNext();) {
                    String special = iterator.next();
                    if (special.contains("|") || special.toLowerCase().contains("or")) {
                        String[] specialparts = special.split("\\||OR|or|Or|oR");
                        boolean contains = false;
                        for (String specialpart : specialparts) {
                            if (combined.contains(specialpart.trim()) || courseMatches(combined, specialpart.trim())) { //second half of this may take a long time
                                contains = true;
                                iterator.remove();
                                continue special;
                            }
                        }
                        if (!contains) combined.add(special);
                    } else if (special.length() < 3) {
                        if (courseMatches(combined, special)) {
                            iterator.remove();
                            continue special;
                        }
                    }
                }
                for (String str : specials) combined.add(str);
                ArrayList<String> sortedcombined = new ArrayList<String>(combined);
                Collections.sort(sortedcombined);
                System.out.println("combined: " + sortedcombined);
                System.out.println("	".matches("\\t"));
                */
                
                /*if (set1.size() > set2.size()) {
                    Iterator<String> iter2 = set2.iterator();
                    while (iter2.hasNext()) {
                        String course2 = iter2.next();
                        if (set1.contains(course2)) {
                            
                        } else {
                            Iterator<String> iter1 = set1.iterator();
                            while (iter1.hasNext()) {
                                String course1 = iter1.next();
                                
                            }
                        }
                        //System.out.println(iter.next());
                    }
                }*/
              //  return null;
          /*  } catch (IOException ex) {
                return null;
                
            } */
   // } 
   
   //not finished
    /*public static int getNumberOfCoursesThatNeed(HopkinsCourse thisCourse) {
        allCourses.add(thisCourse);
        for (Iterator<HopkinsCourse> i = allCourses.iterator(); i.hasNext(); ) {
            HopkinsCourse checkingCourse = i.next();
            
        }
        return 0;
    }*/
   
    public static ArrayList<HopkinsCourse> getPriorities(Collection<Course> preReqs) {
        // Generic Courses must be added before this step
        HashMap<HopkinsCourse, Float> priorities = new HashMap<>();
        addPriorities(priorities, preReqs, 1f);
        ArrayList<HopkinsCourse> sortedCourses = new ArrayList<>();
        
        Iterator<HopkinsCourse> it = priorities.keySet().iterator();
        sortedCourses.add(it.next());
        for (;it.hasNext();) {
            HopkinsCourse currentCourse = it.next();
            float currentPriority = priorities.get(currentCourse);
            boolean hasAdded;
            findSpot:
            for (int i = 0; i < sortedCourses.size();i++) { // Turn into binary sort later
                if (priorities.get(sortedCourses.get(i)) < currentPriority) {
                    sortedCourses.add(i, currentCourse);
                    break findSpot;
                } else if ( i == sortedCourses.size()-1) {
                    sortedCourses.add(currentCourse);
                    break findSpot;
                }
            }
        }
        return sortedCourses;
    }
   private static void addPriorities(Map<HopkinsCourse, Float> toAddTo, Collection<Course> preReqs, float factor) {
        if (preReqs == null || preReqs.isEmpty()) return;
       // float priority = preReqs.getTrueNumRequired() * factor / preReqs.size();
        for (Course preReq : preReqs) {
            if (preReq instanceof HopkinsCourse) {
                addPriorities(toAddTo, ((HopkinsCourse) preReq).preReqs, factor);
                Float previousPriority =  toAddTo.get((HopkinsCourse) preReq);
                toAddTo.put((HopkinsCourse) preReq, previousPriority == null ? factor - 0.001f*((Course) preReq).getLevel() : previousPriority + factor - 0.001f*((Course) preReq).getLevel());
            }
        }
        for (Course preReq : preReqs) {
            if (preReq instanceof GenericCourse) {
                boolean added = false;
                for (HopkinsCourse sample : ((GenericCourse) preReq).getPossibleHopkinsCourses()) {
                    if (toAddTo.get(sample) == null) {
                        toAddTo.put(sample, factor - 0.001f*((Course) preReq).getLevel());
                        added = true;
                        break;
                    }
                }
                if (!added)
                    System.out.println("Error not of courses to create distinctive list");
            }
        }
    }
    /*
    public static ArrayList<HopkinsCourse> getPriorities(RequiredCourseSet preReqs) {
        HashMap<HopkinsCourse, Float> priorities = new HashMap<>();
        addPriorities(priorities, preReqs, 1f);
        ArrayList<HopkinsCourse> sortedCourses = new ArrayList<>();
        Iterator<HopkinsCourse> it = priorities.keySet().iterator();
        sortedCourses.add(it.next());
        for (;it.hasNext();) {
            HopkinsCourse currentCourse = it.next();
            float currentPriority = priorities.get(currentCourse);
            boolean hasAdded;
            findSpot:
            for (int i = 0; i < sortedCourses.size();i++) { // Turn into binary sort later
                if (priorities.get(sortedCourses.get(i)) < currentPriority) {
                    sortedCourses.add(i, currentCourse);
                    break findSpot;
                } else if ( i == sortedCourses.size()-1) {
                    sortedCourses.add(currentCourse);
                    break findSpot;
                }
            }
        }
        return sortedCourses;
    }*/
   private static void addPriorities(Map<HopkinsCourse, Float> toAddTo, RequiredCourseSet preReqs, float factor) {
        if (preReqs == null || preReqs.isEmpty()) return;
        float priority = preReqs.getTrueNumRequired() * factor / preReqs.size();
        for (Requirable preReq : preReqs) {
            if (preReq instanceof HopkinsCourse) {
                addPriorities(toAddTo, ((HopkinsCourse) preReq).preReqs, priority);
                Float previousPriority =  toAddTo.get((HopkinsCourse) preReq);
                toAddTo.put((HopkinsCourse) preReq, previousPriority == null ? priority - 0.001f*((Course) preReq).getLevel(): previousPriority + priority  - 0.001f*((Course) preReq).getLevel());
            } else if (preReq instanceof RequiredCourseSet) {
                addPriorities(toAddTo, (RequiredCourseSet) preReq, priority);
            }
        }
        for (Requirable preReq : preReqs) {
            if (preReq instanceof GenericCourse) {
                boolean added = false;
                for (HopkinsCourse sample : ((GenericCourse) preReq).getPossibleHopkinsCourses()) {
                    if (toAddTo.get(sample) == null) {
                        toAddTo.put(sample, factor - 0.001f*((Course) preReq).getLevel());
                        added = true;
                        break;
                    }
                }
                if (!added) 
                    System.out.println("Error not of courses to create distinctive list");
            }
        }
    }
   
   private static boolean courseMatches(Collection<String> courses, String genericCourse) {
       while (genericCourse.endsWith("0")) {
           genericCourse = genericCourse.substring(0, genericCourse.length() - 1);
       }
       genericCourse = ".*" + genericCourse.substring(0, genericCourse.length() - 1) + "[" + genericCourse.substring(genericCourse.length() - 1) + "-9].*";
       for (String course : courses) {
           if (course.matches(genericCourse)) return true;
       }
       return false;
   }
}
