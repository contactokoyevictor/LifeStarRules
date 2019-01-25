/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.novartis.messaging.helpers;

/**
 *
 * @author Kwame
 */
public class Response {
    private String status;
    private String statusmessage;

    public Response(String stat, String msg){
        status = stat;
        statusmessage = msg;
    }

    public void setStatusmessage(String msg){
        statusmessage = msg;
    }

    public void setStatus(String stat){
        status = stat;
    }

    public String getStatusmessage(){
        return statusmessage;
    }

    public String getStatus(){
        return status;
    }

}
