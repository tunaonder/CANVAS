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
    private int greenStartTime;

    public TrafficLight(String id, double x, double y) {
        super(id, x, y);
        this.state = STATE.RED;
//        , int greenStartTime, 
//            int greenDuration, int redDuration
//        this.greenStartTime = greenStartTime;
//        this.greenStateTime = greenDuration;
//        this.redStateTime = redDuration;
//        if(greenStartTime == 0){
//            this.state = STATE.GREEN;
//        }
//        else{
//            this.state = STATE.RED;
//        }

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

    public int getGreenStartTime() {
        return greenStartTime;
    }

    public void setGreenStartTime(int greenStartTime) {
        this.greenStartTime = greenStartTime;
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
