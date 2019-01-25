package com.novartis.connectors.schedulers;
import javax.servlet.http.HttpServlet;
import javax.servlet.*;
import java.util.logging.*;
/**
 * @author VICTOR_OKOYE
 */
public class ScheduleListener extends HttpServlet {
private TimeCounter timerClass = new TimeCounter(1022992);
//private RemindersTriggerClass trigger = new RemindersTriggerClass(15,40,0);
@Override
 public void init(ServletConfig config) throws ServletException {
    super.init(config);
    try {
            Logger.getLogger(ScheduleListener.class.getName()).log(Level.INFO,"Reminders Started from context root....");
            System.out.println("LifeStarRules Reminders Started from context root....");
            timerClass.start();
        }catch (Exception e) {
            Logger.getLogger(TimeCounter.class.getName()).log(Level.SEVERE, null, e.getMessage());
            e.getMessage();
       }
  }
@Override
public void destroy(){
       timerClass.stopRequest();
 }
}
