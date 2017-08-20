/*
 * Created by Sait Tuna Onder on 2017.05.03  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component;

import vt.trafficnetwork.component.helpers.MovementObject;

/**
 *
 * @author Onder
 */
public class TrafficLight extends MovementObject {

    private MovementObject prev;
    private MovementObject next;
    private STATE state;
    private int redStateTime;
    private int greenStateTime;

    public TrafficLight(String id, double x, double y) {
        super(id, x, y);
        redStateTime = 600;
        greenStateTime = 600;

    }

    public MovementObject getPrev() {
        return prev;
    }

    public void setPrev(MovementObject prev) {
        this.prev = prev;
    }

    public MovementObject getNext() {
        return next;
    }

    public void setNext(MovementObject next) {
        this.next = next;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public int getRedStateTime() {
        return redStateTime;
    }

    public void setRedStateTime(int redStateTime) {
        this.redStateTime = redStateTime;
    }

    public int getGreenStateTime() {
        return greenStateTime;
    }

    public void setGreenStateTime(int greenStateTime) {
        this.greenStateTime = greenStateTime;
    }
    
    
    public void changeState(){
        
        if(this.state == STATE.GREEN){
            this.state = STATE.RED;
        }
        else{
            this.state = STATE.GREEN;
        }
        
    }

    public enum STATE {
        RED, GREEN;
    }

}
