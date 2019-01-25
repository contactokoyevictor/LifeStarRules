/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.novartis.messaging.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kwame
 */
public class OtherResponse {
    private String status;
    private List<String> statusmessage;

    public OtherResponse(){
        statusmessage = new ArrayList();
    }

    public OtherResponse(String stat, List<String> statmsg){
        status = stat;
        statusmessage =statmsg;
    }

    public void setStatusmessage(List<String> msg){
        statusmessage = msg;
    }

    public void setStatus(String stat){
        status = stat;
    }

    public List<String> getStatusmessage(){
        return statusmessage;
    }

    public String getStatus(){
        return status;
    }
}
