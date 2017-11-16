/* 
 * Created by Sait Tuna Onder on 2017.03.09  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */


/* global moveSpotObjects, eventQueue */

var socket = new WebSocket("ws://localhost:8080/build/actions");

//Call onMessage method when socket gets a message from the server
socket.onmessage = onMessage;

/**
 * 
 * This method is called when start Button is clicked
 * It prepared JSON objects and send them to the server
 */
function startSimulation() {

    var staticObjects = [];

    for (var i = 0; i < moveSpotObjects.length; i++) {

        var object = moveSpotObjects[i];


        if (object.type === "Standart") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId
            });
        }
        else if (object.type === "Fork") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId,
                "alternativeNextId": object.nextMoveSpotAlternativeId
            });
        }
        else if (object.type === "Merge") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId,
                "alternativePrevId": object.prevMoveSpotAlternativeId
            });
        }
        else if (object.type === "EnterPoint") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "nextId": object.nextMoveSpotId
            });

        } else if (object.type === "ExitPoint") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "prevId": object.prevMoveSpotId
            });

        }
        else if (object.type === "TrafficLight") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": object.x,
                "y": object.y,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId,
                "state": object.state
            });
        }
    }

    //Start Rendering
    simulationIsRunning = true;
    
    //Set The Button Styling to Green Background
    document.getElementById('startStopButton').style.background = "green";

    socket.send(JSON.stringify(staticObjects));
}

function testSend(){
   
    var testCase = getModel7();
    simulationIsRunning = true;
    socket.send(testCase);
}

//This method is called automatically when client receives a message from server
function onMessage(message) {

    var event = JSON.parse(message.data);

    processEvent(event);

}