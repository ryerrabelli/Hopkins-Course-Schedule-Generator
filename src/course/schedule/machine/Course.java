/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

/**
 *
 * @author rahulyerrabelli
 */
public abstract class Course implements Requirable {
    protected String deptNum = "";
    protected String courseNum = "";
    protected float credits = 0;
    protected int school = 0; //0 - not determined 1-krieger 2-whiting 3- Peabody 4-other jhu   5-transfer
    protected Boolean isDesign = null;
    protected Boolean isWritingIntensive = null;
    protected String area = ""; 
    

}
