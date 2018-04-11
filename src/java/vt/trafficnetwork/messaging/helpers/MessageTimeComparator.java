/*
 * Created by Sait Tuna Onder on 2017.04.13  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.messaging.helpers;

import java.util.Comparator;

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
        return 0;

    }
}
