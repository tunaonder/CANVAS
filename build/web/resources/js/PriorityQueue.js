/* 
 * Created by Sait Tuna Onder on 2017.04.30  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */


// Imlemented with the contributions of:
// http://stackoverflow.com/questions/42919469/efficient-way-to-implement-priority-queue-in-javascript


//This Queue is used to store messages in client 
//Order is the Event Time
function PriorityQueue() {
    this.data = [];
}

//Push Method Implementation
PriorityQueue.prototype.push = function (event, time) {

    // convert value to a number
    time = +time;

    // lower priority should go to the beginning of the queue.
    // priority here is TIME. earlier event is pulled from the queue earlier
    for (var i = 0; i < this.data.length && this.data[i][1] <= time; i++)
        ;

    // If it is first event, directly add it to index i=0
    if (this.data.length < 1) {
        this.data.splice(i, 0, [event, time]);
        return;
    }

    this.data.splice(i, 0, [event, time]);
};

//Remove The first element and return it
PriorityQueue.prototype.pop = function () {
    return this.data.shift()[0];
};

//Return the first element
PriorityQueue.prototype.getFirst = function () {
    return this.data[0][0];
};

PriorityQueue.prototype.size = function () {
    return this.data.length;
};

var eventQueue = new PriorityQueue();


