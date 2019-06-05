package com.hbeonlabs.smartguard.v2;

import android.util.Log;

public class DataManager {


    private String pump_id,ph,id,on_send,off_send,on_receive,off_receive;
    private Boolean pump_status= false;

    public DataManager() {
    }

    public DataManager(String pump_id, String ph, String id, String on_send,
                       String off_send, String on_receive, String off_receive, Boolean pump_status) {
        this.pump_id = pump_id;
        this.ph = ph;
        this.id = id;
        this.on_send = on_send;
        this.off_send = off_send;
        this.on_receive = on_receive;
        this.off_receive = off_receive;
        this.pump_status = pump_status;

    }

    public String getPump_id() {
        return pump_id;
    }

    public void setPump_id(String pump_id) {
        this.pump_id = pump_id;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getPump_status() {
        return pump_status;
    }

    public void setPump_status(Boolean pump_status) {
        this.pump_status = pump_status;
    }

    public String getOn_send() {
        return on_send;
    }

    public void setOn_send(String on_send) {
        this.on_send = on_send;
    }

    public String getOff_send() {
        return off_send;
    }

    public void setOff_send(String off_send) {
        this.off_send = off_send;
    }

    public String getOn_receive() {
        return on_receive;
    }

    public void setOn_receive(String on_receive) {
        this.on_receive = on_receive;
    }

    public String getOff_receive() {
        return off_receive;
    }

    public void setOff_receive(String off_receive) {
        this.off_receive = off_receive;
    }
}
