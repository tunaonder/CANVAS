/* 
 * Created by Sait Tuna Onder on 2018.03.29  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */

/* global eventQueue, renderer, scene, camera, socket */

//Array Of Current Vehicles 
var vehicles = [];

var eventCountRequestLimit = 100;

//Earliest Event
var earliestEvent;

//Earliest Event Time
var earliestEventTime = 0;

//Visualization Time
var visualizationTime = 0;

var simulationIsRunning = false;

var eventsRequested = false;

// This Function is Called 60 times in A Second!
// Three.js main visualization processes are happening in this method.
function render() {
    //pauses when the user navigates to another browser tab
    requestAnimationFrame(render);

    if (simulationIsRunning) {
        // If events are not requested and number of events are lower than the limit
        // make a new event request
        if(!eventsRequested && eventQueue.size() < eventCountRequestLimit){
            console.log('Event Queue need more event from server');
            var requestArray = [];
            requestArray.push({
                "action": "requestEvents"
            });
            socket.send(JSON.stringify(requestArray));  
            eventsRequested = true;
        }
        
        //Render Until All events are processed
        if (eventQueue.size() !== 0) {

            //Get The Earliest Event
            earliestEventTime = eventQueue.getFirst().time;

            // If there are multiple events at the same time, process all of them before incrementing
            // the visualizer time
            while (earliestEventTime < visualizationTime + 1) {

                if (eventQueue.size() !== 0) {

                    //Pop The First Event
                    var event = eventQueue.pop();
                    //Process it
                    processCurrentEvent(event);

                    //Set the new earliest event time
                    earliestEventTime = eventQueue.getFirst().time;

                }
               // console.log(eventQueue.size());
            }
        }

        // Move All Vehicles
        for (var i = 0; i < vehicles.length; i++) {
            vehicles[i].position.x += vehicles[i].speed * Math.cos(vehicles[i].carRotation) * -1;
            vehicles[i].position.y += vehicles[i].speed * Math.sin(vehicles[i].carRotation) * -1;
        }
        // Increment Visualization Time
        visualizationTime++;

    }
    renderer.render(scene, camera);
}