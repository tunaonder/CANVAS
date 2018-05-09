/*
 * Created by Sait Tuna Onder on 2017.04.13  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.messaging.helpers;

import java.util.Comparator;
import javax.json.JsonObject;

/**
 *
 * @author Onder
 */
public class MessageTimeComparator implements Comparator<Message> {

    @Override
    public int compare(Message m1, Message m2) {

        if (m1.getTime() < m2.getTime()) {
            return -1;
        } else if (m1.getTime() > m2.getTime()) {
            return 1;
        }
        
        // Event Time is Equal. Make sure that change speed is placed after vehicle creation
        JsonObject j1 = m1.getJSONObject();
        JsonObject j2 = m2.getJSONObject();
        String t1 = j1.getString("action");
        String t2 = j2.getString("action");
        
        if (t1.equals("changeSpeed") && t2.equals("createVehicle")) {
            return 1;
        }
        else if (t1.equals("createVehicle") && t2.equals("changeSpeed")){
            return -1;          
        }
        return 0;

    }
}
