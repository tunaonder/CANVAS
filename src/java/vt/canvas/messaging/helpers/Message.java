/*
 * Created by Sait Tuna Onder on 2017.04.13  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.messaging.helpers;

import javax.json.JsonObject;

/**
 *
 * @author Onder
 */
public class Message {
    
    private double time;
    private JsonObject object;

    public Message(JsonObject object, double time) {
        this.time = time;
        this.object = object;
    }

    
    public JsonObject getJSONObject() {
        return object;
    }

    public void setJSONObject(JsonObject object) {
        this.object = object;
    }
   
    public Message(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
    
}
