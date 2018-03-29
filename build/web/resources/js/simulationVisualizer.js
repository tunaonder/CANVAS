/* 
 * Created by Sait Tuna Onder on 2018.03.29  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */

/* global eventQueue, renderer, scene, camera */

//Array Of Current Vehicles 
var vehicles = [];

//Earliest Event
var earliestEvent;

//Earliest Event Time
var earliestEventTime = 0;

//Visualization Time
var visualizationTime = 0;

var simulationIsRunning = false;

// This Function is Called 60 times in A Second!
// Three.js main visualization processes are happening in this method.
function render() {
    //pauses when the user navigates to another browser tab
    requestAnimationFrame(render);

    if (simulationIsRunning) {

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