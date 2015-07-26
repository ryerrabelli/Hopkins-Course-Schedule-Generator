/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package course.schedule.machine;

import java.util.Set;

/**
 *
 * @author rahulyerrabelli
 */
public interface Requirable {
    public boolean isFulfilled(Set<HopkinsCourse> coursesTaken);
}
