/*
 * Created by Sait Tuna Onder on 2017.05.03  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficnetwork.component;

import vt.trafficnetwork.component.helpers.StaticObject;

/**
 *
 * @author Onder
 */
public class TrafficLight extends StaticObject {

    private StaticObject prev;
    private StaticObject next;
    private STATE state;
    private int redStateTime;
    private int greenStateTime;
    private int greenStartTime;

    public TrafficLight(String id, double x, double y, int greenStartTime,
            int greenDuration, int redDuration) {
        super(id, x, y);
        // Multiply time values by 60 since the smallest time frame is 1/60 seconds
        this.greenStartTime = greenStartTime * 60;
        this.greenStateTime = greenDuration * 60;
        this.redStateTime = redDuration * 60;        
        if (greenStartTime == 0) {
            this.state = STATE.GREEN;
        } else {
            this.state = STATE.RED;
        }
    }

    public StaticObject getPrev() {
        return prev;
    }

    public void setPrev(StaticObject prev) {
        this.prev = prev;
    }

    public StaticObject getNext() {
        return next;
    }

    public void setNext(StaticObject next) {
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

    public void changeState() {

        if (this.state == STATE.GREEN) {
            this.state = STATE.RED;
        } else {
            this.state = STATE.GREEN;
        }

    }

    public enum STATE {
        RED, GREEN;
    }

}
