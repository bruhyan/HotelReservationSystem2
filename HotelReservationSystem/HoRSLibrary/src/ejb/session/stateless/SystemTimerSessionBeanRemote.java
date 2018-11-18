
package ejb.session.stateless;

import javax.ejb.Remote;
import javax.ejb.Timer;


@Remote
public interface SystemTimerSessionBeanRemote {

    public void init();

    public void roomAllocation();
    
}
