/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

/**
 *
 * @author mdk12
 */
@Stateless
@Local(SystemTimerSessionBeanLocal.class)
@Remote(SystemTimerSessionBeanRemote.class)
public class SystemTimerSessionBean implements SystemTimerSessionBeanRemote, SystemTimerSessionBeanLocal {


    @Resource 
    private SessionContext sessionContext;
    
    public SystemTimerSessionBean(){
    
    }
    
    @Override
    public void CreateTimers(){
        TimerService timerService = sessionContext.getTimerService();
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo("SystemRoomAllocation_Info");
        ScheduleExpression schedule = new ScheduleExpression();
        schedule.dayOfWeek("*").hour("*").minute("*").second("5");
        timerService.createCalendarTimer(schedule, timerConfig);
    }
    
    @Timeout
    public void testTimeOut(Timer timer) 
    {
        System.out.println("+++++++++++ Testing Time out!!! ++++++++++++" + timer.getInfo().toString());
    }
}
