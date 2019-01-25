package com.novartis.connectors.schedulers;
import java.util.Timer;
import java.util.TimerTask;

import java.util.logging.*;
import com.novartis.connectors.prep.*;
import com.novartis.cadence.database.*;
/**
 *
 * @author VICTOR_OKOYE
 */
public class TimeCounter {
    private final Timer timer = new Timer();
    private final int minutes;
    private SendEmailRequest sendMail;
    private Cadence cadence;

    public TimeCounter(int minutes) {
        this.minutes = minutes;
    }
    public void stopRequest(){
        timer.cancel();
    }

    public void start() {
        
        timer.schedule(new TimerTask() {
            public void run() {
                FireEvents();
                //timer.cancel();
            }
            private void FireEvents() {
                try{
                      cadence = new Cadence();
                      sendMail = new SendEmailRequest();
                      //calling cadence rule executor....
                      cadence.invokeCadence();
                      /////////////////////////////////////////////////
                      //calling the eamil prep to create and send mail to users that are due in the message queue and move to history
                      //sendMail.createXmlRequest();
            }
                catch (Exception e) {
                       Logger.getLogger(TimeCounter.class.getName()).log(Level.SEVERE, null, e.getMessage());
                       e.getMessage();
                }
            } 
        }, minutes * 60 * 1000);
        
    }
}
