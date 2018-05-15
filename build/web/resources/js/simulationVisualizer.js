/* 
 * Created by Sait Tuna Onder on 2018.03.29  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */

/* global eventQueue, renderer, scene, camera, socket */

//Array Of Current Vehicles 
var vehicles = [];

// If number of events is less then this limit, more events are requested from the server
var eventCountRequestLimit = 1000;

//Earliest Event
var earliestEvent;

//Earliest Event Time
var earliestEventTime = 0;

//Visualization Time
var visualizationTime = 0;

var simulationIsRunning = false;

var eventsRequested = false;
var simulationHasStarted = false;

// This Function is Called 60 times in A Second!
// Three.js main visualization processes run in this method.
function render() {
    
    //Pauses when the user navigates to another browser tab
    requestAnimationFrame(render);

    // This is true if simulation is not paused
    if (simulationIsRunning) {
        
        // If events are not requested and number of events are lower than the client buffer limit,
        // Make a new event request
        if (!eventsRequested && eventQueue.length < eventCountRequestLimit) {
            var requestArray = [];
            requestArray.push({
                "action": "requestEvents"
            });
            // Connect to WebSocket server to request more events
            socket.send(JSON.stringify(requestArray));
            eventsRequested = true;
        }

        // Render Until All events are processed
        if (eventQueue.length !== 0) {

            //Get The Earliest Event
            earliestEventTime = eventQueue[0].time;

            // If there are multiple events at the same time, process all of them before incrementing
            // the visualizer time
            while (earliestEventTime < visualizationTime + 1) {

                if (eventQueue.length !== 0) {

                    //Pop The First Event
                    var event = eventQueue.splice(0, 1)[0];
                    //Process it
                    processCurrentEvent(event);

                    //Set the new earliest event time
                    earliestEventTime = eventQueue[0].time;
                }
            }
            // Update vehicles positions according to their speed and rotation
            for (var i = 0; i < vehicles.length; i++) {
                vehicles[i].position.x += vehicles[i].speed * Math.cos(vehicles[i].carRotation) * -1;
                vehicles[i].position.y += vehicles[i].speed * Math.sin(vehicles[i].carRotation) * -1;
            }
            // Increment Visualization Time
            visualizationTime++;
        }

    }
    renderer.render(scene, camera);

}

/**
 * Pause/Continue Simulation Visualization
 * @param {type} button
 * @returns {undefined}
 */
function pauseSimulation(button) {
    if (!simulationHasStarted) {
        alert('Simulation has not started yet!');
        return;
    }

    if (simulationIsRunning) {
        button.innerText = 'Continue';
        simulationIsRunning = false;
    } else {
        button.innerText = 'Pause Simulation';
        simulationIsRunning = true;
    }

}