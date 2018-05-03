/* 
 * Created by Sait Tuna Onder on 2017.03.09  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */


/* global moveSpotObjects, eventQueue, vehicleLength */

//var socket = new WebSocket("ws://shark.cs.vt.edu/canvas/actions");
var socket = new WebSocket("ws://localhost:8080/TrafficSimulator/actions");
//var socket = new WebSocket("ws://orca.cs.vt.edu/canvas/actions");

//Call onMessage method when socket gets a message from the server
socket.onmessage = onMessage;

/**
 * 
 * This method is called when start Button is clicked
 * It prepared JSON objects and send them to the server
 */
function startSimulation() {
    
    if(simulationHasStarted){
        alert('Simulation is already running!');
        return;
    }
    
    hideEnterPointForm();
    hideForkForm();
    hideTrafficLightForm();
   
    document.getElementById("vehicleSizeDiv").setAttribute('style', 'display:none !important');
    

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
    
    lengthRatio = vehicleLength / 16;
    vehicleLength2 =  Math.floor(vehicleLength2 * lengthRatio);
    vehicleLength3 = Math.floor(vehicleLength3 * lengthRatio);
    vehicleLength4 = Math.floor(vehicleLength4 * lengthRatio);
    
    // Add Simulation duration before model components
    simulationModel.push({
        "duration": simDuration,
        "vehicleLength": vehicleLength
    });

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
    socket.send(JSON.stringify(simulationModel));
}

//This method is called automatically when client receives a message from server
function onMessage(message) {

    var event = JSON.parse(message.data);
    processEvent(event);

    // Event Request has returned messages. 
    // Set it back to false to be able to make a new request
    if(eventsRequested){
        eventsRequested = false;
    }    
}