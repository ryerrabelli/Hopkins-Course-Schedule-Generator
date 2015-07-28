/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import scheduleGenerator.DbFiles.Functions;

/**
 *
 * @author ryerrabelli
 */
public class RequiredCourseSet extends HashSet<Requirable>  implements Requirable {
    protected int numRequired = 0; //0 means all courses are required, negative number means counting from the total num of reqs
    
    public RequiredCourseSet(int numRequired) {
        this.numRequired = numRequired;
    }
    public RequiredCourseSet(HashSet<Requirable> reqCourses, int numRequired) {
        this.numRequired = numRequired;
        this.addAll(reqCourses);
        
    }

    public RequiredCourseSet(int numRequired, Object...reqs) {
        this.numRequired = numRequired;
        for (Object req : reqs) {
            if (req instanceof HopkinsCourse) this.add((HopkinsCourse) req);
            else if (req instanceof GenericCourse) this.add((GenericCourse) req);
            else if (req instanceof RequiredCourseSet) {
                if (numRequired == 0 && ((RequiredCourseSet) req).getNumRequired() == 0) this.addAll((RequiredCourseSet) req);
                else if (numRequired == 1 && ((RequiredCourseSet) req).getNumRequired() == 1) this.addAll((RequiredCourseSet) req);
                else this.add((RequiredCourseSet) req);
            } else if (req instanceof String && (((String) req).contains("[") || ((String) req).contains("{"))) {
                String strReq = (String) req;
                strReq = strReq.trim();
                int innerNumRequired = 0;
                if (strReq.startsWith("[")) innerNumRequired = 1;
                strReq = strReq.substring(1, strReq.length()-1);
                ArrayList<Integer> commas = indOfCorresp(strReq, new char[] {'[','{'},new char[] {']','}'},',');
                commas.add(strReq.length());
                int pComma = -1;
                RequiredCourseSet innerRequiredCourseSet = new RequiredCourseSet(innerNumRequired);
                commaLoop:
                for(int comma: commas) {
                    String part = strReq.substring(pComma+1, comma).trim();
                    pComma = comma;
                    if (part.contains("[")) {
                        if (this.getNumRequired() == 1) innerRequiredCourseSet.addAll(new RequiredCourseSet(1, part));
                        else innerRequiredCourseSet.add(new RequiredCourseSet(1, part));
                    } else if (part.contains("{")) {
                        if (this.getNumRequired() == 0) innerRequiredCourseSet.addAll(new RequiredCourseSet(0, part)); 
                        else innerRequiredCourseSet.add(new RequiredCourseSet(0, part));
                    } else {
                        if (part.length() < 3) {
                            innerRequiredCourseSet.add(new GenericCourse(part, 0, ""));
                        } else {
                            HopkinsCourse toAdd = HopkinsCourse.getCourse(part);
                            if (toAdd == null) continue commaLoop;
                            innerRequiredCourseSet.add(toAdd);
                        }
                    }
                }
                if (innerRequiredCourseSet.getNumRequired() == 0 && numRequired == 0) this.addAll(innerRequiredCourseSet);
                else if (innerRequiredCourseSet.getNumRequired() == 1 && numRequired == 1) this.addAll(innerRequiredCourseSet);
                else this.add(innerRequiredCourseSet);
            } else if (HopkinsCourse.getCourse(req.toString()) != null) this.add(HopkinsCourse.getCourse(req.toString()));
            else if (req.toString().matches("([a-zA-Z]{2}\\.)?[0-9]{3}\\.[0-9]{3,}")) this.add(HopkinsCourse.getCourse(req.toString()));
            else if (req.toString().matches("([a-zA-Z]{2}\\.)?[0-9]{3}\\.[0-9]+")) this.add(new GenericCourse(req.toString(), -1f, ""));
            else System.out.println("Could not add to RequiredCourseSet:" + req);
        }
    } 
    
    //Not finished, will duplicate input
    public RequiredCourseSet(Collection<Requirable> reqCourseSet) {
        for (Iterator<Requirable> i = reqCourseSet.iterator(); i.hasNext(); ) {
            Requirable requirement = i.next();
            
        }
    }
        
    public static RequiredCourseSet stringToRequiredCourseSet(String input) {
        input = input.replace("[+]", "");
        input = input.replaceAll(" *[oO][rR] *", "∨");
        input = input.replaceAll(" *[aA][nN][dD] *", "∧");
        input = input.substring(input.indexOf(":")+1);
        Object b = Functions.conv2Val(input);
        return new RequiredCourseSet(0,b);
    }
    
    /*public RequiredCourseSet convertToRequiredCourseSet(String input) {
        ArrayList allParts = new ArrayList();
        allParts.add(input);
        while (input.contains("(") && input.contains(")")) {
            String[] parts = input.split("\\Q(\\E", 2);
            int secInd = indOfCorresp(parts[1],'(',')');
            RequiredCourseSet inside = this.convertToRequiredCourseSet(parts[1].substring(0,secInd));
            allParts.remove(input);
            allParts.add(parts[0]);
            allParts.add(inside);
            allParts.add(input.substring(secInd+1));
        }
    }*/
    public static ArrayList<Integer> indOfCorresp(String input, char[] openings, char[] closings, char searchFor) {
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        int correctLevel = 0; int[] changingLevels = new int[openings.length];
        for (int i = 0; i < openings.length; i++) changingLevels[i] = 0;
        for (int i = 0; i < input.length(); i++){
            char c = input.charAt(i);
            boolean canCheck = true;
            for (int o = 0; o < openings.length; o++) {
                if (openings[o] == c) changingLevels[o]++;
                else if (closings[o] == c) changingLevels[o]--;
                if (changingLevels[o] != correctLevel) canCheck = false;
            }
            
            if (canCheck && input.charAt(i)==searchFor) toReturn.add(i);
        }
        return toReturn;
    }
    
    public HashSet<Course> getSetOfCourses() {
        HashSet<Course> courses = new HashSet<>();
        for (Requirable req : this) {
            if (req instanceof Course) courses.add((Course) req);
            else if (req instanceof RequiredCourseSet) {
                courses.addAll(this.getSetOfCourses());
            }
        }
        return courses;
    }
    
    /*public RequiredCourseSet stringToRequiredCourseSet(String str) {
        str = str.trim();
        int numReq = 1;
        if (str.startsWith("{") && str.endsWith("}")) {
            numReq = 0;
            str = str.substring(0, str.length()-1);
        } else if (str.startsWith("[") && str.endsWith("]")) str = str.substring(0, str.length()-1);
        
    }*/
    

    
    public void addReq(Course course) {
        this.add(course);
    }
    public void addReq(RequiredCourseSet reqCourses) {
        this.add(reqCourses);
    }
    public void addReq(HashSet<Requirable> reqCourses, int numReq) {
        this.add(new RequiredCourseSet(reqCourses, numReq));
    }
    
    @Override
    public boolean isFulfilled(Set<HopkinsCourse> coursesTaken) {
        int reqsMet = 0;
        int reqsNeeded = getTrueNumRequired(); 
        for (Iterator<Requirable> i = this.iterator(); i.hasNext();) {
            Requirable requirement = i.next();
            if (requirement instanceof Course) { // polymorphism with HopkinsCourse
                if (((Course) requirement).isFulfilled(coursesTaken) ) reqsMet++;
            } else if (requirement instanceof RequiredCourseSet) {
                if ( ((RequiredCourseSet) requirement).isFulfilled(coursesTaken)) reqsMet++;
            } else System.out.println("ERROR: Requirable is not an instance of GenericCourse or RequiredCourseSet: " + requirement);
        }
        return reqsMet >= reqsNeeded;
    }
    
    public int getNumRequired() {
        return numRequired;
    }
    public int getTrueNumRequired() {
        return numRequired > 0 ? numRequired : this.size() + numRequired;
    }
    
    /*@Override
    public boolean equals(Object b) {
        if (b instanceof Requirable) {
            return true; //need to complete
        } else return false;
    } */
    
    @Override
    public String toString() {
        /*String toReturn = "";
        Iterator<Requirable> i = this.iterator();
        if (i.hasNext()) toReturn = i.next().toString();
        for (; i.hasNext();) {
            toReturn = ", " + i.next().toString();
        }
        if (this.getNumRequired() == 0) return "{" + toReturn + "}";
        else return "[" + toReturn + "]";
        */
        
        
        if (this.getNumRequired() == 0) {
            String orig = super.toString();
            int correctLevel = 0;
            int[] changingLevels = new int[3];
            for (int i = 0; i < 3; i++) {
                changingLevels[i] = 0;
            }
            for (int i = 0; i < orig.length(); i++) {
                char c = orig.charAt(i);
                boolean canCheck = true;
                if (c == '(') changingLevels[0]++;
                else if (c == '[') {
                    for (int a : changingLevels) if (a!=correctLevel) canCheck = false;
                    if (canCheck) orig = orig.substring(0,i) + "{" + orig.substring(i+1);
                    changingLevels[1]++;
                } else if (c == '{') changingLevels[2]++;
                else if (c == ')') changingLevels[0]--;
                else if (c == ']') {
                    changingLevels[1]--;
                    for (int a : changingLevels) if (a!=correctLevel) canCheck = false;
                    if (canCheck) orig = orig.substring(0,i) + "}" + orig.substring(i+1);
                } else if (c == '}') changingLevels[2]--;
                
            }
            return orig;
            //return super.toString().replace("[", "{").replace("]", "}");
        } else return super.toString(); 
   }
}
