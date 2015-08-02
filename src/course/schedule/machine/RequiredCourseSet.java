/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import scheduleGenerator.DbFiles.Functions;
import scheduleGenerator.DbFiles.ManageTxtFiles;

/**
 *
 * @author ryerrabelli
 */
public class RequiredCourseSet extends HashSet<Requirable>  implements Requirable {
    protected int numRequired = 0; //0 means all courses are required, negative number means counting from the total num of reqs
    
    public static HashMap<String, RequiredCourseSet> categoryRequiredCourseSets = new HashMap<>();
    
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
            if (req == null) {
                System.out.println("Note, a course in the RCS constructor was found to be null");
                continue;
            }
            if (req instanceof HopkinsCourse) this.add((HopkinsCourse) req);
            else if (req instanceof GenericCourse) this.add((GenericCourse) req);
            else if (req instanceof RequiredCourseSet) {
                if (numRequired == 0 && ((RequiredCourseSet) req).getNumRequired() == 0) this.addAll((RequiredCourseSet) req);
                else if (numRequired == 1 && ((RequiredCourseSet) req).getNumRequired() == 1) this.addAll((RequiredCourseSet) req);
                else if (numRequired == -1) {
                    this.numRequired = ((RequiredCourseSet) req).getNumRequired();
                    this.addAll((RequiredCourseSet) req);
                } else this.add((RequiredCourseSet) req);
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
                            innerRequiredCourseSet.add(new GenericCourse(part));
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
            else if (req.toString().matches("([a-zA-Z]{2}\\.)?[0-9]{3}\\.[0-9]+")) this.add(new GenericCourse(req.toString()));
            else System.out.println("Could not add to RequiredCourseSet:" + req);
        }
    } 
    
    public RequiredCourseSet(RequiredCourseSet reqCourseSet) {
        this.numRequired = reqCourseSet.numRequired;
        for (Iterator<Requirable> i = reqCourseSet.iterator(); i.hasNext(); ) {
            Requirable next = i.next();
            if (next instanceof HopkinsCourse) this.add((HopkinsCourse) next);
            else if (next instanceof GenericCourse) this.add( new GenericCourse( ((GenericCourse) next).toString()) );
            else if (next instanceof RequiredCourseSet) this.add( ((RequiredCourseSet) next).duplicate());
            else System.out.println("Error. Requirable is not one of the three categories");
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
    public static Requirable stringToRequirable(String input) {
        input = input.replace("[+]", "");
        input = input.replaceAll(" *[oO][rR] *", "∨");
        input = input.replaceAll(" *[aA][nN][dD] *", "∧");
        input = input.substring(input.indexOf(":")+1);
        Object b = Functions.conv2Val(input);
        if (input.contains("[") || input.contains("{") || input.contains(",") || input.contains("∨") || input.contains("∧")) return new RequiredCourseSet(-1,b);
        else if (input.matches("(AS\\.|EN\\.)?\\d{3}\\.\\d{3}")) return HopkinsCourse.getCourse(input);
        else return new GenericCourse(input);
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
        public static HopkinsCourse getCourse(String input) {return HopkinsCourse.getCourse(input); }
        public static void addCat(String title, RequiredCourseSet rcs ) {categoryRequiredCourseSets.put("groups/" + title.toLowerCase().trim().replace(" ", "_"), rcs); }
        public static void addMaj(String title, RequiredCourseSet rcs ) {categoryRequiredCourseSets.put("majors/" + title.toLowerCase().trim().replace(" ", "_"), rcs); }
        public static void addMin(String title, RequiredCourseSet rcs ) {categoryRequiredCourseSets.put("minors/" + title.toLowerCase().trim().replace(" ", "_"), rcs); }
        public RequiredCourseSet duplicate() {
        return new RequiredCourseSet(this);
    }
      
        public static void createCategoryRequiredCourseLists() throws IOException {
        ManageTxtFiles.getAllCourses();

        // Premed
        RequiredCourseSet premedRCS = new RequiredCourseSet(new HashSet<Requirable>(HopkinsCourse.getCourses("030.101","030.105","171.101","173.111","171.102","173.112","030.102","030.106","030.205","030.206","030.225")),0);
        premedRCS.add(new GenericCourse("020.1<credits=3>"));
        premedRCS.add(new GenericCourse("020.1<credits=3>"));
        categoryRequiredCourseSets.put("premed", premedRCS);

        //calc physics
        RequiredCourseSet calcPhys = new RequiredCourseSet(0);
        calcPhys.add(HopkinsCourse.getCourse("110.108"));
        calcPhys.add(HopkinsCourse.getCourse("110.109"));
        categoryRequiredCourseSets.put("groups/calc phys", calcPhys);

        //calc bio
        RequiredCourseSet calcBio = new RequiredCourseSet(0);
        calcBio.add(HopkinsCourse.getCourse("110.106"));
        calcBio.add(HopkinsCourse.getCourse("110.107"));
        categoryRequiredCourseSets.put("groups/calc bio", calcBio);

        //calc 2 courses
        RequiredCourseSet calc2Courses = new RequiredCourseSet(1);
        calc2Courses.add(calcBio);
        calc2Courses.add(calcPhys);
        
        //calc intro
        RequiredCourseSet introCalc = new RequiredCourseSet(calc2Courses);
        introCalc.add(HopkinsCourse.getCourse("110.113"));
        addCat("intro calc", introCalc);

        //calc 3
        RequiredCourseSet calc3 = new RequiredCourseSet(1);
        calc3.add(HopkinsCourse.getCourse("110.202"));
        calc3.add(getCourse("110.211"));
        addCat("calc 3", calc3);
        
        // Linear Algebra and Differential Equations, 2 separate courses
        RequiredCourseSet linAlgDifEq = new RequiredCourseSet(0); 
        linAlgDifEq.add(getCourse("110.201"));
        linAlgDifEq.add(getCourse("110.302"));
        addCat("lin alg dif eq", linAlgDifEq);
        
        //Linear Algebra and Differential Equations, other ways to fill with 2 courses
        
        RequiredCourseSet LADEPlus = new RequiredCourseSet(1);
        LADEPlus.add(getCourse("550.291"));
        LADEPlus.add(new GenericCourse("110.3<credits=4>"));
        RequiredCourseSet linAlgDifEq2Courses = new RequiredCourseSet(1);
        linAlgDifEq2Courses.add(LADEPlus);
        linAlgDifEq2Courses.add(linAlgDifEq);
        
        // physics biological science majors
        RequiredCourseSet physicsBio = new RequiredCourseSet(0);
        physicsBio.add(getCourse("171.103"));
        physicsBio.add(getCourse("171.104"));
        addCat("physics bio", physicsBio);

        // physics physical science majors
        RequiredCourseSet physicsPhys = new RequiredCourseSet(0);
        physicsPhys.add(getCourse("171.101"));
        physicsPhys.add(getCourse("171.102"));
        addCat("physics phys", physicsPhys);
        
        // physics
        RequiredCourseSet physics = new RequiredCourseSet(1);
        physics.add(physicsBio.duplicate());
        physics.add(physicsPhys.duplicate());
        addCat("physics", physics);
        
        // physics biological science majors with lab
        RequiredCourseSet physicsBioLab = new RequiredCourseSet(0, physicsBio);
        physicsBioLab.add(getCourse("173.111"));
        physicsBioLab.add(getCourse("173.112"));
        addCat("physics bio with lab", physicsBioLab);

        // physics physical science majors with lab
        RequiredCourseSet physicsPhysLab = new RequiredCourseSet(0, physicsPhys);
        physicsPhysLab.add(getCourse("173.111"));
        physicsPhysLab.add(getCourse("173.112"));
        addCat("physics phys with lab", physicsPhysLab);

        // physics Lab
        RequiredCourseSet physicsLab = new RequiredCourseSet(1);
        physicsLab.add(physicsBioLab.duplicate());
        physicsLab.add(physicsPhysLab.duplicate());
        addCat("physics with lab", physics);
        
        // intro chem with lab
        RequiredCourseSet chemLab = new RequiredCourseSet(0);
        chemLab.add(getCourse("030.101"));
        chemLab.add(getCourse("030.102"));
        chemLab.add(getCourse("030.105"));
        chemLab.add(getCourse("030.106"));
        RequiredCourseSet chemLabOrAP = new RequiredCourseSet(1);
        chemLabOrAP.add(chemLab);
        chemLabOrAP.add(new RequiredCourseSet(0, getCourse("030.100"), getCourse("030.103")));
        addCat("chem with lab", chemLabOrAP);
        
        // orgo
        RequiredCourseSet orgo = new RequiredCourseSet(0);
        orgo.add(getCourse("030.205"));
        orgo.add(new RequiredCourseSet(1, getCourse("030.206"), getCourse("030.212")));
        addCat("orgo", orgo);
        
        //orgo lab
        RequiredCourseSet orgoLab = orgo.duplicate();
        orgoLab.add(new RequiredCourseSet(1, getCourse("030.225"), getCourse("030.227")));
        addCat("orgo with lab", orgoLab);
        
        // BME major without track
        RequiredCourseSet BMEMajor =new RequiredCourseSet(0);
        BMEMajor.addAll(physicsPhysLab);
        BMEMajor.addAll(chemLabOrAP);
        BMEMajor.add(getCourse("030.205"));
        BMEMajor.addAll(calcPhys);
        BMEMajor.add(calc3);

        BMEMajor.add(linAlgDifEq2Courses);
        BMEMajor.add(new GenericCourse("550.3<credits=3>"));
        BMEMajor.add(new GenericCourse("000.1<tag=programming, credits=3>"));
        BMEMajor.add(getCourse("580.111"));
        BMEMajor.add(getCourse("580.202"));
        BMEMajor.add(getCourse("580.221"));
        BMEMajor.add(getCourse("580.222"));
        BMEMajor.add(getCourse("580.223"));
        BMEMajor.add(getCourse("580.321"));

        BMEMajor.add(getCourse("580.421"));
        BMEMajor.add(getCourse("580.422"));
        BMEMajor.add(getCourse("580.423"));
        BMEMajor.add(getCourse("580.424"));
        BMEMajor.add(getCourse("580.429"));
        addMaj("bme", BMEMajor);

        // Molecular and Cellular Bio major
        RequiredCourseSet molCelBiomajor = new RequiredCourseSet(0);
        molCelBiomajor.add(calc2Courses.duplicate());
        molCelBiomajor.add(chemLab);
        molCelBiomajor.add(orgoLab);
        molCelBiomajor.add(getCourse("020.305"));
        molCelBiomajor.add(getCourse("020.315"));
        molCelBiomajor.add(getCourse("020.306"));
        molCelBiomajor.add(getCourse("020.316"));
        molCelBiomajor.add(getCourse("020.303"));
        molCelBiomajor.add(getCourse("020.363"));
        molCelBiomajor.add(new RequiredCourseSet(1,getCourse("020.340"), getCourse("020.373")));
        molCelBiomajor.add(physicsLab);
        addMaj("molcelbio", molCelBiomajor);
        
        //MSE major
        RequiredCourseSet MSEMajor = new RequiredCourseSet(0);
        
        MSEMajor.add(getCourse("510.311"));
        MSEMajor.add(getCourse("510.312"));
        MSEMajor.add(getCourse("510.313"));
        MSEMajor.add(getCourse("510.314"));
        MSEMajor.add(getCourse("510.315"));
        MSEMajor.add(getCourse("510.316"));
        MSEMajor.add(getCourse("510.428"));
        MSEMajor.add(getCourse("510.429"));
        MSEMajor.add(getCourse("510.433"));
        MSEMajor.add(getCourse("510.434"));
        for (int i = 0; i <4; i++) MSEMajor.add(new GenericCourse("510.300<credits=3,for=mse>"));
        MSEMajor.add(physicsPhysLab);
        MSEMajor.add(getCourse("510.101"));
        MSEMajor.add(getCourse("030.105"));
        MSEMajor.add(getCourse("030.106"));
        MSEMajor.add(getCourse("030.205"));
        MSEMajor.add(getCourse("030.225"));
        MSEMajor.add(getCourse("510.202"));
        MSEMajor.add(new GenericCourse("600.3<credits=3,for=mse>"));
        MSEMajor.add(calcPhys);
        MSEMajor.add(calc3);
        MSEMajor.add(linAlgDifEq);
        addMaj("mse", MSEMajor);
        
        
        RequiredCourseSet biophysicsMajor = new RequiredCourseSet(0);
        biophysicsMajor.add(chemLabOrAP);
        biophysicsMajor.add(orgo);
        biophysicsMajor.add(physicsLab);
        biophysicsMajor.add(getCourse("171.310"));
        biophysicsMajor.add(calcPhys);
        biophysicsMajor.add(calc3);
        biophysicsMajor.add(new RequiredCourseSet(1, getCourse("110.201"),getCourse("110.212"),getCourse("550.291")));
        biophysicsMajor.add(getCourse("250.205"));
        biophysicsMajor.add(getCourse("250.253"));
        biophysicsMajor.add(getCourse("250.315"));
        biophysicsMajor.add(getCourse("250.316"));
        biophysicsMajor.add(getCourse("250.345"));
        biophysicsMajor.add(getCourse("250.372"));
        biophysicsMajor.add(getCourse("250.381"));
        biophysicsMajor.add(getCourse("250.383"));
        for (int i=0;i<2;i++) biophysicsMajor.add(new GenericCourse("000.2<tag=biophysics,for=bph>"));
        for (int i=0;i<2;i++) biophysicsMajor.add(new GenericCourse("000.2<tag=biophysics,for=bph>"));
        addMaj("biophysics", biophysicsMajor);
       /* // Biophysics
        RequiredCourseSet biophysicsMajor = new RequiredCourseSet(0);
        biophysicsMajor.add(getCourse("250.265"));
        biophysicsMajor.add(getCourse("250.322"));
        biophysicsMajor.add(getCourse("250.351"));
        biophysicsMajor.add(getCourse("250.353"));
        biophysicsMajor.add(getCourse("250.391"));
        biophysicsMajor.add(getCourse("250.410"));
        biophysicsMajor.add(getCourse("250.519"));
        biophysicsMajor.add(getCourse("250.689"));
        biophysicsMajor.add(getCourse("250.690"));
        
        biophysicsMajor.add(getCourse("030.301"));
        biophysicsMajor.add(getCourse("030.302"));
        biophysicsMajor.add(getCourse("030.423"));
        biophysicsMajor.add((getCourse("030.425")));
        
        biophysicsMajor.add(getCourse("171.204"));
        biophysicsMajor.add(getCourse("171.301"));
        biophysicsMajor.add(getCourse("171.312"));
        biophysicsMajor.add(getCourse("171.411"));
        
        biophysicsMajor.add(getCourse("020.330"));
        biophysicsMajor.add(getCourse("020.346"));
        biophysicsMajor.add(getCourse("020.363"));
        biophysicsMajor.add(getCourse("020.380"));
        
        biophysicsMajor.add(getCourse("600.226"));
        biophysicsMajor.add(getCourse("600.271")); */
        
        //premed
        RequiredCourseSet premed = new RequiredCourseSet(0);
        premed.add(introCalc);
        premed.add(chemLab);
        premed.add(orgoLab);
        premed.add(physicsLab);
        premed.add(new RequiredCourseSet(1, getCourse("220.105"), getCourse("060.113"), getCourse("060.114"))); //English requirement
        addCat("premed", premed);
        
        //Math minor
        RequiredCourseSet mathMinorRCS = new RequiredCourseSet(0);
        mathMinorRCS.add(introCalc);
        mathMinorRCS.add(new GenericCourse("110.2<credits=4>"));
        mathMinorRCS.add(new GenericCourse("110.3<credits=4>"));
        mathMinorRCS.add(new GenericCourse("110.3<credits=4>"));
        mathMinorRCS.add(new RequiredCourseSet(1, new GenericCourse("110.3<credits=4>"), new GenericCourse("550.3<credits=4>")));
        categoryRequiredCourseSets.put("minors/math", mathMinorRCS);

        //Comp sci minor
        RequiredCourseSet compSciMinorGen = new RequiredCourseSet(0);
        compSciMinorGen.add(getCourse("600.107"));
        compSciMinorGen.add(getCourse("600.120"));
        compSciMinorGen.add(getCourse("600.226"));
        
        RequiredCourseSet CSMinorAnalysis = compSciMinorGen.duplicate();
        CSMinorAnalysis.add(getCourse("600.271"));
        for (int i = 0; i<3; i++) CSMinorAnalysis.add(new GenericCourse("600.3<tag=csanalysis,for=csn>"));
        addMin("cs analysis", CSMinorAnalysis);
        
        RequiredCourseSet CSMinorSystems = compSciMinorGen.duplicate();
        CSMinorSystems.add(getCourse("600.233"));
        for (int i = 0; i<3; i++) CSMinorSystems.add(new GenericCourse("600.3<tag=cssystems,for=css>"));
        addMin("cs systems", CSMinorSystems);
        
        RequiredCourseSet CSMinorApp = compSciMinorGen.duplicate();
        CSMinorApp.add(new RequiredCourseSet(1,getCourse("600.233"), getCourse("600.271")));
        for (int i = 0; i<3; i++) CSMinorApp.add(new GenericCourse("600.3<tag=csapplications,for=csp>"));
        addMin("cs applications", CSMinorApp);
        
        //Computer Integrated Surgery Minor
        RequiredCourseSet compSurgeryMinor = new RequiredCourseSet(0);
        compSurgeryMinor.add(getCourse("600.107"));
        compSurgeryMinor.add(getCourse("600.226"));
        
        RequiredCourseSet calc3PlusExtra = new RequiredCourseSet(1);
        calc3PlusExtra.add(calc3.duplicate());
        calc3PlusExtra.add(getCourse("110.212"));
        
        compSurgeryMinor.add(calc2Courses.duplicate());
        compSurgeryMinor.add(calc3PlusExtra.duplicate());
        
        RequiredCourseSet higherMath = new RequiredCourseSet(1);
        higherMath.add(getCourse("550.291"));
        higherMath.add(getCourse("110.201"));
        higherMath.add(getCourse("110.211"));
        higherMath.add(getCourse("110.212"));
        
       compSurgeryMinor.add(higherMath.duplicate());
    }
        
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
                courses.addAll(((RequiredCourseSet) req).getSetOfCourses());
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
