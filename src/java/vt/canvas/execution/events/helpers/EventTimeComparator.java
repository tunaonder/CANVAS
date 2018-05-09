/*
 * Created by Sait Tuna Onder on 2017.03.19  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.execution.events.helpers;

import java.util.Comparator;

/**
 *
 * @author Onder
 */
public class EventTimeComparator implements Comparator<Event>{

    @Override
    public int compare(Event e1, Event e2) {
        if(e1.getTime() < e2.getTime()) return -1;
        else if(e1.getTime() > e2.getTime()) return 1;
        return 0;

    }


}
