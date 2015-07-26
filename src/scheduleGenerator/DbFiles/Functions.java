
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleGenerator.DbFiles;

import course.schedule.machine.Requirable;
import course.schedule.machine.RequiredCourseSet;
import java.math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.crypto.NoSuchMechanismException;

/**
 *
 * @author ryerrabelli
 * @version 1.7
 */

/*
 * places to look when adding new binary operand
 *  indsAtOperands
 *  splitGroupVals
 *  conv2ValDouble
 *  conv2ValPEMDAS (when i is too high)
 *  Displayer
 *      isAnyOperand
 *      getOperand
 *      operandCount
 * 
 */
public  class Functions {
    static Map<String, Object> userConsts = new HashMap<>();
    static Map<String, Double> userBooleans = new HashMap<>();
    static Map<String, Long> userIntegers = new HashMap<>();
    static Map<String, Double> userDoubles = new HashMap<>();
    static Map<String, BigDecimal> userBigDecimal = new HashMap<>();
    static Map<String, Number[][]> userMatrices = new HashMap<>();
    static Map<String, Number> userScalars = new HashMap<>();
//    static Map<String, Vector> user2DVectors = new HashMap<>();
    
        public static short magPrecision = 5;
    public static short anglePrecision = 1;
    
    public static final double tau = 2 * Math.PI;
    public static final char degreeSymbol = '°';
    
    public static byte angleType = 0; // 0 for degrees, 1 for radians
    public static boolean specialFormatting = false;
    public static boolean formatNegSymbSp = true;
    public static boolean showInPi = false;
    public static boolean showExp = false;
    public static boolean showFirstExp = false;
    public static boolean denominatorUnits = true;
    public static boolean displayForces = true;
    public static boolean folPEMDAS = true;
            
    public static byte OS = 1; // 0 = windows, 1 = mac, 2 = unix, 3 = solaris, 4 = other
    public static int radix = 10; // not completed yet
        
    public static final char[] sp = new char[]{'\u2070', '\u00B9', '\u00B2', '\u00B3', '\u2074', '\u2075', '\u2076', '\u2077', '\u2078', '\u2079'};
    public static final char[] radicals = new char[]{'k', 'k', '√', '∛', '∜'}; //unicodes for 2nd 3rd and 4th roots: 
    public static final char spFullStop = '\u00B7';
    public static final char vec = '\u20D7';
    public static final char neg = '~';
    public static final String angleSymb = "∠";
    
    public static final String Infinity = "\u221E", NegativeInfinity = "-"+Infinity, lemniscate = "\u221E";
    public static final char pi = 'π';
    public static final char euler = 'ℯ';
    public static final char phi = 'ϕ';
    
    public static final String lFloor = "⌊";
    public static final String rFloor = "⌋";
    public static final String lCeil = "⌈";
    public static final String rCeil = "⌉";
    public static final String absSymb = "|";
    
    public static final String newLn = System.getProperty("line.separator");
    public static final String newLnHTML = "<br>";
    
    public static final Object cNoReturn = "\\NORETURN\\";
    public static final Object cNoSolutions = "\\NOSOLUTIONS\\";
    public static final Object cAllReals = "\\ALLREALS\\";
    
    public static final String conversionOperator = "➤";
    public static final char remSymb = '٪';
    public static String bigDecimalExt = "BD";
    
    private final static long birth = System.nanoTime();
    
    public static ManageTxtFiles txtfiles = new ManageTxtFiles();
    public static String optionsfilepath = txtfiles.getOptionsFilePath();
    
    public final static char openList = '≪';
    public final static char closeList = '≫';
    
    
    public static Object findFunction(String func, Object ... args) { // example: "abc", 2 strings still have quotes. booleans are still bools not ints
        try {
            func = func.toLowerCase();
            switch (func.replace("_", "")) {
                case "":        
                    String toReturn = "";
                    for (Object obj : args) toReturn = toReturn + ","+toStringOrNull(obj);
                    return toReturn.substring(1);
                case "test":        return test();
                case "vertline":    return vertLine(args[0]);
                
                case "abs":         return abs( (Number) args[0]);
                case "nint":
                case "round":       return round( (Number) (args[0]));
                case "floor":       return floor( (Number) args[0] );
                case "ceil":
                case "ceiling":     return ceil( (Number) args[0]);
                case "factorial":   return factorial( (Number) args[0]);
                case "isprime":     return isPrime( Math.round(((Number) args[0]).floatValue()));
                case "primefactorization":
                case "primefactor":
                case "primefactors":
                case "factors":     return primeFactorization( Math.round(((Number) args[0]).floatValue()));
                case "gcd":
                case "gcf":         return GCF(args);
                case "lcm":         return LCM( args);
                case "randomnum":   
                case "random":
                case "randnum":
                case "rand":        return random(args);
                case "randomint":
                case "randint":     return randomInt((Number) args[0], (Number) args[1]);
                
                case "eval":
                case "evaluate":    return evaluate(args[0].toString(),args[1].toString(), numberEval(args[2].toString()));
                case "solve":
                case "solvenew":    return solve(args[0].toString(),args[1].toString());
                case "solveold":    return solveOld(args[0].toString(),args[1].toString());
                //case "solveLin":    return solveLin(args);
                case "lim":
                case "limit":       return limit(args[0].toString(),args[1].toString(), numberEval(args[2].toString()));
                case "narysummation":
                case "narysum":
                case "sum":
                case "∑":           return NarySummation( minQ(args[0].toString()), minQ(args[1].toString()), numberEval(args[2].toString()), numberEval(args[3].toString()));
                case "naryproduct":
                case "naryprod":
                case "product":
                case "∏":           return NaryProduct(minQ(args[0].toString()), minQ(args[1].toString()), numberEval(args[2].toString()), numberEval(args[3].toString()));
                case "Γ":
                case "gamma":
                case "gammafunc":
                case "gammafunction": return gammaFunc((Number) args[0]);

                case "sin":     
                case "sine":        return sin((Number) args[0]);
                case "cos":
                case "cosine":      return cos((Number) args[0]);
                case "tan":
                case "tangent":     return tan((Number) args[0]);
                case "asin": 
                case "arcsin":
                case "sin~1":       return asin((Number) args[0]);
                case "acos":
                case "arccos":
                case "cos~1":       return acos((Number) args[0]);
                case "atan":
                case "arctan":
                case "tan~1":       return atan((Number) args[0]);
              /*  case "torad":       return toRad( (Number) args[0]);
                case "todeg":       return toDeg( (Number) args[0]);
                
                case "triarea":
                case "trianglearea":
                case "heron":       return heron( (Number) args[0], (Number) args[1], (Number) args[2]);
                
                case "√":
                case "sqrt":        return sqrt( (Number) args[0]);
                case "∛":           
                case "cbrt":        return cbrt((Number) args[0]);
                case "∜":           return root( (Number) args[0], 4);
                case "root":        return args.length < 2 ?
                                        root((Number) args[0], 2) :
                                        root( (Number) args[0], (Number) args[1]); */
                case "ln":          return ln((Number) args[0]);
                case "ld":
                case "lb":          return log2((Number) args[0]);
                case "exp":         return exp((Number) args[0]);
                case "expm1":       return expm1((Number) args[0]);
                
                case "avg":
                case "mean":        return mean(args);
                case "median":      return median(args);
                case "mode":        return mode(args);
                case "averagemode":
                case "avgmode":
                case "meanmode":    return avgMode(args);
                case "standarddeviation":
                case "stndev":
                case "stdev":       return standardDeviation((ArrayList<Number>) args[0]);
                case "ncr":
                case "combination": return combination((Number) args[0], (Number) args[1]);
                case "npr":
                case "permutation": return permutation((Number) args[0], (Number) args[1]);
                case "normpdf":     return normPDF(args);
                case "normcdf":     return normCDF(args);
                case "binompdf":    
                case "binomialpdf": return binomPDF(args);
                case "binomcdf":    
                case "binomialcdf": return binomCDF(args);
                case "geompdf":
                case "geometrypdf": return geomPDF(args);
                case "geomcdf":
                case "geometrycdf": return geomCDF(args);
                    
                case "det":
                case "determinant": return det((Object[][]) args[0]);
                case "rowadd":      return rowAdd( (Number[][]) args[0], (Number) args[1], (Number) args[2]);
                case "rowsub":
                case "rowsubtract": return rowSubtract( (Number[][]) args[0], (Number) args[1], (Number) args[2]);
                case "rowmult":
                case "rowmultiply": return rowMultiply( (Number[][]) args[0], toInt(args[1]), (Number) args[2]);
                case "rowdiv":
                case "rowdivide":   return rowDivide( (Number[][]) args[0], toInt(args[1]), (Number) args[2]);
                case "getmatrixcolumn":
                case "getmatrixcol":
                case "getmatcol":   return Arrays.asList(getMatrixCol((Number[][]) args[0], toInt(args[1]) ));
                case "rowsort":     return rowSort( (Number[][]) args[0]);
                case "reverse":     return reverse( (Object[]) args[0] );
                case "rowsortreverse":
                case "rowsortrev":  return rowSortReverse( (Number[][]) args[0]);
                case "rowecholonform":
                case "echelonform":
                case "echelon":
                case "ref":         return ref( (Number[][]) args[0]);
                    
//                case "mag":         return mag((Vector) args[0]);
                    
                case "uni":
                case "unicode":     return unicode(args[0]);
                case "bd":
                case "tobd":
                case "bigdecimal":
                case "tobigdecimal":return toBigDecimal( (Number) args[0]);
                case "tostr":
                case "tostring":    return toString(args[0]);
               // case "tobold":      return toBold(args[0]);
                default:
                    if (func.matches("sine?\\d{1,}"))
                        return Math.pow(sin((Number) args[0]), doubleEval(func.split("sine?",2)[1], 1.));
                    else if (func.matches("cos(ine)?\\d{1,}"))
                        return Math.pow(cos((Number) args[0]), doubleEval(func.split("cos(ine)?",2)[1], 1.));
                    else if (func.matches("tan(gent)?\\d{1,}"))
                        return Math.pow(tan((Number) args[0]), doubleEval(func.split("tan(gent)?",2)[1], 1.));
                  //  else if (func.matches("ro{0,2}t\\d{0,}"))
                    //    return root( (Number) args[0], doubleEval(func.split("ro{0,2}t",2)[1], 2.));
                    else if (func.matches("lo{0,1}g\\d{0,}"))
                        return log( (Number) args[0], doubleEval(func.split("lo{0,1}g",2)[1], 10.));
                    else throw new NoSuchMechanismException("Method name: " + func);
            }
        } catch (IndexOutOfBoundsException ex) {
            String[] exParts = ex.toString().split("IndexOutOfBoundsException");
            throw new IndexOutOfBoundsException("Too few arguments for method: " + exParts[exParts.length-1]);
        } catch (ClassCastException ex) {
            String[] exParts = ex.toString().split("ClassCastException");
            throw new NumberFormatException("Wrong type argument for method: " + exParts[exParts.length-1]);
        }
    }
    public static Object makeObject(String className, Object ... args) {// example: "abc", 2 strings still have quotes, booleans are still bools and not nums
        try{
            switch(className.toLowerCase()) {
                case "string":
                    String all = "";
                    for (int i = 0; i < args.length; i++) all = all + (i==0 ? "" : " ") + toStringOrNull(args[i]);
                    return "\"" + all + "\"";
                default:
                   throw new NoSuchMechanismException("Class name: " + className);
            }
        } catch (IndexOutOfBoundsException ex) {
            String[] exParts = ex.toString().split("IndexOutOfBoundsException");
            throw new IndexOutOfBoundsException("Too few arguments for class: " + exParts[exParts.length-1]);
        } catch (ClassCastException ex) {
            String[] exParts = ex.toString().split("ClassCastException");
            throw new NumberFormatException("Wrong type argument for class: " + exParts[exParts.length-1]);
        }
    }

    
    private static Integer toInt(Object b) {
        if (b instanceof Number) {
            return (int) Math.round(((Number) b).doubleValue());
        }
        return null;
    }
    private static Long toLong(Object b) {
        if (b instanceof Number) {
            return Math.round(((Number) b).doubleValue());
        }
        return null;
    }    
    private static Object test() {
        return "\"hi\""; 
    }
    private static Object vertLine(Object b) {
        String method = "abs";
        if (b instanceof Number) return abs((Number) b);
        else if (b instanceof Object[][]) return det((Object[][]) b);
//        else if (b instanceof Vector) return mag((Vector) b);
        return b;
    }
    
    
    private static Number abs(Number n) {
        if (n == null) return null;
        else if (n instanceof Long) return Math.abs(n.longValue());
        else if (n instanceof Double) return Math.abs(n.doubleValue());
        else if (n instanceof Float) return Math.abs(n.floatValue());
        else if (n instanceof BigDecimal) return ((BigDecimal) n).abs();
        else if (n instanceof Integer) return Math.abs(n.intValue());
//        else if (n instanceof Scalar) return Scalar.abs((Scalar) n);
        return Math.abs(n.doubleValue());
    }       
    private static Number round(Number n) {
//        if (n instanceof Scalar) return ((Scalar) n).round();
        return Math.round(n.doubleValue());
    }
    private static double floor(Number n) { return Math.floor(n.doubleValue()); }
    private static double ceil(Number n) { return (int) Math.ceil(n.doubleValue()); }
    private static Number factorial(Number n) { // double is used for large factorials, can go up 170!
        if (n == null) return Double.NaN;
        try{ Number d = n.doubleValue();
            if (d.floatValue() < 0) throw new UnsupportedOperationException("factorial of  negatives is not supported");;
//            if (n instanceof Scalar) throw new UnsupportedOperationException("factorial of  scalars is not supported");
//            if (Math.abs((d.doubleValue() + 0.5) % 1 - 0.5) > Math.pow(10, -1 * magPrecision)) return gammaFunc(d); //at low n, gammaFunc is not very accurate
            if (d.floatValue() > 1) return (d.floatValue() <= 10) ? Math.round(d.floatValue()) * Math.round(factorial(Math.round(d.floatValue()) - 1).floatValue()): NaryProduct("x","x",1, n);
            if (Math.pow(Math.round( d.floatValue()),2) == Math.round(d.floatValue())) return 1; // d is 0 or 1
        } catch (StackOverflowError ex) { System.out.println("StackOverFlowError at factorial method in functions.java"); }
        return Double.NaN;
    }
    private static boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i < Math.floor(Math.sqrt(num))+1; i++) if (num % i == 0) return false;
        return true;
    }
    private static ArrayList<Integer> primeFactorization(int num) {
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        
        int i;
        for (i = 2; i < Math.round(Math.sqrt(Math.abs(num)))+1; i++) {
            if (Math.abs(num) % i == 0) break;
        }
        if (i < Math.round(Math.sqrt(Math.abs(num)))+1) {
            toReturn.add(i);
            ArrayList<Integer> itsFactors = primeFactorization(Math.abs(num) / i);
            toReturn.addAll(itsFactors);
        } else toReturn.add(num);
        return toReturn;
    }
    private static long GCF(Object[] input) {
        Long[] nums = new Long[input.length];
        for (int i = 0; i < input.length; i++) nums[i] = Math.round(((Number) input[i]).doubleValue());
        long result = nums[0];
        for (int i = 1; i < input.length; i++) {
            if (nums[i] == 0) result = result;
            else result = GCF(new Long[] {nums[i], result % nums[i]});
        }
        return result;
    }
    private static long LCM(Object[] input) { // objs must be group of numbers
        long product = 1;
        for (int i = 0; i < input.length; i++) product *= Math.round(((Number) input[i]).doubleValue());
        return product/GCF(input);
    }
    private static Number random(Object[] input) {// min is inclusive, max is exclusive
        switch (input.length) {
            case 2: return Math.random()
                    * (((Number) input[1]).doubleValue() - ((Number) input[0]).doubleValue())
                    + ((Number) input[0]).doubleValue();
            case 0: 
            default: return Math.random();
        }
    }
    private static long randomInt(Number min, Number max) { // min and max are inclusive
        return (long) Math.floor(Math.random()* (max.doubleValue() - min.doubleValue() + 1)
        + min.doubleValue());
    }
    
    public static Number evaluate(String express, String var, Object val) {
        var = minQ(var);
        express = minQ(express);
//        if (!isValidVarName(var)) return (Number) correctEval(express);
        String valStr = toStringOrNull(val).replaceAll("[Ee]-", "E~");
        if (valStr.startsWith("-")) valStr = "~"+valStr.substring(1);
        express = express.replace(var, "("+valStr+")");
        return (Number) conv2Val(express);
        //return (Number) correctEval(toStringOrNull(conv2Val(express)));
    }
    private static ArrayList<String> getEquationHands(String equation, int numHands) {
        if (!equation.startsWith("\"") || !equation.endsWith("\"")) throw new NumberFormatException("getEquationHands acts on a string that starts with quotes: " + equation.startsWith("\"") + " and ends with quotes: " + equation.endsWith("\""));
        String[] hands = equation.substring(1,equation.length()-1).split("=");
        if (hands.length != numHands)
            throw new NumberFormatException(hands.length <= 1 ? "Not an equation" : "Has an incorrect number("+hands.length+") of sides");
        for (int i = 0; i < numHands; i++) {
            hands[i] = "\"" + hands[i] + "\"";
        }
        return new ArrayList<String>(Arrays.asList(hands));
    }
    public static Double solveOld(String equation, String var) {
        double ans = 0;
        int iterations = 0;
        ArrayList<Double> tries = new ArrayList();
        ArrayList<String> hands = getEquationHands(equation, 2);
        String LH = hands.get(0);
        String RH = hands.get(1);
        double prevLHval = 0, prevRHval = 0, prevDif = 0, LHval = evaluate(LH, var, ans).doubleValue(), RHval = evaluate(RH, var, ans).doubleValue(), dif;
        boolean LHcx = LH.contains(minQ(var)), RHcx = RH.contains(minQ(var));
        while (true) {
            tries.add(ans);
            if (LHcx) LHval = evaluate(LH, var, ans).doubleValue();
            else if (RHcx) RHval = evaluate(RH, var, ans).doubleValue();
            else return null;
            dif = LHval - RHval;
            
            if (Math.abs(dif) <= Math.pow(10, -1*magPrecision)) break;//solve("x+2=3","x")
            while (tries.contains(ans)) {
                if (Double.isInfinite(ans)) break;
                if (Math.signum(dif) != Math.signum(prevDif)) ans -= (dif + dif)/2;
                else ans += dif/2;
            }
            prevLHval = LHval;
            prevRHval = RHval;
            prevDif = dif;
            iterations++;
            System.out.println("ans: " + ans + ",   LHval: " + LHval + ",   RHval: " + RHval + ",   dif: " + dif);
            if (iterations > 10000) return null;
        }
        return ans;
    }
    public static ArrayList<Number> solve(String function, String var) {
        ArrayList<String> hands = getEquationHands(function, 2);
        String LH = hands.get(0);
        String RH = hands.get(1);
        ArrayList<Number> goodInputs = new ArrayList<Number>();
        BigDecimal start = new BigDecimal(-100), end = new BigDecimal(100);
        BigDecimal increase = new BigDecimal(.05), iteration = start;
        BigDecimal LHeval = new BigDecimal(evaluate(LH,var,iteration).doubleValue());
        BigDecimal RHeval = new BigDecimal(evaluate(RH,var,iteration).doubleValue());
        BigDecimal prec = new BigDecimal(Math.pow(10, -1*magPrecision));
        boolean LHcvar = LH.contains(minQ(var)), RHcvar = RH.contains(minQ(var));
        for (; iteration.compareTo(end) <= 0; iteration = iteration.add(increase)) {
            try {
                if (LHcvar) LHeval = new BigDecimal(evaluate(LH,var,iteration).doubleValue());
                else if (RHcvar) RHeval = new BigDecimal(evaluate(RH,var,iteration).doubleValue());
                else break;
            } catch (NullPointerException npe) { continue;}
            BigDecimal dif = LHeval.subtract(RHeval).abs();
            if (dif.compareTo(prec) <= 0) goodInputs.add(iteration.setScale(magPrecision, RoundingMode.HALF_EVEN));
        }
        return goodInputs;
    }
    public static ArrayList<Number> solve(String[] linEquats) {
        return null;
    }
    
    public static Number limit(String expres, String var, Number val) { // not operational yet
        double epsilon = (val.doubleValue() + 1) * Math.pow(10, -1*(magPrecision)+1);
        Number limit;
        try {
            limit = evaluate(expres, var, val);
            if (limit instanceof Double && !((Double) val).isInfinite() && (((Double) limit).isNaN() || ((Double) limit).isInfinite())) throw new Throwable();
            if (limit instanceof Float && !((Double) val).isInfinite() && (((Float) limit).isNaN() || ((Float) limit).isInfinite())) throw new Throwable();
        } catch (Throwable t) {
            limit = (evaluate(expres, var, val.doubleValue() + epsilon).doubleValue() + evaluate(expres, var, val.doubleValue() - epsilon).doubleValue())/2;
        }
        return limit;
    }
    public static Number NarySummation(String expres, String var, Number low, Number high) { // sum E symbol
        Number sum = 0;
        for (int x = Math.round(low.floatValue()); x <= Math.round(high.floatValue()); x++) {
            sum = sum.doubleValue() + evaluate(expres, var, x).doubleValue();
        }
        return sum;
    }
    public static Number NaryProduct(String expres, String var, Number low, Number high) { // product Pi symbol
        Number product = 1;
        for (int x = Math.round(low.floatValue()); x <= Math.round(high.floatValue()); x++) {
            product = product.doubleValue() * evaluate(expres, var, x).doubleValue();
        }
        return (Number) correctEval(toStringOrNull(product));
    }
    public static Number gammaFunc(Number n) { // at low n (less than about 4), there is a noticeable error
        double t = n.doubleValue();
        if (t <= 0) throw new UnsupportedOperationException("Gamma of non positive integers not supported");
        if (Math.abs(Math.round(t) - t) < Math.pow(10, -1*magPrecision))
            return factorial(Math.round(t)-1);
        long max = Math.max( (long) (t*5+1), 10);
        int segmentCnt = 10000000; // should be 10000000
        double segmentLn = (max)/ (double) segmentCnt;
        double toReturn = 0;
        for (int x = 1; x < segmentCnt; x++) {
            toReturn += Math.pow(x * segmentLn, t-1) * Math.exp(-1*x * segmentLn);
        }
        return segmentLn * 1.0 * Math.round(toReturn * segmentCnt/1) / segmentCnt;
    }
    
        public static boolean printInRad() { return false; }
    public static boolean printInDeg() { return true; }
    
    /*public static Number toRad(Number inDeg) { 
        if (inDeg instanceof Angle) return new Scalar(((Angle) inDeg).doubleValue(), Arrays.asList("rad"));
        else return !printInRad() ? Math.toRadians(inDeg.doubleValue()) : inDeg; 
    }
    public static Number toDeg(Number inRad) {
        if (inRad instanceof Angle) return new Scalar(((Angle) inRad).doubleValue(), Arrays.asList("deg"));
        else return printInRad() ? Math.toDegrees(inRad.doubleValue()) : inRad;
    } */
    public static double sin(Number angle) {
        if (!printInRad()) angle = Math.toRadians(angle.doubleValue());
        return Math.sin(angle.doubleValue());
    }
    public static double cos(Number angle) {
        if (!printInRad()) angle = Math.toRadians(angle.doubleValue());
        return Math.cos(angle.doubleValue());
    }
    public static double tan(Number angle) {
        if (!printInRad()) angle = Math.toRadians(angle.doubleValue());
        return Math.tan(angle.doubleValue());
    }    
    public static double asin(Number ratio) {
        double angle = Math.asin(ratio.doubleValue());
        if (!printInRad()) angle = Math.toDegrees(angle);
        return angle;
    }
    public static double acos(Number ratio) {
        double angle = Math.acos(ratio.doubleValue());
        if (!printInRad()) angle = Math.toDegrees(angle);
        return angle;
    }
    public static double atan(Number ratio) {
        double angle = Math.atan(ratio.doubleValue());
        if (!printInRad()) angle = Math.toDegrees(angle);
        return angle;
    }
    
   /* private static Scalar heron(Number an, Number bn, Number cn) {
        Scalar a = Scalar.valueOf(an); Scalar b = Scalar.valueOf(bn); Scalar c = Scalar.valueOf(cn); 
        Scalar s = a.add(b).add(c).divide(2);
        Scalar area = s.multiply(s.subtract(a)).multiply(s.subtract(b)).multiply(s.subtract(c)).sqrt();
        return area;
    } */
    
/*    private static Object sqrt(Number radicand) {
        if (!(radicand instanceof Scalar)) return Math.sqrt( radicand.doubleValue() );
        else return ((Scalar) radicand).sqrt();
    }
    private static Object cbrt(Number radicand) {
        return root(radicand, 3);
    }
    private static Object root(Number radicand, Number radix) {
        if (!(radicand instanceof Scalar)) return Math.pow(radicand.doubleValue(), 1.0/radix.doubleValue());
        else return ((Scalar) radicand).root(Math.round(radix.floatValue())); 
    }*/
    public static double ln(Number num) { return Math.log(num.doubleValue()); }
    public static double log2(Number num) { return log(num, 2); }
    public static double log10(Number num) { return Math.log10(num.doubleValue()); }
    public static double log(Number num, Number base) { return log10(num)/log10(base); }
    public static double exp(Number num) { return Math.exp(num.doubleValue()); }
    public static double expm1(Number num) { return Math.expm1(num.doubleValue()); }
    
    private static double mean(Object[] input) {
        double sum = 0;
        for (int i = 0; i < input.length; i++) sum += ((Number) input[i]).doubleValue();
        return sum / input.length;
    }
    private static Object median(Object[] input) {
        Arrays.sort(input);
        if (input.length == 0) return null;
        else return input.length % 2 == 1 ? input[input.length/2] : mean(new Object[] {input[input.length/2 - 1], input[input.length/2]}); // both ints so length/2 floors down
    }
    private static ArrayList<Object> mode(Object[] input) {
        ArrayList data = new ArrayList(Arrays.asList(input));
        Set<Object> uniques = new HashSet<Object>(data);
        ArrayList<Object> modes = new ArrayList<Object>();
        int freq = 0;
        for (Object b : uniques) {
            int n_freq = Collections.frequency(data, b);
            if (n_freq > freq) {
                modes = new ArrayList();
                modes.add(b);
                freq = n_freq;
            } else if (n_freq == freq) {
                modes.add(b);
            }
        }
        return modes;
    }
    private static Number avgMode(Object[] input) { return mean(mode(input).toArray()); }
    private static double standardDeviation(ArrayList<Number> data) {
        double mean = mean(data.toArray());
        ArrayList<Number> sqDif = new ArrayList<Number>();
        for (int i = 0; i < data.size(); i++) { sqDif.add( Math.pow(mean - data.get(i).doubleValue(),2));}
        double sqDifMean = mean(sqDif.toArray());
        return Math.sqrt(sqDifMean);
    }
    public static long combination(Number n, Number k) {
       if (k.doubleValue() > n.doubleValue()) return 0;
       double kFc = factorial(k).doubleValue();
       return (long) (permutation(n,k)/kFc);

    }
    public static long permutation(Number n, Number k) {
       long nl = n.longValue(), kl = k.longValue();
       double answer = 1;
       for (long i = nl; i > nl-kl; i--) answer *= i;
       return (long) answer;
    }
    public static double normPDF(Object[] args) {
       Number mean = 0;
       Number SD = 1;
       if (args.length > 1) mean = (Number) args[0];
       if (args.length > 2) SD = (Number) args[1];
       Number point = (Number) args[2];
       double adjPoint = (point.doubleValue()-mean.doubleValue())/SD.doubleValue();
       double constant = Math.sqrt(tau)*SD.doubleValue();
       double varPart = Math.expm1(-0.5*Math.pow(adjPoint,2))+1;
       return varPart/constant;
    }
    public static double normCDF(Object[] args) { // not working
       Number mean = 0;
       Number SD = 1;
       Number lowBound, highBound;
       if (args.length > 3) {
           mean = (Number) args[0];
           SD = (Number) args[1];
           lowBound = (Number) args[2];
           highBound = (Number) args[3];
       } else {
            lowBound = (Number) args[0];
           highBound = (Number) args[1];
       }
       double min = lowBound.doubleValue(), max = highBound.doubleValue();
       //min = (lowBound.doubleValue() - mean.doubleValue())/SD.doubleValue();
       //max = (highBound.doubleValue() - highBound.doubleValue())/SD.doubleValue();
       int segmentCnt = 10000000; // should be 10000000
       double segmentLn = (max-min)/segmentCnt;
       double toReturn = 0;
       for (int i = 1; i < segmentCnt; i++) {
           toReturn += normPDF(new Object[] {mean, SD, min + i*segmentLn});
       }
       return segmentLn * 10.0 * Math.round(toReturn * segmentCnt/10) / segmentCnt;
    }
    public static double binomPDF(Object[] args) {
        Number numTrials = (Number) args[0];
        double prob = ((Number) args[1]).doubleValue();
        if (prob < 0 || prob > 1) throw new ClassCastException("Probability (" + args[1] + ") is not between 0 and 1 inclusive");
        Number x = (Number) args[2];
        long coeff = combination(numTrials, x);
        return coeff * Math.pow(prob, x.doubleValue()) * Math.pow(prob, numTrials.doubleValue() - x.doubleValue());
    }
    public static double binomCDF(Object[] args) {
        Number numTrials = (Number) args[0];
        double prob = ((Number) args[1]).doubleValue();
        if (prob < 0 || prob > 1) throw new ClassCastException("Probability (" + args[1] + ") is not between 0 and 1 inclusive");
        long low = Math.round(((Number) args[2]).doubleValue()), high = Math.round( ((Number) args[3]).doubleValue() );
        double toReturn = 0;
        for (long i = low; i <= high; i++) {
            try {
                toReturn += binomPDF(new Object[] {numTrials, prob, i});
            } catch (ClassCastException ex) {}
        }
        return toReturn;
    }
    public static double geomPDF(Object[] args) {
        double prob = ((Number) args[0]).doubleValue();
        long x = Math.round(((Number) args[1]).doubleValue());
        if (prob < 0 || prob > 1) throw new ClassCastException("Probability (" + args[1] + ") is not between 0 and 1 inclusive");
        return prob * Math.pow(1-prob, --x);
    }
    public static double geomCDF(Object[] args) {
        double prob = ((Number) args[0]).doubleValue();
        long min = Math.round(((Number) args[1]).doubleValue()), max = Math.round(((Number) args[2]).doubleValue());
        if (prob < 0 || prob > 1) throw new ClassCastException("Probability (" + args[1] + ") is not between 0 and 1 inclusive");
        double toReturn = 0;
        for (long i = min; i <= max; i++) {
            try {
                toReturn += geomPDF(new Object[] {prob, i});
            } catch (ClassCastException ex) {}
        }
        return toReturn;
    }
    
    public static Object det(Object[][] matrix) { // only supports up to 3 by 3
        String toCalc = "";
        Integer sideLength = matrix.length;
        for (int row = 0; row < sideLength; row++)
            if (matrix[row].length != sideLength) throw new NumberFormatException("Not a square matrix");
        String opA = "+", opM = "*", opS = "-";
        if (sideLength == 0) {
            return Double.NaN;
        } if (sideLength == 1) {
            toCalc = toStr(matrix[0][0]);
            return conv2Val(toCalc);
        } else if (sideLength == 2) {
            toCalc = "("+matrix[0][0] + opM + matrix[1][1] + ")" + opS + "("+ matrix[0][1] + opM + matrix[1][0] +")";
            return conv2Val(toCalc);
        } else if (sideLength == 3) {
            toCalc = "(";
            for (int startCol = 0; startCol < sideLength; startCol++) {
                if (startCol != 0) toCalc = toCalc + opA;
                toCalc = toCalc + "(1";
                for (int i = 0; i < sideLength; i++) {
                    toCalc = toCalc + opM + matrix[i % sideLength][(i + startCol) % sideLength];
                }
                toCalc = toCalc + ")";
            } //det([[1,2,3][4,5,6][7,8,9]])
            toCalc = toCalc + ")" + opS + "(";
            for (int startCol = 0; Math.abs(startCol) < sideLength; startCol++) {
                if (startCol != 0) toCalc = toCalc + opA;
                toCalc = toCalc + "(1";
                for (int row = 0; Math.abs(row) < sideLength; row--) {
                    toCalc = toCalc + opM + matrix[(-1 * row) % sideLength][(sideLength * 5 + row + startCol) % sideLength];
                }
                toCalc = toCalc + ")";
            }
            toCalc = toCalc + ")";
            return conv2Val(toCalc);
        } else { // sideLength greater than 3
            
        }
        return Double.NaN;
    }
    // row operations
    public static Number[][] rowAdd(Number[][] matrix, Number addFrom, Number addTo) {
        Number[] rowFrom = matrix[Math.round(addFrom.floatValue())], rowTo = matrix[Math.round(addTo.floatValue())];
        if (rowFrom.length != rowTo.length) throw new NumberFormatException("Row lengths not the same");
        Number[] sum = new Number[rowFrom.length];
        for (int col=0; col < rowFrom.length; col++) {
            sum[col] = rowFrom[col].doubleValue() + rowTo[col].doubleValue();
        }
        matrix[Math.round(addTo.floatValue())] = sum;
        return matrix;
    }
    public static Number[][] rowSubtract(Number[][] matrix, Number addFrom, Number addTo) {
        Number[] rowFrom = matrix[Math.round(addFrom.floatValue())], rowTo = matrix[Math.round(addTo.floatValue())];
        if (rowFrom.length != rowTo.length) throw new NumberFormatException("Row lengths not the same");
        Number[] sum = new Number[rowFrom.length];
        for (int col=0; col < rowFrom.length; col++) {
            sum[col] = rowFrom[col].doubleValue() - rowTo[col].doubleValue();
        }
        matrix[Math.round(addTo.floatValue())] = sum;
        return matrix;
    }
    public static Number[][] rowMultiply(Number[][] matrix, int rowNum, Number multiplier) {
        Number[] rowFrom = matrix[rowNum];
        Number[] sum = new Number[rowFrom.length];
        for (int col=0; col < rowFrom.length; col++) {
            sum[col] = rowFrom[col].doubleValue() * multiplier.doubleValue();
        }
        matrix[rowNum] = sum;
        return matrix;
    }
    public static Number[][] rowDivide(Number[][] matrix, int rowNum, Number divisor) {
        Number[] rowFrom = matrix[rowNum];
        Number[] sum = new Number[rowFrom.length];
        for (int col=0; col < rowFrom.length; col++) {
            sum[col] = rowFrom[col].doubleValue() / divisor.doubleValue();
        }
        matrix[rowNum] = sum;
        return matrix;
    }
    public static Number[] getMatrixCol(Number[][] matrix, int colNum) {
        Number[] col = new Number[matrix.length];
        for (int rowNum = 0; rowNum < matrix.length; rowNum++) {
            col[rowNum] = matrix[rowNum][colNum];
        }
        return col;
    }
    public static Number[][] rowSort(Number[][] matrix) { // not fully functional
        if (matrix.length == 0) return matrix;
        if (matrix[0].length == 0) return matrix;
        ArrayList<Integer> first = new ArrayList(Arrays.asList(getMatrixCol(matrix, 0)));
        Collections.sort(first);
        Number[][] retMatrix = new Number[matrix.length][matrix[0].length];
        for (Number[] row : matrix) {
            int rowLev = first.indexOf(row[0]);
            while (retMatrix[rowLev][0] != null) {
                rowLev++;
            }
            retMatrix[rowLev] = row;
        }
        return retMatrix;
    }
    public static Object[] reverse(Object[] array) {
        for (int i = 0; i < array.length/2; i++) {
            Object temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
        return array;
    }
    public static Number[][] rowSortReverse(Number[][] matrix) {
        matrix = rowSort(matrix);
        return (Number[][]) reverse(matrix);
    }
    public static Number[][] ref(Number[][] matrix) {
        int rowTot = matrix.length;
        if (rowTot == 0) return matrix;
        int colTot = matrix[0].length;
        if (colTot == 0) return matrix;
        matrix = rowSortReverse(matrix);
        for (int colNum = 0; colNum < matrix[0].length; colNum++) {
            if (colNum >= rowTot) break;
            Number pivot = matrix[colNum][colNum];
            if (abs(pivot).doubleValue() < Math.pow(10,-1*magPrecision)) continue;
            for (int rowNum = colNum+1; rowNum < rowTot; rowNum++) {
                matrix = rowMultiply(matrix, rowNum, pivot.doubleValue() / matrix[rowNum][colNum].doubleValue());
                matrix = rowSubtract(matrix, colNum, rowNum);
            }
            matrix = rowDivide(matrix, colNum,pivot);
        }
        return matrix;
    }
    
    /*private static Scalar mag(Vector v) {
        return v == null ? null : v.getMagnitude();
    }/**/
    
    private static Object unicode(Object b) {
        if (b instanceof Number) {
            return "\"" + String.valueOf((char) Math.round(((Number) b).floatValue())) + "\"";
        } else if (b instanceof Character || (b instanceof String && minQ(((String) b)).length() == 1)) {
            Character c;
            if (b instanceof Character) c = (Character) b;
            else c = minQ(((String) b)).charAt(0);
            return (int) c;
        }
        return null;
    }
    private static BigDecimal toBigDecimal(Number b) {
        return new BigDecimal(b.doubleValue());
    }
    private static String toString(Object b) { // this return the object as a string and starting and ending with quotes
        String str = "";
        if (b instanceof String && ((String) b).startsWith("\"") && ((String) b).endsWith("\"")) {
            str = toStr(b);
            str = str.substring(1, str.length()-1);
        }// else if (b != null && b instanceof Scalar) str = b.toString().trim();
        else str = toStr(b);
        return "\"" + str + "\"";
    }
    /*private static String toBold(Object b) {
        return "\""+toBold(minQ(toStr(b)))+"\"";
    }*/
    public static boolean addMutable(String name, Object val) {
        try {
            Number valNum = val instanceof Number ? (Number) val : null;
            if (valNum == null) return false;
            switch (name) {
               // case "time":            return setTime(valNum.doubleValue());
                case "magprec":         return setMagPrecision(valNum.shortValue());
                case "angprec":         return setAngPrecision(valNum.shortValue());
                case "angtype":         return setAngleType(toLong(valNum));
                case "consolefont":     return setConsoleFont(toLong(valNum));
                    
                case "folpemdas":       return setFollowsPEMDAS(toLong(valNum));
                case "showexp":         return setShowExp(toLong(valNum));
                case "showinpi":        return setShowInPi(toLong(valNum));
                default:                return false;
            }
        } catch (ClassCastException | NullPointerException ex) {
            System.out.println("Wrong value for mutable: " + ex); 
            return false;
        }
    }
    public static boolean addConst(String name, Object val) {
        if (!isValidVarName(name)) throw new NumberFormatException("Invalid variable name");
        
        userConsts.put(name, val);
        return true;
    }
    public static boolean addBool(String name, Object val) {
        if (!isValidVarName(name)) throw new NumberFormatException("Invalid variable name");
        if (addMutable(name, val)) return true;
        if (val instanceof Boolean) addBool (name, (Boolean) val);
        else userBooleans.put(name, ((Number) val).doubleValue());
        return true;
    }
    public static boolean addBool(String name, Boolean val) {
        if (!isValidVarName(name)) throw new NumberFormatException("Invalid variable name");
        if (addMutable(name, val)) return true;
        userBooleans.put(name, (double) bool2Int(val));
        return true;
    }
    public static boolean addInt(String name, Long val) {
        if (!isValidVarName(name)) throw new NumberFormatException("Invalid variable name");
        if (addMutable(name, val)) return true;
        userIntegers.put(name, val);
        return true;
    }
    public static boolean addBigDecimal(String name, Number val) {
        return addBigDecimal(name, BigDecimal.valueOf(val.doubleValue()));
    }
    public static boolean addBigDecimal(String name, BigDecimal val) {
        if (!isValidVarName(name)) throw new NumberFormatException("Invalid variable name");
        if (addMutable(name, val)) return true;
        userBigDecimal.put(name, val);
        return true;
    }
    public static boolean addMatrix(String name, Number[][] val) {
        if (!isValidVarName(name)) throw new NumberFormatException("Invalid variable name");
        if (addMutable(name, val)) return true;
        userMatrices.put(name, val);
        return true;
    }
    public static boolean addDouble (String name, Double val) {
        if (!isValidVarName(name)) throw new NumberFormatException("Invalid variable name");
        if (addMutable(name, val)) return true;
        //if (name.equals("time")) { return addScalar(name, new Scalar(val,Arrays.asList("s")));}
        userDoubles.put(name, val);
        return true;
    }
    /*public static boolean addScalar(String name, Scalar val) {
       if (!isValidVarName(name)) throw new NumberFormatException("Invalid variable name");
       if (addMutable(name, val)) return true;
       //if (name.equals("time")) setTime(val);
       userScalars.put(name, val);
       return true;
    } /**/
    public static Object getConst(String name) {
        if (!isValidVarName(name)) throw new NumberFormatException("Invalid variable name");
        return userConsts.get(name);
    }
    public static Boolean getBool(String name) { // no bool const allowed
        Object c = getConst(name);
        return c instanceof Boolean ? (Boolean) c : isNumericalTrue(userBooleans.get(name));
    }
    public static Long getInt(String name) {
        Object c = getConst(name);
        return c instanceof Long ? (Long) c: userIntegers.get(name);
    }
    public static BigDecimal getBigDecimal(String name) {
        Object c = getConst(name);
        return c instanceof BigDecimal ? (BigDecimal) c: userBigDecimal.get(name);
    }
    public static Double getDouble(String name) {
        Object c = getConst(name);
        return c instanceof Double ? (Double) c: userDoubles.get(name);
    }
  /*  public static Scalar getScalar(String name) {
        Object c = getConst(name);
        if (c instanceof Scalar) return (Scalar) c;
        Number toRet = userScalars.get(name);
        return toRet instanceof Scalar ? (Scalar) toRet : new Scalar(toRet.doubleValue());
    }*/
    public static Number[][] getMatrix(String name) { // no const matrix allowed
        if (!isValidVarName(name)) throw new NumberFormatException("Invalid variable name");
        Object c = getConst(name);
        return c instanceof Number[][] ? (Number[][]) c: userMatrices.get(name);
    }
    
    public static boolean setAngleType(Number value) {
        userIntegers.put("angtype", toLong(value));
        angleType = value.byteValue();
        return true;
    }
    /*public static boolean setTime(double value) { return setTime(new Scalar(value, Arrays.asList("s"))); }
    public static boolean setTime(Scalar value) {
        userDoubles.put("time", value.doubleValue());
        setTime(value);
        return true;
    }/**/

    public static boolean setConsoleFont(long value) {
        userIntegers.put("consolefont", value);
        //Displayer.consolefont = value;
        return true;
    }
    public static boolean setFollowsPEMDAS(long value) {
        userIntegers.put("folpemdas", value);
        folPEMDAS = isNumericalTrue(value);
        return true;
    }
    public static boolean setShowExp(long value) {
        userIntegers.put("showexp", value);
        showExp = isNumericalTrue(value);
        return true;
    }
    public static boolean setShowInPi(long value) {
        userIntegers.put("showinpi", value);
        showInPi = isNumericalTrue(value);
        return true;
    }
    public static boolean setMagPrecision(short value) {
        userIntegers.put("magprec", new Long(value) );
        magPrecision = value;
        return true;
    }
    public static boolean setAngPrecision(short value) {
        userIntegers.put("angprec", new Long(value) );
        anglePrecision = value;
        return true;
    }
    public static int parenthDif(String input) {
        int toReturn = 0;
        for (int i = 0; i < input.length(); i++){
            if (input.charAt(i) == '(' && (i == 0 || input.charAt(i-1) != '\\')) toReturn++;
            else if (input.charAt(i) == ')'&& (i == 0 || input.charAt(i-1) != '\\')) toReturn--;
        }
        return toReturn;
    }
    public static int curlyhDif(String input) {
        int toReturn = 0;
        for (int i = 0; i < input.length(); i++){
            if (input.charAt(i) == '{' && (i == 0 || input.charAt(i-1) != '\\')) toReturn++;
            else if (input.charAt(i) == '}'&& (i == 0 || input.charAt(i-1) != '\\')) toReturn--;
        }
        return toReturn;
    }
    
    public static boolean isTrue(Object b) {
        if (b instanceof Boolean) return (Boolean) b;
        else if (b instanceof Number) return isNumericalTrue((Number) b);
        else if (b instanceof String) return Boolean.valueOf((String) b);
        return false;
    }
    public static boolean isNumericalTrue(Number d) { return d == null ? false : d.doubleValue() >= 0; }
    public static int double2Bool2Int(Number d) { return bool2Int(isNumericalTrue(d)); }
    public static int bool2Int(boolean b) {
        if (b) return 1;
        else return -1;
    }
    public static final int TRUE = 1, FALSE = -1;
    public static String opCorrecter(String input) {        
        String o_input = input + " not the same";
        //if (true) return input;
        while (!o_input.equals(input)) {
            o_input = input;
            
            input = input.replaceAll("[oO][rR]", "∨");
            input = input.replaceAll("[aA][nN][dD]", "∧");
            
            input = input.replace("⁻", "~");
            input = input.replace("−", "-");
            //input = input.replace("~~", "+");
            input = input.replace("--", "+");
            //input = input.replace("~+", "-");
            input = input.replace("-+", "-");
            //input = input.replace("+~", "-");
            input = input.replace("+-", "-");
            input = input.replace("++", "+");
            
            input = input.replaceAll("[\\•\\×]", "\\*");
            input = input.replace("÷", "/");
            input = input.replace("//", "*");
            input = input.replace("R%", remSymb+"");
            input = input.replace("=>", "►");
            
            input = input.replace("||", "∨");
            input = input.replace(">=", "≤");
            input = input.replace("<=", "≥");
            input = input.replaceAll("===", "⩶");
            input = input.replaceAll("==", "⩵");
            input = input.replace("=!=", "≠");
            input = input.replaceAll("<<<", "⋘");
            input = input.replaceAll("<<", "≪");
            input = input.replaceAll(">>>", "⋙");
            input = input.replaceAll(">>", "≫");
            
            input = input.replaceAll(closeList+"U", closeList+"∪");
            input = input.replaceAll("U"+openList, "∪"+openList);//'∩'
            input = input.replaceAll(closeList+"I"+openList, closeList+"∩"+openList);
            
            input = fixVar(input, euler+"","euler","e", euler + "uler");
            input = fixVar(input, pi + "","pi");
            input = fixVar(input, tau + "","tau");
            input = fixVar(input, phi + "","phi");
        }
        return input;
    }
    private static String fixVar(String input, String replacement, String ... name) {//name is last
        if (name.length == 1) {
            if (!input.contains(name[0])) return input;
            input = "+" + input;
            for (char op : opLevs.get(0)) input = (input).replaceAll("\\Q"+op + name[0]+"\\E", op + replacement);
            input = input.substring(1) + "+";
            for (char op : opLevs.get(0)) input = (input).replaceAll("[^(a-zA-Z)]\\Q"+name[0] + op+"\\E", replacement + op);
            input = input.substring(0, input.length() - 1);
        } else for (int i = 0; i < name.length; i++) input = fixVar(input, replacement, name[i]);
        return input;
    }
    private static boolean isValidVarName(String name) { return isValidVarName(name); }
    public static Double boolEval(String str) {
        str = str.replace("~", "-");
        str = str.replace("true", Math.round(bool2Int(true))+"");
        str = str.replace("false", Math.round(bool2Int(false))+"");
        return Double.valueOf(str);
    }
    public static Object correctEval(String str) {
        Object toReturn = null;
        if (str == null) return null;
        if (str.equals(cNoReturn)) return cNoReturn;
     /*   else if (str.startsWith("[[") && str.endsWith("]]") && freqOfMetaInString(str, "[[") == freqOfMetaInString(str, "]]") && !str.contains("],") && !str.contains(",[")) {
            toReturn = matrixEval(str);
        }*/ else if (str.startsWith(openList+"") && str.endsWith(closeList+"") && freqOfMetaInString(str, openList+"") == freqOfMetaInString(str, closeList+"")) {
            toReturn = listEval(str);
   //     } else if (str.startsWith(Vector.openVec) && str.endsWith(Vector.closeVec)) {
     //       toReturn = vectorEval(str);
  //      } else if (!str.startsWith("[") && str.contains("[") && str.endsWith("]")) {
    //        toReturn = scalarEval(str);
        } else if (str.toLowerCase().endsWith(bigDecimalExt.toLowerCase()) && str.length() > 2) {
            toReturn = BigDecimalEval(str.substring(0,str.length()-2));
   //     } else if (str.startsWith("{") && str.endsWith("}") && freqOfMetaInString(str, "{") == freqOfMetaInString(str, "}")) {
     //       toReturn = curlyBraceEval(str);
        } else try {
                toReturn = doubleEval(str);
                toReturn = intEval(str);
            } catch (NumberFormatException ex) {
                if (toReturn == null ) {
                    if (str.startsWith("\"")) toReturn = stringEval(str);
       /*             else if (str.startsWith(Polynomial.openPoly + "") && str.endsWith(Polynomial.closePoly + "")) {
                        toReturn = polyEval(str);
                    } else {
                        if (!str.startsWith("'")) str = "'" + str + "'";
                        toReturn = monomialEval(str);
                    }*/
                }
            }
        return toReturn;
    }
    public static Long intEval(String str) {
        if (str == null) return null;
        List numbers = Arrays.asList("zero","one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
                "ten", "eleven","twelve","thirteen","fourteen","fifteen","sixteen","seventeen","eighteen","nineteen","twenty");
        if (numbers.contains(str.toLowerCase())) return (long) numbers.indexOf(str.toLowerCase());
        return Long.valueOf(str.replace("~", "-"));
    }
    public static BigDecimal BigDecimalEval(String str) {
        if (str == null) return null;
        return BigDecimal.valueOf(doubleEval(str));
    }
    public static Float floatEval(String str) {
        if (str == null) return null;
        return Float.valueOf(str.replace("~", "-"));
    }
    public static Double doubleEval(String str) {
        return doubleEval(str, 0.);
    }
    public static Double doubleEval(String str, Double defaultDub) {
        if (str == null) return null;
        if (str.trim().length() == 0) return defaultDub;
        if (str.length() > 0)
            switch (str.toLowerCase().replaceAll("[\\_\\ ]", "").replace("~", "-")) {
                case Infinity:
                case "infinity":
                case "positiveinfinity":
                case "inf":
                case "+inf":
                case "posinf": return Double.POSITIVE_INFINITY;
                
                case NegativeInfinity:
                case "negativeinfinity":
                case "-inf":
                case "neginf": return Double.NEGATIVE_INFINITY;
                
                case pi+"": return Math.PI;
                case euler+"": return Math.E;
                case tau+"": return tau;
                case phi+"": return 0.5 + Math.sqrt(5.0)/2.0;
                default:
                    List numbers = Arrays.asList("zero","one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
                    "ten", "eleven","twelve","thirteen","fourteen","fifteen","sixteen","seventeen","eighteen","nineteen","twenty");
                    if (numbers.contains(str.toLowerCase())) return (double) numbers.indexOf(str.toLowerCase());
            }
        str = str.replace("~", "-");
        return Double.valueOf(str);
    }
   // private static Scalar scalarEval(String str) { return scalarEval(str); }
    public static String stringEval(String str) {
        // str should start and end with a quotation mark
        if (str == null) return null;
        if (str.equals(cNoReturn)) return str;
        if (str.startsWith("\"") && str.endsWith("\""))
            return str;//return str.substring(1, str.length()-1);
        else 
            return str;
            //WARNING ON JUL 24, I (RAHUL) COMMENTED THIS OUT WITHOUT LOOKING AT THE CONSEQUENCES throw new NumberFormatException("String at stringEval does not start ("+str.startsWith("\"")+") and end ("+str.endsWith("\"")+") with quotes");
    }
    //private static Monomial monomialEval(String str) /*str should start and end with a quotation mark*/ { return monomialEval(str); }
    public static Number[][] matrixEval(String str) { return matrixEval(str); }
    public static ArrayList<Object> listEval(String str) { return listEval(str); }
    private static Number numberEval(String str) { return numberEval(str); }
    
    public static int freqInString(String data, String lookingFor) {
        int length = lookingFor.length();
        int freq = 0;
        for (int i = 0; i + length - 1< data.length(); i++) {
            String part = data.substring(i, i + length);
            if (part.equals(lookingFor)) freq++;
        }
        return freq;
    }
    public static int freqOfMetaInString(String data, String meta) {
        int freq = 0;
        for (int begin = 0; begin + meta.length() <= data.length(); begin++) {
            if (
                    data.substring(begin, begin + meta.length()).equals(meta)
                    && (begin == 0 || freqOfMetaInString(data.substring(Math.max(0,begin-2),begin), "\\") % 2 == 0)
                ) {
                freq++;
            }
        }
        return freq;
    }
    private static String concatString(String addend, String augend) {
        if (addend.startsWith("\"") && augend.startsWith("\"") && addend.endsWith("\"") && augend.endsWith("\"")) {
            addend = addend.substring(1, addend.length()-1);
            augend = augend.substring(1, augend.length()-1);
            return "\"" + addend + augend + "\"";
        } else throw new NumberFormatException("strings don't start and end with quotes at concatString: " + addend +", " + augend);
    }
    /* private static String concatMonomial(String addend, String augend) {
        if (addend.startsWith("'") && augend.startsWith("'") && addend.endsWith("'") && augend.endsWith("'")) {
            addend = addend.substring(1, addend.length()-1);
            augend = augend.substring(1, augend.length()-1);
            return "'" + addend + augend + "'";
        } else throw new NumberFormatException("strings don't start and end with quotes at concatMonomial: " + addend +", " + augend);
    } //*/
    public static ArrayList<String> splitGroupVals(String input, int i) {
        char[] separators = new char[1]; // when adding new separator add to the specific case and case 0
        separators = opLevs.get(i); //added in code shortening 0 
        return new ArrayList(betterSplit(input, separators)); 
    }
    public static  ArrayList<Integer> indsAtOperand(String input, int lev) {
        String regex = "[\\Q"; // when adding new separator add to the specific case and case 0
        char[] separators = opLevs.get(lev);
        for (int i = 0; i < separators.length; i++) regex = regex + separators[i];
        return indsAtOperand(input, Pattern.compile(regex + "\\E]"));
    }
    public static  ArrayList<Integer> indsAtOperand(String input, Pattern pat) {
        Matcher matcher = pat.matcher(input);
        ArrayList<Integer> positions = new ArrayList();
        while (matcher.find()) {
            positions.add(matcher.start());
        }
        return positions;
    }
    
        public static String[] commandSplitting(String line) {
        int pLev, brackLev, curlyLev, absLev,  qLev, apostLev, dubComp;
        pLev = brackLev = curlyLev = absLev = qLev = apostLev = dubComp = 0;
        int lastSplitPoint = 0;
        ArrayList<String> commands = new ArrayList<>();
        line = line.replace("<<", "≪").replace(">>", "≫");
        for (int i = 0; i < line.length(); i++) {
            switch (line.charAt(i)) {
                case '\\':
                    if (i < line.length() - 1) line = line.substring(0, i) + line.substring(i+1);
                    break;
                case '(':
                    pLev++;
                    break;
                case ')':
                    pLev--;
                    break;
                case '[':
                    brackLev++;
                    break;
                case ']':
                    brackLev--;
                    break;
                case '{':
                    curlyLev++;
                    break;
                case '}':
                    curlyLev--;
                    break;
                case '≪':
                    dubComp++;
                    break;
                case '≫':
                    dubComp--;
                    break;
                case '|':
                    absLev = Math.abs(qLev - 1);
                    break;
                case '"':
                    qLev = Math.abs(qLev - 1);
                case '\'':
                    apostLev = Math.abs(apostLev - 1);
                    break;
            }
            if (pLev == 0 && brackLev == 0 && curlyLev == 0 && qLev == 0 && apostLev == 0 && absLev == 0 && dubComp == 0 && line.charAt(i) == ' ') {
                commands.add(line.substring(lastSplitPoint,i));
                lastSplitPoint = i+1;
            }
        }
          commands.add(line.substring(lastSplitPoint));
          String[] toReturn = new String[commands.size()];
          for (int i = 0; i < toReturn.length; i++) {
              toReturn[i] = commands.get(i);
          }
          return toReturn;
    }
    public static ArrayList<String> betterSplit(String text, char[] splitter) { 
        // if ur changing this, change commandSplitting in Displayer also
        text = text.trim();
        ArrayList<String> toReturn = new ArrayList<>();
        int lastSplitPoint = 0;
        int pLev, brackLev, curlyLev, qLev, apostLev, absLev, dubComp;
        pLev = brackLev = curlyLev = qLev = apostLev = absLev = dubComp = 0;
        if(text.isEmpty()) return toReturn;
        text = text.replace("<<", "≪").replace(">>", "≫");
        for (int i = 0; i < text.length(); i++) {
            switch (text.charAt(i)) {
                case '\\':
                    if (i < text.length() - 1) text = text.substring(0, i) + text.substring(i+1);
                    break;
                case '(':
                    pLev++;
                    break;
                case ')':
                    pLev--;
                    break;
                case '[':
                    brackLev++;
                    break;
                case ']':
                    brackLev--;
                    break;
                case '{':
                    curlyLev++;
                    break;
                case '}':
                    curlyLev--;
                    break;
                case '≪':
                    dubComp++;
                    break;
                case '≫':
                    dubComp--;
                    break;
                case '|':
                    absLev = Math.abs(qLev - 1);
                    break;
                case '"':
                    qLev = Math.abs(qLev - 1);
                case '\'':
                    apostLev = Math.abs(apostLev - 1);
                    
            }
            if (pLev == 0 && brackLev == 0 && curlyLev == 0 && qLev == 0 && apostLev == 0 && dubComp == 0) {
                for (int s = 0; s < splitter.length; s++) {
                    if (text.charAt(i) == splitter[s]) {
                        toReturn.add(text.substring(lastSplitPoint,i));
                        lastSplitPoint = i+1;
                        break;
                    }
                }
            }
        }
        toReturn.add(text.substring(lastSplitPoint));
        return toReturn;
    }
    public static ArrayList<String> betterSplit(String text, char splitter) {
        text = text.trim();
        ArrayList<String> toReturn = new ArrayList<>();
        int lastSplitPoint = 0;
        int pLev, brackLev, curlyLev, qLev, apostLev;
        pLev = brackLev = curlyLev = qLev = apostLev = 0;
        if(text.isEmpty()) return toReturn;
        for (int i = 0; i < text.length(); i++) {
            switch (text.charAt(i)) {
                case '\\':
                    if (i < text.length() - 1) text = text.substring(0, i) + text.substring(i+1);
                    break;
                case '(':
                    pLev++;
                    break;
                case ')':
                    pLev--;
                    break;
                case '[':
                    brackLev++;
                    break;
                case ']':
                    brackLev--;
                    break;
                case '{':
                    curlyLev++;
                    break;
                case '}':
                    curlyLev--;
                    break;
                case '"':
                    qLev = Math.abs(qLev - 1);
                case '\'':
                    apostLev = Math.abs(apostLev - 1);
            }
            if (pLev == 0 && brackLev == 0 && curlyLev == 0 && qLev == 0 && apostLev == 0 && text.charAt(i) == splitter) {
                toReturn.add(text.substring(lastSplitPoint,i));
                lastSplitPoint = i+1;
            }
        }
        toReturn.add(text.substring(lastSplitPoint));
        return toReturn;
    }
    public static ArrayList<Integer> betterIndsAtOperand(String text, char[] splitter) {
        text = text.trim();
        ArrayList<Integer> toReturn = new ArrayList<>();
        int pLev, brackLev, curlyLev, qLev, apostLev;
        pLev = brackLev = curlyLev = qLev = apostLev  = 0;
        if(text.isEmpty()) return toReturn;
        for (int i = 0; i < text.length(); i++) {
            switch (text.charAt(i)) {
                case '\\':
                    if (i < text.length() - 1) text = text.substring(0, i) + text.substring(i+1);
                    break;
                case '(':
                    pLev++;
                    break;
                case ')':
                    pLev--;
                    break;
                case '[':
                    brackLev++;
                    break;
                case ']':
                    brackLev--;
                    break;
                case '{':
                    curlyLev++;
                    break;
                case '}':
                    curlyLev--;
                    break;
                case '"':
                    qLev = Math.abs(qLev - 1);
                case '\'':
                    apostLev = Math.abs(apostLev - 1);
                    
            }
            if (pLev == 0 && brackLev == 0 && curlyLev == 0 && qLev == 0 && apostLev == 0) {
                for (int s = 0; s < splitter.length; s++) {
                    if (text.charAt(i) == splitter[s]) {
                        toReturn.add(i);
                        break;
                    }
                }
            }
        }
        return toReturn;
    }
    
    public static ArrayList<Integer> betterIndsAtOperand(String text, int lev) {
        return betterIndsAtOperand(text, opLevs.get(lev));
    }
    public static int indAtOperand(String input, int whichOne) { 
        try {
            return indsAtOperand(input, 0).get(whichOne);
        } catch (IndexOutOfBoundsException ex) {
            return input.length();
        }
    }
    
    public static int indAtOperandLast(String input, int whichOne) { // whichOne is number from back, whichOne = 0 means last
        ArrayList<Integer> inds= indsAtOperand(input, 0);
        whichOne = inds.size() - whichOne - 1;
        try { return inds.get(whichOne); }
        catch (IndexOutOfBoundsException ex) { return -1; }
    }    

    public static int getFirstAlph(String name) { // returns index of first alphabetic char in string
        int alphPos;
        for (alphPos = 0; alphPos < name.length(); alphPos++) {
            if (Character.isAlphabetic(name.charAt(alphPos))) break;
        }
        return alphPos;
    }
    
        public static int indOfCorresp(String input, char opening, char closing) {
        int changingLevel = 1, correctLevel = 0;
        for (int i = 0; i < input.length(); i++){
            if (input.charAt(i) == opening) changingLevel++;
            else if (input.charAt(i) == closing) changingLevel--;
            
            if (changingLevel == correctLevel) return i;
        }
        
        return -1;
    }
    public static int indOfCorrespParenth(String input) { return indOfCorresp(input, '(', ')'); }
    public static int indOfCorrespBrack(String input) { return indOfCorresp(input, '[', ']'); }
    public static int indOfCorrespCurly(String input) { return indOfCorresp(input, '{', '}'); }
    
    public static Object conv2Val(String input) {
        if (input == null || input.equals("null")) return null;
        input = input.trim();
        if (input.isEmpty()) return "";
        String o_input = input;

            if (input.startsWith(openList+"") && input.endsWith(closeList+"") && freqOfMetaInString(input,openList+"") == 1 && freqOfMetaInString(input,closeList+"") == 1) {
               return correctEval(input);
   //         } else if (input.startsWith(Vector.openVec) && input.endsWith(Vector.closeVec)) {
    //            return correctEval(input);
            }
        
        input = conv2ValPar(input);
        if (input.startsWith(openList+"") && input.endsWith(closeList+"") && freqOfMetaInString(input,openList+"") == 1 && freqOfMetaInString(input,closeList+"") == 1 ) {
            return listEval(input);
        }
        if (o_input.startsWith("+") || input.startsWith("-")) input = 0 + input;
        Object toReturn = conv2ValPEMDAS(input, 1);
        if (toReturn instanceof String) return stringEval ( (String) toReturn );
        else return toReturn;
       // else return correctEval( toStringOrNull(toReturn) ); //WARNING ON JUL 24, I (RAHUL) COMMENTED THIS OUT WITHOUT LOOKING AT THE CONSEQUENCES
     }
    
    
    
    
    
    
    public static String conv2ValPar(String input) {
        while (true) {
            String separ;
            if (freqInString(input, "(") > freqInString(input, "\\(") /*&& input.contains("(") */ && parenthDif(input) == 0) separ = "(";
            //else if (freqInString(input, "{") > freqInString(input, "\\{") /*&& input.contains("(") */ && parenthDif(input) == 0) separ = "{";
            else break;
            
            int Lpos = input.indexOf(separ);
            int Rpos;
            if (separ.equals("(")) Rpos = indOfCorrespParenth(input.substring(Lpos+1)) + Lpos + 1;
            //else if (separ.equals("{")) Rpos = indOfCorrespCurly(input.substring(Lpos+1)) + Lpos + 1;
            else throw new RuntimeException("Separ should be '(' or '{' but it is not");
            String pre = input.substring(0,Lpos);
            int methodNameStart = indAtOperandLast(pre,0)+1; // the 0 means last
            while (!Character.isLetterOrDigit(input.charAt(methodNameStart)) && methodNameStart < Lpos) methodNameStart++;
            
            //if (separ.equals("{")) input = pre + (convInside == null ? null :convInside.replace("-", neg + "")) + input.substring(Rpos+1);
            
            String methodName = pre.substring(methodNameStart, Lpos);
            pre = pre.substring(0, methodNameStart);
            String convInside = null, inside = input.substring(Lpos+1,Rpos);
            ArrayList<String> insideParts = betterSplit(inside,',');
            Object[] insideConvParts = new Object[insideParts.size()];
            for (int i=0; i < insideParts.size(); i++) {
                String temp = toStringOrNull(insideParts.get(i));
                insideConvParts[i] = conv2Val(temp);
            }
            int firstAlph = getFirstAlph(methodName); // returns first alphabetic character in string
            if (firstAlph > 0) {
                pre = pre + methodName.substring(0,firstAlph) + "*";
                methodName = methodName.substring(firstAlph);
            }
            if (methodName.equals("ans")) {
                if (( (Number) correctEval(toStringOrNull(insideConvParts[0]))).intValue() == 0)
                    throw new NumberFormatException("Infinite recursion. Cannot evaluate");
            }
            if (convInside == null) {
                Object result;
                if (separ.equals("(")) result = Functions.findFunction(methodName, insideConvParts);
                //else if (separ.equals("{")) result = Functions.makeObject(methodName, insideConvParts);
                else throw new RuntimeException("Separ should be '(' or '{' but it is not");
                if (result instanceof Boolean) result = Functions.bool2Int((Boolean) result);
                convInside = toStringOrNull(result);
            }
            String post = input.substring(Rpos+1);
            input = pre + (convInside == null ? null :convInside.replace("-", neg + "")) + post;
        }
        return input;
    }
    public static String conv2ValSurrounder(String input, String surrounder, String methodName) {
        while (freqInString(input, surrounder) > 0 && freqInString(input, surrounder) % 2 == 0) {
            int LApos = input.indexOf(surrounder);
            int RApos = LApos + 1 + input.substring(LApos+1).indexOf(surrounder);
            Object inside = conv2Val(input.substring(LApos+1, RApos));
            Object absInside = findFunction(methodName, inside);
            input = input.substring(0, LApos) + absInside + input.substring(RApos+1);
        }
        return input;
    }
    public static String fixStartEnd(String input, String left, String right, String method) {
        while (freqInString(input, left) > 0 && freqInString(input, left) == freqInString(input, right)) {
            int LApos = input.indexOf(left);
            int RApos = LApos + 1 + input.substring(LApos+1).indexOf(right);
            Object inside = conv2Val(input.substring(LApos+1, RApos));
            Object absInside = findFunction(method, inside);
            input = input.substring(0, LApos) + absInside + input.substring(RApos+1);
        }
        return input;
    }
    public static Object conv2ValPEMDAS(String input, int i) {
        int expLevel = 2, highestLevel = folPEMDAS ? opLevs.size() : 2;
        if (input == null || input.equals("null")) return null;
        if (i == 0) throw new IndexOutOfBoundsException("i is 0 in conv2ValPEMDAS");
        if (i == highestLevel) return input;
        input = opCorrecter(input);
        ArrayList<String> vals = splitGroupVals(input, folPEMDAS ? i : 0);
        Object combined = null;
        if (vals.size() == 1) {
            String cmbB4 = null;
            cmbB4 = (i == highestLevel ?
                    (vals.get(0)) : 
                    (toStringOrNull(conv2ValPEMDAS(vals.get(0),i+1 ))));
            combined = conv2ValSingle(cmbB4);
        } else {
            int start = (i == expLevel ? vals.size() - 1 : 0);
            combined = conv2ValPEMDAS(vals.get(start), i+1);
            start  = start + (i == expLevel ? -1 : 1);
            for (int count = start; count < vals.size() && count >= 0; count = count + (i == expLevel ? -1 : 1)) {
                String operand = toStr(((Character) input.charAt( betterIndsAtOperand(input, folPEMDAS ? i : 0).get( (folPEMDAS ? i : 0) == expLevel ? count :count-1) )));
                Object obj1 = conv2ValPEMDAS(vals.get(count), i+1);
                Object sum = i == expLevel ? conv2ValDub(obj1, operand, combined) : conv2ValDub(combined, operand, obj1);
                combined = (sum);
            }    
        }
        Object toRet;
      //  toRet = correctEval(toStringOrNull(combined)); //WARNING ON JUL 24, I (RAHUL) COMMENTED THIS OUT WITHOUT LOOKING AT THE CONSEQUENCES
        return combined;
    }
    public static String getAllOps() {
        String toRet = "";
        for (int i = 0; i < opLevs.size(); i++) toRet = toRet + opLevs.get(0)[0];
        return toRet;
    }
    // change polynomial level for splitting into monomials if you change this list
    final public static ArrayList<char[]> opLevs = new ArrayList<char[]>(Arrays.asList(
            new char[] {'+','-','∪','∩','*','×','•','÷','/',remSymb,
                '^','►','&','∧','∨'},
            new char[] {'*','×','•','÷','/',remSymb,'&','∨'},
            new char[] {'^','►','∧'}));
    
    //strings should no longer have quotes
    //binary operators here
    public static Object conv2ValDub (Object preOp, String operand, Object postOp) {
        //Object preOp, postOp;
        //preOp = conv2ValSingle(preOpStr);
        //postOp = conv2ValSingle(postOpStr);
        Double preOpDub = null, postOpDub = null;
        if (preOp == null || postOp == null || (preOp instanceof Double && postOp instanceof Double)) {
                if (preOp instanceof Double) preOpDub = (Double) preOp;
                if (postOp instanceof Double) postOpDub = (Double) postOp;

            boolean isPreOpNull = (preOpDub == null);
            boolean isPostOpNull = (postOpDub == null);
            boolean areBothNull = isPreOpNull && isPostOpNull;
            if (isPreOpNull && !isPostOpNull && postOpDub.isInfinite()) {
                preOpDub = 0.;
                isPreOpNull = false;
            } else if (isPostOpNull && !isPreOpNull && preOpDub.isInfinite()) {
                postOpDub = 0.;
                isPostOpNull = false;
            } if ( isPreOpNull || isPostOpNull) {
                if (areBothNull) {
                    switch(operand) {
                        case "-": return 0.;
                        case "/": return 1.;
                    }
                } else {
                        switch (operand) {
                            case "*":
                                if (isPreOpNull && Math.abs(postOpDub) <= Math.pow(10, -1*magPrecision)) return 0.;
                                else if (isPostOpNull && Math.abs(preOpDub) <= Math.pow(10, -1*magPrecision)) return 0.;
                                break;
                            case "^":
                                if (isPreOpNull && Math.abs(postOpDub) <= Math.pow(10, -1*magPrecision)) return 1.;
                                break;
                    }
                }
                return null;
            }
        }
        if (preOpDub != null) preOp = preOpDub;
        if (postOpDub != null) postOp = postOpDub;
        //preOp = correctEval(toStr(preOp));
        //postOp = correctEval(toStr(postOp));
        Double preOpNumer = null,postOpNumer = null;
        if (preOp instanceof Number) preOpNumer = ((Number) (preOp)).doubleValue();
        if (postOp instanceof Number) postOpNumer = ((Number) (postOp)).doubleValue();

        Object toReturn = null;
      /*  if ( (preOp instanceof Object[][]) != (postOp instanceof Object[][]) && (!(preOp instanceof Monomial) || !operand.matches("[=]"))) {
            Object[][] group = null;
            Object partChange = null;
            if (preOp instanceof Object[][]) {
                group = (Object[][]) preOp;
                partChange = postOp;
            } else if (postOp instanceof Object[][]) {
                group = (Object[][]) postOp;
                partChange = preOp;
            }
            Object[][] ans = new Object[group.length][];
            for (int rowNum = 0; rowNum < group.length; rowNum++) {
                Object[] row = new Object[group[rowNum].length];
                for (int cellNum = 0; cellNum < group[rowNum].length; cellNum++) {
                    if(preOp instanceof Object[]) row[cellNum] = conv2ValDub(group[rowNum][cellNum], operand, partChange );
                    else row[cellNum] = conv2ValDub(partChange, operand, group[rowNum][cellNum]);
                }
                ans[rowNum] = row;
            }
            toReturn = ans;
        } else*/ if (preOp instanceof Object[][] && postOp instanceof Object[][] && operand.matches("[+-⩵=&∨]")) {
            Object[][] val1 = (Object[][]) preOp, val2 = (Object[][]) postOp;
            Object[][] ans = new Object[val1.length][];
            if (val1.length != val2.length) toReturn = null;
            else if (val1.length == 0) toReturn = new Number[0][];
            else if (val1[0].length != val2[0].length) toReturn = null;
            else {
                Object[][] toRetArray = new Object[val1.length][val1[0].length];
                toReturn = 1;
                for (int row = 0; row < val1.length; row++) {
                    for (int cell = 0; cell < val1[row].length; cell++) {
                        Object conv = conv2ValDub(val1[row][cell], operand, val2[row][cell]);
                        if (operand.matches("[+-]")) toRetArray[row][cell] = conv; 
                        else toReturn = bool2Int(isTrue(toReturn) && isTrue(conv));
                    }
                }
                if (operand.matches("[+-]")) toReturn = toRetArray;
            }
        } else 
            switch (operand) {
                case "+":
                    if (preOp instanceof String || postOp instanceof String) 
                        toReturn = concatString(toStringOrNull(preOp), toStringOrNull(postOp));
          //          else if (preOp instanceof Scalar && postOp instanceof Scalar)
            //            toReturn = ((Scalar) preOp).add((Scalar) postOp);
                    else /* if (
                            (preOp instanceof AlgebraicTerm || preOp instanceof Number) &&
                            (postOp instanceof AlgebraicTerm || postOp instanceof Number) &&
                            (preOp instanceof AlgebraicTerm && postOp instanceof AlgebraicTerm)) {
                        if (preOp instanceof Number) preOp = new Monomial((Number) preOp);
                        if (postOp instanceof Number) postOp = new Monomial((Number) postOp);
                        toReturn = new Polynomial(Arrays.asList((Monomial) preOp, (Monomial) postOp));
                    } else */ if (preOp instanceof BigDecimal && postOp instanceof BigDecimal) toReturn = ((BigDecimal) preOp).add((BigDecimal) postOp);
                    else if (preOpNumer != null && postOpNumer != null)  toReturn = preOpNumer + postOpNumer;
                    break;
                case "-":
        //            if (preOp instanceof Scalar && postOp instanceof Scalar)
          //              toReturn = ((Scalar) preOp).subtract((Scalar) postOp);
            /*        else /* if ( ((preOp instanceof Number || postOp instanceof Number) && (preOp instanceof AlgebraicTerm || postOp instanceof AlgebraicTerm)) || (preOp instanceof AlgebraicTerm && postOp instanceof AlgebraicTerm) ) { 
                        ((Monomial) postOp).multiply(-1);
                        toReturn = new Polynomial(Arrays.asList((Monomial) preOp, (Monomial) postOp));
                        ((Monomial) postOp).multiply(-1);
                    } else */ if (preOp instanceof BigDecimal && postOp instanceof BigDecimal) toReturn = ((BigDecimal) preOp).subtract((BigDecimal) postOp);
                    else  if (preOpNumer != null && postOpNumer != null)  toReturn = preOpNumer - postOpNumer;
                    break;
                case "∧": //and
                    RequiredCourseSet a = new RequiredCourseSet(0, preOp,  postOp);
                    toReturn = a;
                    break;
                case "∨": //or
                    toReturn = new RequiredCourseSet(1,preOp,  postOp);
                    break;
                case "∩": // intersection
                    Set inter = new LinkedHashSet();
                    if (!(preOp instanceof List)) preOp = new ArrayList(Arrays.asList(preOp));
                    if (!(postOp instanceof List)) postOp = new ArrayList(Arrays.asList(postOp));
                    for (Object b : (List) preOp) if (((List) postOp).contains(b)) inter.add(b);
                    toReturn = new ArrayList(inter);
                    break;
                case "∪": // union
                    Set union = new LinkedHashSet();
                    if (preOp instanceof List) union.addAll((List) preOp);
                    else union.add(preOp);
                    if (postOp instanceof List) union.addAll((List) postOp);
                    else union.add(postOp);
                    toReturn = new ArrayList(union);
                    break;
                case "⊂": //subset
                    if (preOp.equals(postOp)) {
                        toReturn = false;
                        break;
                    }
                case "⊆": //subset or equal to
                    if (!(preOp instanceof List)) preOp = new ArrayList(Arrays.asList(preOp));
                    if (!(postOp instanceof List)) postOp = new ArrayList(Arrays.asList(postOp));
                    toReturn = ((List) postOp).containsAll((List) preOp);
                    break;
                case "⊃": //superset
                    if (preOp.equals(postOp)) {
                        toReturn = false;
                        break;
                    }
                case "⊇": //superset or equal to
                    if (!(preOp instanceof List)) preOp = new ArrayList(Arrays.asList(preOp));
                    if (!(postOp instanceof List)) postOp = new ArrayList(Arrays.asList(postOp));
                    toReturn = ((List) preOp).containsAll((List) postOp);
                    break;
                case "*":
                    if ((preOp instanceof String && !(postOp instanceof String)) || (!(preOp instanceof String) && postOp instanceof String)) { // one of the arguments is a string, the other is not
                        Double number = preOpNumer;
                        String str = toStringOrNull(postOp);
                        if (preOpNumer == null) {
                            number = postOpNumer;
                            str = toStringOrNull(preOp);
                        }
                        toReturn = "\"\"";
                        for (int i = 0; i < Math.abs(number); i++) {
                            char[] toAdd = str.substring(1, str.length() - 1).toCharArray();
                            for (int c = 0; c < toAdd.length; c++) {
                                if (number > 0) toReturn = concatString(toStr(toReturn), "\"" + toAdd[c] + "\"");
                                else if (number < 0) toReturn = concatString(toStr(toReturn), "\"" + toAdd[toAdd.length - 1 - c] + "\"");
                            }
                        }
/*                    } else if (preOp instanceof Scalar && postOp instanceof Scalar)
                        toReturn = ((Scalar) preOp).multiply((Scalar) postOp);
                    else if (preOp instanceof Scalar && postOp instanceof Number)
                        toReturn = ((Scalar) preOp).multiply(postOpNumer);
                    else if (preOp instanceof Number && postOp instanceof Scalar)
                        toReturn = ((Scalar) postOp).multiply(preOpNumer);
                    else if (preOp instanceof Monomial && postOp instanceof Monomial) {
                        ((Monomial) (toReturn = preOp)).multiply((Monomial) postOp);
                    } else if (preOp instanceof Monomial && postOp instanceof Number) {
                        toReturn = preOp;
                        ((Monomial) toReturn).multiply(postOpNumer);
                    } else if (postOp instanceof Monomial && preOp instanceof Number) {
                        toReturn = postOp;
                        ((Monomial) toReturn).multiply(preOpNumer); */
                    }  else if (preOp instanceof BigDecimal && postOp instanceof BigDecimal) {
                        toReturn = ((BigDecimal) preOp).multiply((BigDecimal) postOp);
                    } else  if (preOpNumer != null && postOpNumer != null) toReturn = preOpNumer * postOpNumer;
                    else if (postOpNumer != null) {
                       double pow = Math.abs(Math.round(postOpNumer.doubleValue())); 
                        toReturn = preOp;
                        for (long l = 1;l < pow;l++) toReturn = conv2ValDub(toReturn, "+", preOp);
                        if (postOpNumer.doubleValue() < 0) toReturn = conv2ValDub(0, "-", toReturn);
                    }
                    break;
                case "/":
                   /* if (preOp instanceof Scalar && postOp instanceof Scalar)
                        toReturn = ((Scalar) preOp).divide((Scalar) postOp);
                    else if (preOp instanceof Scalar && postOp instanceof Number)
                        toReturn = ((Scalar) preOp).divide(postOpNumer);
                    else if (preOp instanceof Monomial && postOp instanceof Monomial)
                        ((Monomial) (toReturn = preOp)).divide((Monomial) postOp);
                    else if (preOp instanceof Monomial && postOp instanceof Number)
                        ((Monomial) (toReturn = preOp)).divide(postOpNumer);
                    else if (postOp instanceof Monomial && preOp instanceof Number)
                        ((Monomial) (toReturn = postOp)).divide(preOpNumer); 
                    else*/ if (preOp instanceof BigDecimal && postOp instanceof BigDecimal)
                        toReturn = ((BigDecimal) preOp).divide((BigDecimal) postOp);
                    else if (preOpNumer != null && postOpNumer != null) toReturn = preOpNumer / postOpNumer;
                    break;
                case remSymb+"": 
                    /*if (preOp instanceof Scalar && postOp instanceof Scalar)
                        toReturn = ((Scalar) preOp).modulus((Scalar) postOp);
                    else if (preOp instanceof Scalar && postOp instanceof Number)
                        toReturn = ((Scalar) preOp).modulus(postOpNumer);
                    else if (preOp instanceof Number && postOp instanceof Scalar)
                        toReturn = new Scalar(((Number) preOp).doubleValue()).modulus((Scalar) postOp);
                    else */if (preOp instanceof BigDecimal && postOp instanceof BigDecimal) toReturn = ((BigDecimal) preOp).remainder((BigDecimal) postOp);
                    else if (preOpNumer != null && postOpNumer != null) toReturn = preOpNumer % postOpNumer;
                    break;
                case "^":
                   /* if (preOp instanceof Scalar && !(postOp instanceof Scalar)) {
                        if (postOp instanceof Number && !(postOp instanceof Scalar) && Math.abs(postOpNumer.intValue() - postOpNumer.doubleValue()) <= Math.pow(10, -1*magPrecision))
                            toReturn = ((Scalar) preOp).pow(postOpNumer.intValue());
                    } else */if (preOp instanceof BigDecimal && postOp instanceof Integer)
                        toReturn = ((BigDecimal) preOp).pow( (Integer) postOp);
                    else if (preOpNumer != null && postOpNumer != null)
                        toReturn = Math.pow(preOpNumer, postOpNumer);
                    else if (postOpNumer != null) {
                        double pow = Math.abs(Math.round(postOpNumer.doubleValue())); 
                        toReturn = preOp;
                        for (long l = 1;l < pow;l++) toReturn = conv2ValDub(toReturn, "*", preOp);
                        if (postOpNumer.doubleValue() < 0) toReturn = conv2ValDub(1, "/", toStringOrNull(toReturn));
                    }
                    break;
                case "►":
                    if (postOp instanceof String) {
                            switch(minQ(stringEval( toStr(postOp))).toLowerCase()) {
                                case "str":
                                case "string":
                                    toReturn = "\"" + toStringOrNull(preOp) + "\"";
                                    break;
                                case "%":
                                case "percent":
                                case "percentage":
                                    toReturn = preOpNumer != null ? preOpNumer*100 : concatString(toStr(preOp),"\"%\"");
                                    break;
                                case "`":
                             /*   case "rad":
                                    toReturn = preOpNumer != null ? toRad(preOpNumer) : concatString(toStr(preOp),"\"`\"");
                                    break;
                                case "°":
                                case "deg":
                                    toReturn = preOpNumer != null ? toDeg(preOpNumer) : concatString(toStr(preOp),"\"`\"");
                                    break;*/
                        }
                    } else toReturn = postOp;
                    break;
                case "&":
                    if (preOp instanceof String) preOp = bool2Int(Boolean.valueOf(((String) preOp).substring(1, ((String) preOp).length() - 1)));
                    if (postOp instanceof String) {
                        postOp =
                                bool2Int(
                                Boolean.valueOf(
                                (((String) postOp).substring(1, ((String) preOp).length() - 1))));
                    }
                    toReturn = (double) bool2Int(isNumericalTrue(preOpNumer) && isNumericalTrue(postOpNumer));
                    String c = "";
                    final String str;
                    break;
          /*      case "∨":
                    if (preOp instanceof String)
                        preOp = bool2Int(Boolean.valueOf(((String) preOp).substring(1, ((String) preOp).length() - 1)));
                    if (postOp instanceof String)
                        postOp = bool2Int(Boolean.valueOf(((String) postOp).substring(1, ((String) preOp).length() - 1)));
                    toReturn = (double) bool2Int(isNumericalTrue(preOpNumer) || isNumericalTrue(postOpNumer));
                    break;*/
                /*case "⩵": case "⩶": case "<": case ">": case "≤": case "≥": case "≠":
                    toReturn = compared(preOp, operand, postOp);
                    break;
                case "=":
                    if (preOp instanceof Monomial) {
                        String name = Monomial.minC(((Monomial) preOp).toStringInput());
                        if (postOp instanceof Integer || postOp instanceof Long) {
                            addInt(name, Math.round(((Number) postOp).doubleValue()));
                        } else if (postOp instanceof Double) {
                            addDouble(name, ((Number) postOp).doubleValue());
                        } else if (postOp instanceof Scalar) {
                            addScalar(name, (Scalar) postOp);
                        } else if (postOp instanceof Number[][]) {
                            addMatrix(name, (Number[][]) postOp);
                        } else {
                            addConst(name, postOp);
                        }
                        toReturn = cNoReturn;
                    } 
                    break;*/
        }
        if (toReturn instanceof Boolean) toReturn = bool2Int((Boolean) toReturn);
        if (preOp instanceof Number /* && !(preOp instanceof Scalar) */ && postOp instanceof Number /*&& !(postOp instanceof Scalar)*/ && toReturn instanceof Number /*&& !(toReturn instanceof Scalar)*/ && !(toReturn instanceof BigDecimal)) {
            if (
                    (!operand.equals("/") && !operand.equals("^") && postOpNumer != null && Math.abs(postOpNumer) % 1 < Math.pow(10, -1*magPrecision)
                    && preOpNumer != null && Math.abs(preOpNumer) % 1 < Math.pow(10, -1*magPrecision) )
                    || (Math.abs(Math.abs(((Number) toReturn).doubleValue() % 1 - 0.5) - 0.5) < Math.pow(10, -1*magPrecision))) {
                        if ( Math.abs(((Number) toReturn).doubleValue() - (((Number) toReturn).doubleValue() % 1) - ((Number) toReturn).intValue()) <= Math.pow(10, -1*magPrecision)) 
                            return new Long( Math.round(( (Number) toReturn).doubleValue()));
            }
        }
        return toReturn;
    }
    
/*    private static Boolean compared(Object preOp, String op, Object postOp) { //op is > or <
        Boolean compared = null;
        if (preOp instanceof Number && postOp instanceof Number) {
            Double preOpNumer = ((Number) preOp).doubleValue(), postOpNumer = ((Number) postOp).doubleValue();
            if (preOp instanceof Scalar || postOp instanceof Scalar) {
                Scalar preOpSca = null;
                Scalar postOpSca = null;
                if (preOp instanceof Scalar) preOpSca = (Scalar) preOp;
                else preOpSca = new Scalar(preOpNumer);
                if (postOp instanceof Scalar) postOpSca = (Scalar) postOp;
                else postOpSca = new Scalar(postOpNumer);
                
                if (op.equals("⩵")) compared = preOpSca.equals(postOpSca);
                else if (op.equals("⩶")) compared = preOpSca.equalsExact(postOpSca);
                else if (op.equals("≠")) compared = !preOpSca.equals(postOpSca);
                else {
                    int res = preOpSca.compareTo(postOpSca);
                    if (op.equals("<")) compared = res < -1 * Math.pow(10, -1*magPrecision);
                    else if(op.equals(">")) compared = res > Math.pow(10, -1*magPrecision);
                    else if (op.equals("≤")) compared = compared(preOpSca,"<", postOpSca) || compared(preOpSca,"⩵", postOpSca);
                    else if (op.equals("≥")) compared = compared(preOpSca,">", postOpSca) || compared(preOpSca,"⩵", postOpSca);
                    else throw new NumberFormatException("Does not support '"+op+"' comparing symbol");
                }
            } else {
                if (op.equals("⩵")) {
                    compared = comparedEquals(preOp, postOp);
                } else if (op.equals("⩶")) {
                    compared = comparedEqualsExact(preOp, postOp);
                } else if (op.equals("≠")) {
                    compared = !comparedEquals(preOp, postOp);
                } else if (op.equals("<")) {
                    compared = postOpNumer - preOpNumer > Math.pow(10, -1 * magPrecision);
                } else if (op.equals(">")) {
                    compared = preOpNumer - postOpNumer > Math.pow(10, -1 * magPrecision);
                } else if (op.equals("≤")) {
                    compared = compared(preOp, "<", postOp) || compared(preOp, "⩵", postOp);
                } else if (op.equals("≥")) {
                    compared = compared(preOp, ">", postOp) || compared(preOp, "⩵", postOp);
                } else throw new NumberFormatException("Does not support '"+op+"' comparing symbol");
            }
        } else {
            if (preOp instanceof String && postOp instanceof String && op.equals("⩵")) {
                compared = comparedEquals(preOp, postOp);
            } else if (preOp instanceof String && postOp instanceof String && op.equals("⩶")) {
                compared = comparedEqualsExact(preOp, postOp);
            } else
                try {
                    if (preOp instanceof Comparable) {
                        compared = ((Comparable) preOp).compareTo(postOp) < 0;
                    } else if (postOp instanceof Comparable) {
                        compared = ((Comparable) postOp).compareTo(postOp) < 0;
                    }
                } catch (ClassCastException ccex) { compared = false; }
        }
        return compared;//⩶
    }
    private static Boolean comparedEquals(Object preOp, Object postOp) {
        Boolean toReturn = null;
        if (preOp instanceof Number && postOp instanceof Number ) {
            if (preOp instanceof Scalar && postOp instanceof Scalar) {
                toReturn = ((Scalar) preOp).equals( (Scalar) postOp);
            } else if (preOp instanceof Scalar) {
                toReturn = ((Scalar) preOp).equals(postOp);
            } else if (postOp instanceof Scalar) {
                toReturn = ((Scalar) postOp).equals(preOp);
            } else toReturn = Math.abs( ((Number) preOp).doubleValue() - ((Number) postOp).doubleValue()) <= Math.pow(10, -1*magPrecision);
        } else if (preOp instanceof String && postOp instanceof String) {
            toReturn = ((String) preOp).equalsIgnoreCase((String) postOp);
        } else toReturn = preOp.equals(postOp);
        return toReturn;
    }
    private static Boolean comparedEqualsExact(Object preOp, Object postOp) {
        Boolean toReturn = null;
        if (preOp instanceof Number && postOp instanceof Number ) {
            if (preOp instanceof Scalar && postOp instanceof Scalar) {
                toReturn = ((Scalar) preOp).equalsExact( (Scalar) postOp);
            } else if (preOp instanceof Scalar) {
                toReturn = ((Scalar) preOp).equalsExact(postOp);
            } else if (postOp instanceof Scalar) {
                toReturn = ((Scalar) postOp).equalsExact(preOp);
            } else toReturn = Math.abs( ((Number) preOp).doubleValue() - ((Number) postOp).doubleValue()) <= 0;
        } else if (preOp instanceof String && postOp instanceof String) {
            toReturn = ((String) preOp).equals((String) postOp);
        } else toReturn = preOp.equals(postOp);
        return toReturn;
    } */
    // unary operations here
    protected static Object conv2ValSingle(String input) {
        Object val = null;
        try {
            if (input.equals("null")) {
                return null;
            } else if (input.endsWith("!")) {
                Object b = conv2ValSingle(input.substring(0, input.length()-1));
                b = Functions.factorial((Number) b);
                return b;
            } else if (input.endsWith("deg")) {
                Object b = conv2ValSingle(input.substring(0, input.length()-3));
                if (printInRad()) b = Math.toRadians(((Number) b).doubleValue());
                return b;
            } else if (input.endsWith("°")) {
                Object b = conv2ValSingle(input.substring(0, input.length()-1));
                if (printInRad()) b = Math.toRadians(((Number) b).doubleValue());
                return b;
            } else if (input.endsWith("rad")) {
                Object b = conv2ValSingle(input.substring(0, input.length()-3));
                if (!printInRad()) b = Math.toDegrees(((Number) b).doubleValue());
                return b;
            } else if (input.endsWith("`")) {
                Object b = conv2ValSingle(input.substring(0, input.length()-1));
                if (!printInRad()) b = Math.toDegrees(((Number) b).doubleValue());
                return b;
            } else if (input.endsWith("%")) { // percent
                Object b = conv2ValSingle(input.substring(0, input.length()-1));
                b = ((Number) b).doubleValue()/100.0;
                return b;
            } else if (input.endsWith("‰")) { // per mille
                Object b = conv2ValSingle(input.substring(0, input.length()-1));
                b = ((Number) b).doubleValue()/1000.0;
                return b;
            } else if (input.endsWith("‱")) { // per ten thousand (basis point)
                Object b = conv2ValSingle(input.substring(0, input.length()-1));
                b = ((Number) b).doubleValue()/10000.0;
                return b;
            }  else if (input.startsWith("~")) {
                Object b = conv2ValSingle(input.substring(1));
                if (b instanceof BigDecimal) return ((BigDecimal) b).negate();
                return conv2ValDub(-1,"*",toStr(b));
            } else if (input.startsWith("{") && input.endsWith("}") && freqOfMetaInString(input, "{") == freqOfMetaInString(input, "}")) {
                return input;
            } else {
                try {
                    if (input.matches("[0-9]+b")) { //number followed by b
                        return Long.valueOf(input.substring(0, input.length()-1),2);
                    } else if (input.toLowerCase().matches("[0-9]+oct")) {
                        return Long.valueOf(input.substring(0, input.length()-3), 8);
                    } else if (input.toLowerCase().endsWith("[0-9]+ox")) {
                        return Long.valueOf(input.substring(0, input.length()-2), 16);
                    } else if (input.length() > 2 && input.contains("b") && input.substring(input.lastIndexOf("b")+1).matches("~?\\d+")){
                        int base = Integer.parseInt(input.substring(input.lastIndexOf("b")+1), radix);
                        String inDifBase = input.substring(0, input.lastIndexOf("b"));
                        return Long.valueOf(inDifBase, base);
                    } else if (input.matches("[a-zA-Z]+\\.[a-zA-Z]+")) {
                        String[] parts = input.split("\\.", 2);
                      /*  if (parts[0].equalsIgnoreCase("Program")) {
                            switch(parts[1].toLowerCase()) {
                                case "default": return Program.defaultProgram;
                                case "basic": return Program.basicProgram;
                                case "help": return Program.helpProgram;
                                case "scribble": return Program.scribbleProgram;
                                case "web": return Program.webProgram;
                                case "numbered": return Program.numberedProgram;
                                default: return null;
                            }
                        } else */return null;
                    }
                } catch (NumberFormatException ex) {
                    return Double.POSITIVE_INFINITY;
                }
            }
            /*if (input.startsWith(angleSymb)) { // special angle brace
                input = input.substring(1);
                val = Angle.valueOf(input);
            } else */if (input.startsWith("@")) {
                input = input.substring(1);
                val = 2; //correct later
            } else if (input.startsWith("!")) {
                input = input.substring(1);
                val = (double) bool2Int(getBool(input));
            } else if (input.startsWith("#")) {
                input = input.substring(1);
                val = getInt(input);
            } else if (input.startsWith("$")) {
                input = input.substring(1);
                val = getMatrix(input);
            //} else if (input.startsWith("%%") || input.toLowerCase().startsWith("s%")) {
              //  input = input.substring(2);
                //val = getScalar(input);
            } else if (input.toLowerCase().matches("f%.+")) {
                input = input.substring(2);
                val = getBigDecimal(input);
            } else if (input.startsWith("%") || input.toLowerCase().startsWith("d%")) { // redundant
                input = input.replaceFirst("d?%", "");
                val = getDouble(input);
            } else if (input.startsWith("¿")) {
                input = input.substring(1);
                val = getConst(input);
            } else if (val == null) {
                if (input.startsWith("\"") && input.endsWith("\"")) val =  stringEval(input);
               // else if(!input.startsWith("[") && input.contains("[") && freqOfMetaInString(input, "[") == freqOfMetaInString(input, "]")) val = Scalar.valueOf(input);
                
                //else val = correctEval(input); //WARNING ON JUL 24, I (RAHUL) COMMENTED THIS OUT WITHOUT LOOKING AT THE CONSEQUENCES
                else val = input;
            }
            return val;
        } catch (NullPointerException nEx) {
            return null;
        }
    }    
    
    public static Number[][] matrixAdd(Number[][] augend, Number[][] addend) {
        if (augend == null || addend == null) return null;
        if (augend.length == 0) return new Number[0][];
        Number[][] toReturn = new Number[augend.length][augend[0].length];
        for (int row = 0; row < augend.length; row++) {
            for (int cell = 0; cell < augend[row].length; cell++) {
                toReturn[row][cell] = augend[row][cell].floatValue() + addend[row][cell].floatValue();
            }
        }
        return toReturn;
    }
    
   // public String dotSeearcher(ArrayList<String> parts) { // converts a format like "Math.Pi"
   //     if (p)
   //     return null;
   // }
    
     public static String toStr(Object obj) {
     /*   if (obj.equals(Unicode.cNoReturn)) return Unicode.cNoReturn+"";
        if (obj instanceof Object[][]) {
            return matrix2String(obj);
        } else if (obj instanceof Scalar) {
            return ((Scalar) obj).toStringInput();
        } else if (obj instanceof Vector) { 
            return ((Vector) obj).toStringInput();
        } else*/ if (obj instanceof List) { 
            String toRet = String.valueOf(obj);
            toRet = toRet.replace("[", openList+"").replace("]", closeList+"");
            toRet = toRet.replace(", ", ",");
            return toRet;
       // } else if (obj instanceof Monomial) { return ((Monomial) obj).toStringInput();
  //      } else if (obj instanceof Polynomial) { return ((Polynomial) obj).toStringInput();
    //    } else if (obj instanceof BigDecimal) { return ((BigDecimal) obj).toString()+Unicode.bigDecimalExt; 
      /*  } else if (obj instanceof Program) { return ((Program) obj).toStringInput(); */}
        return String.valueOf(obj);
    }
    public static String toStringOrNull(Object obj) {        if (obj == null) return null;
        else return toStr(obj); }
    
    public static String minQ(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length()-1);
        } else throw new NumberFormatException("minQ acts on a string that starts with quotes: " + str.startsWith("\"") + " and ends with quotes: " + str.endsWith("\""));
    }
}
