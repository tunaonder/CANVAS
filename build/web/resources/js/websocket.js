/* 
 * Created by Sait Tuna Onder on 2017.03.09  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */


/* global moveSpotObjects, eventQueue, vehicleLength */

//var socket = new WebSocket("ws://shark.cs.vt.edu/canvas/actions");
//var socket = new WebSocket("ws://localhost:8080/CANVAS/actions");
var socket = new WebSocket("ws://orca.cs.vt.edu/canvas/actions");

//Call onMessage method when socket gets a message from the server
socket.onmessage = onMessage;

/**
 * 
 * This method is called when start Button is clicked
 * It prepared JSON objects and send them to the server
 */
function startSimulation() {
    
    // Check if simulation is already running
    if(simulationHasStarted){
        alert('Simulation is already running!');
        return;
    }
    
    // Hide control panel forms in case they are displayed
    hideEnterPointForm();
    hideForkForm();
    hideTrafficLightForm();
   
   // Hide vehicle size adjusment buttons
   // Vehicle size cannot be changed while simulation is running
    document.getElementById("vehicleSizeDiv").setAttribute('style', 'display:none !important');
    
    // Check if simulation duration is valid
    var simDuration = document.getElementById("simulationDuration").value;
    if (simDuration === "") {
        alert("Please enter simulation duration");
        return;
    }

    if (!simDuration.match(/^\d+$/)) {
        alert("Simulation duration is not valid");
        return;
    }

    if (simDuration <= 0) {
        alert("Duration has to be a positive number in terms of minutes");
        return;
    }

    var simulationModel = [];
    
    // Default Length of vehicles is 16.
    // Find the length change ratio according to user's vehicle size update
    // Recalculate the lengths of other vehicles(e.g trucks, busses)
    // The vehicles will be created according to updated sizes
    lengthRatio = vehicleLength / 16;
    vehicleLength2 =  Math.floor(vehicleLength2 * lengthRatio);
    vehicleLength3 = Math.floor(vehicleLength3 * lengthRatio);
    vehicleLength4 = Math.floor(vehicleLength4 * lengthRatio);
    
    // Set Simulation duration and adjusted vehicle length as the first object of JSON array
    simulationModel.push({
        "duration": simDuration,
        "vehicleLength": vehicleLength
    });
    
    // Add all simulation components to the JSON array
    for (var i = 0; i < moveSpotObjects.length; i++) {

        var object = moveSpotObjects[i];

        if (object.type === "Standart") {
            simulationModel.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId
            });
        } else if (object.type === "Fork") {
            simulationModel.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId,
                "alternativeNextId": object.nextMoveSpotAlternativeId,
                "newPathProbability": object.newPathProbability
            });
        } else if (object.type === "Merge") {
            simulationModel.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId,
                "alternativePrevId": object.prevMoveSpotAlternativeId
            });
        } else if (object.type === "EnterPoint") {
            simulationModel.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "nextId": object.nextMoveSpotId,
                "minTime": object.minTime,
                "maxTime": object.maxTime
            });

        } else if (object.type === "ExitPoint") {
            simulationModel.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "prevId": object.prevMoveSpotId
            });

        } else if (object.type === "TrafficLight") {
            simulationModel.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId,
                "greenStartTime": object.greenStartTime,
                "greenDuration": object.greenDuration,
                "redDuration": object.redDuration
            });
        }
    }

    simulationHasStarted = true;
    
    // Start WebSocket session
    // Send the JSON data to the server
    socket.send(JSON.stringify(simulationModel));
}

// This method is called automatically when client receives a message from server
function onMessage(message) {

    var event = JSON.parse(message.data);
    processEvent(event);

    // Event Request has returned messages. 
    // Set it back to false to be able to make a new request
    if(eventsRequested){
        eventsRequested = false;
    }    
}