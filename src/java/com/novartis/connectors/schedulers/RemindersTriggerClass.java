package com.novartis.connectors.schedulers;
/**
 * @author VICTOR_OKOYE
 */
import java.util.logging.*;
import com.novartis.connectors.prep.*;
import com.novartis.cadence.database.*;;

public class RemindersTriggerClass
{
    private volatile boolean stopRequested;
    private Thread runThread;
    private final Scheduler scheduler = new Scheduler();
    private final int hourOfDay, minute, second;
    
    private SendEmailRequest sendMail;
    private Cadence cadence;
    //Adam Barrow 20110525 - WHY IS THIS IN SCHEDULE?? private TypingToolSqlExecutor typtoolExe;
    

public RemindersTriggerClass(int hourOfDay, int minute, int second)
{
       this.hourOfDay = hourOfDay;
       this.minute = minute;
       this.second = second;
}
public void start() {
       if (runThread != null) {
           Thread.currentThread().interrupt();
           stopRequested = true;
          }
         scheduler.schedule(new SchedulerTask() {
         @Override
         public void run() {
                runThread = Thread.currentThread();
                stopRequested = false;        
                fireAllEvents();
            }
            private void fireAllEvents(){
                try{
                         cadence = new Cadence();
                         sendMail = new SendEmailRequest();
                         //Adam Barrow 20110525 - WHY IS THIS IN SCHEDULE?? typtoolExe = new TypingToolSqlExecutor();

                         ///////////////////////////////////////////////
                         //calling the typing tool to generate segment code for users...
                         //Adam Barrow 20110525 - WHY IS THIS IN SCHEDULE?? typtoolExe.fireTypingToolRules();
                         /////////////////////////////////////////////////
                         //calling cadence rule executor....
                         cadence.invokeCadence();
                         /////////////////////////////////////////////////
                         //calling the eamil prep to create and send mail to users that are due in the message queue and move to history
                         //sendMail.createXmlRequest();
                                               
                   }
                   catch(Exception e){
                       Logger.getLogger(RemindersTriggerClass.class.getName()).log(Level.SEVERE, null, e.getMessage());
                       e.getMessage();
                }
            }
        }, new DailyIterator(hourOfDay, minute, second));
    }
    public void stopRequest() {
    if (runThread != null) {
       Thread.currentThread().interrupt();
       stopRequested = true;
    }
  }
 }