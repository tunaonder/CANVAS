/*
 * Created by Sait Tuna Onder on 2017.04.13  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.messaging.helpers;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * This class has a Priority Queue to keep all messages in order according to their time
 * @author Onder
 */
public class MessageList {

    private final PriorityQueue<Message> messageList;
    private int size;

    public MessageList() {

        Comparator<Message> comparator = new MessageTimeComparator();

        //Default 1000 Elements capacity
        messageList = new PriorityQueue<>(20000, comparator);
        
        size = 0;

    }

    public PriorityQueue<Message> getMessageList() {
        return messageList;
    }

    public boolean isEmpty() {
        return messageList.isEmpty();
    }

    public void addMessage(Message message) {
    messageList.add(message);
        size++;
    }

    public Message pollNextMessage() {
        size--;
        return messageList.poll();
    }

    public Message peekNextMessage() {
        return messageList.peek();

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    

    
    
}
