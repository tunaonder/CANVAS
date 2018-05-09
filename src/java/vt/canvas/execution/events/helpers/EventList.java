/*
 * Created by Sait Tuna Onder on 2017.03.19  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.execution.events.helpers;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * This Class Has A Priority To Queue to keep all events in order according to their time
 * @author Onder
 */
public class EventList {

    private final PriorityQueue<Event> futureEventList;

    public EventList() {

        Comparator<Event> comparator = new EventTimeComparator();

        //Default 5000 Elements capacity
        futureEventList = new PriorityQueue<>(5000, comparator);

    }

    public PriorityQueue<Event> getFutureEventList() {
        return futureEventList;
    }

    public boolean isEmpty() {
        return futureEventList.isEmpty();
    }

    public void addEvent(Event event) {
        futureEventList.add(event);
    }

    public Event pollNextEvent() {
        return futureEventList.poll();

    }
    
    public Event peekNextEvent() {
        return futureEventList.peek();

    }
    
    public int getSize(){
        return futureEventList.size();
    }



}
