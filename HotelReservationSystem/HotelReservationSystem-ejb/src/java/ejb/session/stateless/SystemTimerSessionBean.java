/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
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
    
   
    public void init(){
        TimerService timerService = sessionContext.getTimerService();
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo("SystemRoomAllocation_Info");
        ScheduleExpression schedule = new ScheduleExpression();
        schedule.dayOfWeek("*").hour("2");
        timerService.createCalendarTimer(schedule, timerConfig);
    }
    
    
    @Timeout
    @Override
    public void roomAllocation() 
    {
        //business rule : booking requires room of room type, no more room type left, allocate one that is higher (More expensive? How to determine is a room type is better?
        // more expensive, higher capacity)
        //If no more, then just dont allocate.
        
        System.out.println("Test");
        
    }
}
