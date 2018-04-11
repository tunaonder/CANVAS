/* 
 * Created by Sait Tuna Onder on 2017.04.07  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */


/* global eventQueue */

//Event Id. Will be Incremented for each received message
var eventId = 0;

// Vehicle Create Event
function Event(type, time, vehicleId, speed, length, rotation, x, y, targetX, targetY, eventId) {
    this.type = type;
    this.time = time;
    this.vehicleId = vehicleId;
    this.speed = speed;
    this.length = length;
    this.rotation = rotation;
    this.x = x;
    this.y = y;
    this.targetX = targetX;
    this.targetY = targetY;
    this.id = eventId;
}

// Change Direction Event
function Event2(type, time, vehicleId, rotation, eventId, x, y, speed) {
    this.type = type;
    this.time = time;
    this.vehicleId = vehicleId;
    this.rotation = rotation;
    this.id = eventId;
    this.x = x;
    this.y = y;
    this.speed = speed;

}

// Change Speed Event
function Event3(type, time, vehicleId, speed) {
    this.type = type;
    this.time = time;
    this.vehicleId = vehicleId;
    this.speed = speed;

}

// Destroy Vehicle Event
function Event4(type, time, vehicleId) {
    this.type = type;
    this.time = time;
    this.vehicleId = vehicleId;

}

// Change Traffic Light State Event
function Event5(type, time, lightId) {
    this.type = type;
    this.time = time;
    this.lightId = lightId;

}


// This Method is called by websocket.js when a new message is received
// Events are created accordingly and added to Event Queue
function processEvent(event) {

    eventId++;

    if (event.action === "createVehicle") {

        
        var type = event.action;
        var time = event.time;
        var vehicleId = event.vehicleId;
        var speed = event.speed;
        var length = event.length;
        var x = event.x;
        var y = event.y;
        var targetX = event.targetX;
        var targetY = event.targetY;
        var rotation = event.rotation;

        var newEvent = new Event(type, time, vehicleId, speed, length, rotation, x, y, targetX, targetY, eventId);

        eventQueue.push(newEvent, time);

    } else if (event.action === "changeDirection") {
        var type = event.action;
        var time = event.time;
        var vehicleId = event.vehicleId;
        var rotation = event.rotation;
        var x = event.x;
        var y = event.y;
        var speed = event.speed;

        var newEvent = new Event2(type, time, vehicleId, rotation, eventId, x, y, speed);
        
        eventQueue.push(newEvent, time);

    } else if (event.action === "changeSpeed") {

        var type = event.action;
        var time = event.time;
        var vehicleId = event.vehicleId;
        var speed = event.speed;

        var newEvent = new Event3(type, time, vehicleId, speed);

        eventQueue.push(newEvent, time);

    } else if (event.action === "vehicleDestroy") {

        var type = event.action;
        var time = event.time;
        var vehicleId = event.vehicleId;

        var newEvent = new Event4(type, time, vehicleId);

        eventQueue.push(newEvent, time);
        
        
    } else if (event.action === "trafficLightStateChange") {

        var type = event.action;
        var time = event.time;
        var lightId = event.lightId;
        var newEvent = new Event5(type, time, lightId);
        eventQueue.push(newEvent, time);
    }

}

/**
 * Process Each event according to their types
 * @param {type} event
 * @returns {undefined}
 */
function processCurrentEvent(event) {


    if (event.type === "createVehicle") {
        createNewVehicle(event);
    } else if (event.type === "changeDirection") {
        changeVehicleDirection(event);

    } else if (event.type === "changeSpeed") {        
        changeVehicleSpeed(event);

    } else if (event.type === "vehicleDestroy") {
        destroyVehicle(event);

    } else if (event.type === "trafficLightStateChange") {
        changeTrafficLightState(event);

    }
}





