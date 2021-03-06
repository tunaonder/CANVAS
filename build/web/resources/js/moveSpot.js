/* 
 * Created by Sait Tuna Onder on 2017.02.03  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */

/* global vector, THREE, renderer, raycaster, mouse, camera, controlPanelHeight, scene, mode, headerHeight, controlPanelWidth, simulationHasStarted */

// List Of ALL STATIC OBJECTS
var moveSpotObjects = [];

// The Last Added Spot
var currentMoveSpot = null;

// Radius of Spots
var enterExitRadius = 7;
var moveSpotRadius = 5;
var trafficLightRadius = 6;

//Color Code Of Spots
var trafficLightRed = 0xcc0000;
var trafficLightGreen = 0x00FF00;
var exitPointColor = 0x000000;
var enterPointColor = 0xFF6347;
var moveSpotColor = 0xffff00;
var forkColor = 0x0000ff;
var mergeColor = 0x9932CC;

var lastClickedTrafficLightId = 'none';
var lastClickedEnterPointId = 'none';
var lastClickedForkId = 'none';

//Standart Move Spot 
function MoveSpot(geometry, material, id, x, y, nextId, prevId, type) {
    THREE.Mesh.call(this, geometry, material);
    this.objectId = id;
    this.x = x;
    this.y = y;
    this.nextMoveSpotId = nextId;
    this.prevMoveSpotId = prevId;
    this.type = type;

}

// Fork extends Move Spot. Has an Alternative Next Spot
function Fork(geometry, material, id, x, y, nextId, prevId, type, newPathProbability) {
    MoveSpot.call(this, geometry, material, id, x, y, nextId, prevId, type);
    this.newPathProbability = newPathProbability;
    this.nextMoveSpotAlternativeId = null;
}

// Merge extends Move Spot. Has an Alternative Previous Spot
function Merge(geometry, material, id, x, y, nextId, prevId, type) {
    MoveSpot.call(this, geometry, material, id, x, y, nextId, prevId, type);
    this.prevMoveSpotAlternativeId = null;
}

// Traffic Light extends Move Spot
function TrafficLight(geometry, material, id, x, y, nextId, prevId,
        type, greenStartTime, greenDuration, redDuration) {
    MoveSpot.call(this, geometry, material, id, x, y, nextId, prevId, type);
    this.greenDuration = greenDuration;
    this.redDuration = redDuration;
    this.greenStartTime = greenStartTime;
    this.state = 'Red';
    if (greenStartTime === '0') {
        this.state = 'Green';
    }
}

// Enter Point extends Move Spot
function EnterPoint(geometry, material, id, x, y, nextId, prevId, type, minTime, maxTime){
    MoveSpot.call(this, geometry, material, id, x, y, nextId, prevId, type);
    this.minTime = minTime;
    this.maxTime = maxTime;    
}

// This Methold is called to add an enter Point
function enterPointInsert(xCoord, yCoord, objectId, minTime, maxTime) {

    // Set EnterPoint as a child of THREE.Mesh
    EnterPoint.prototype = new THREE.Mesh();

    //Create The Geometry and the material for the Mesh
    var geometry = new THREE.CircleGeometry(enterExitRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: enterPointColor});

    var enterPoint = new EnterPoint(geometry, material, objectId, xCoord, yCoord, "none", "none", "EnterPoint", minTime, maxTime);

    // Set the position of the enter point inside canvas scene
    enterPoint.position.x = enterPoint.x;
    enterPoint.position.y = enterPoint.y;

    // Set the Current Spot
    currentMoveSpot = enterPoint;

    // Add it to the move spot objects
    moveSpotObjects.push(enterPoint);

    // Add to the Three.js Scene
    scene.add(enterPoint);
    
    // Callback method is called when the enter point is clicked within the scene
    enterPoint.callback = enterPointCallback;

}

// This Method inserts Exit Point
function exitPointInsert(xCoord, yCoord, objectId) {
    
    // ExitPoint does not have specific object type on the client
    // Therefore, it can be created as a MoveSpot
    MoveSpot.prototype = new THREE.Mesh();

    // Create The Geometry and the material for the Mesh
    // The size and color of the exit point is different than a standart movespot
    var geometry = new THREE.CircleGeometry(enterExitRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: exitPointColor});

    var exitPoint = new MoveSpot(geometry, material, objectId, xCoord, yCoord, "none", "none", "ExitPoint");

    // Set the position of the enter point inside canvas scene
    exitPoint.position.x = exitPoint.x;
    exitPoint.position.y = exitPoint.y;

    // Set The Connections with the current spot
    currentMoveSpot.nextMoveSpotId = exitPoint.objectId;
    exitPoint.prevMoveSpotId = currentMoveSpot.objectId;

    // Make current spot null
    // This is required to be able to start a new path with an EnterPoint
    currentMoveSpot = null;

    //Add it to static object array
    moveSpotObjects.push(exitPoint);

    //Add to the Scene
    scene.add(exitPoint);

}

// This method inserts a traffic light
function trafficLightInsert(xCoord, yCoord, objectId, greenStartTime, greenDuration, redDuration) {

    TrafficLight.prototype = new THREE.Mesh();
    var geometry = new THREE.CircleGeometry(trafficLightRadius, 32);
    var material;

    // Set the Color of the Traffic Light
    if (greenStartTime === '0') {
        material = new THREE.MeshBasicMaterial({color: trafficLightGreen});
    } else {
        material = new THREE.MeshBasicMaterial({color: trafficLightRed});
    }

    var trafficLight = new TrafficLight(geometry, material, objectId, xCoord,
            yCoord, 0, 0, "TrafficLight", greenStartTime, greenDuration, redDuration);

    trafficLight.position.x = trafficLight.x;
    trafficLight.position.y = trafficLight.y;

    currentMoveSpot.nextMoveSpotId = trafficLight.objectId;
    trafficLight.prevMoveSpotId = currentMoveSpot.objectId;

    currentMoveSpot = trafficLight;

    moveSpotObjects.push(trafficLight);

    //Add to the Scene
    scene.add(trafficLight);
    
    // This method is called when the traffic light is clicked within the scene
    trafficLight.callback = trafficLightCallback;
}

// This method inserts a standard move spot
function moveSpotInsert(xCoord, yCoord, objectId) {
    
    var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: moveSpotColor});

    MoveSpot.prototype = new THREE.Mesh();

    var addedMoveSpot = new MoveSpot(geometry, material, objectId, xCoord, yCoord, "none", "none", "Standart");

    addedMoveSpot.position.x = addedMoveSpot.x;
    addedMoveSpot.position.y = addedMoveSpot.y;

    currentMoveSpot.nextMoveSpotId = addedMoveSpot.objectId;
    addedMoveSpot.prevMoveSpotId = currentMoveSpot.objectId;
    
    currentMoveSpot = addedMoveSpot;

    moveSpotObjects.push(addedMoveSpot);

    scene.add(addedMoveSpot);

    // This function is called when the move spot is clicked within the scene
    addedMoveSpot.callback = moveSpotCallback;
}

/**
 * Converts the move spot into a Fork or Merge
 * Clicked move spot is removed from the scene and a new object(Fork or Merge)
 * is created
 * @returns {undefined}
 */
function moveSpotCallback() {

    //If current mode is FORK
    if (mode === 'forkButton') {
        
        // If the move spot does not have an existing next object, it cannot be converted to a fork
        if (this.nextMoveSpotId === 'none') {
            alert('Error: A static object must have a next object before converting to a Fork!');
            return;
        }
        
        // Get the new path probability
        var newPathProbability = document.getElementById("convertToForkForm:forkNewPathProbability").value;

        // Check values
        if (newPathProbability === "") {
            alert("Please Fill Fork Details");
            return;
        }

        var isNumber = newPathProbability.match(/^\d+$/);
        if (!isNumber) {
            alert("Fork Details is not valid");
            return;
        }

        if (newPathProbability <= 0 || newPathProbability >= 100) {
            alert("Fork Path Selection Probability must be a number between 0 and 100");
            return;
        }

        var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
        var material = new THREE.MeshBasicMaterial({color: forkColor});

        //Create A New Fork Containing clicked MoveSpot Info
        Fork.prototype = new THREE.Mesh();

        //Set the Next Object Id none, because alternativeId will be the Next Object of this object
        var fork = new Fork(geometry, material, this.objectId, this.x,
                this.y, "none", this.prevMoveSpotId, "Fork", newPathProbability);
        //Set the Alternative Next Move Spot Id
        fork.nextMoveSpotAlternativeId = this.nextMoveSpotId;

        //Last Clicked MoveSpot Is The Fork
        currentMoveSpot = fork;

        // Add Fork To The Scene
        fork.position.x = fork.x;
        fork.position.y = fork.y;

        // Update The Move Spot Array
        // Delete The Current Move Spot and Add The Created FORK
        for (var i = 0; i < moveSpotObjects.length; i++) {

            if (this.objectId === moveSpotObjects[i].objectId) {
                moveSpotObjects[i] = fork;
            }

        }
        
        scene.add(fork);
        //Remove The MoveSpot
        scene.remove(this);
        
        // This method is called when fork is clicked
        fork.callback = forkCallback;

        alert('MoveSpot is Converted To Fork');
        
        document.getElementById("convertToForkForm").style.display = 'none';
        document.getElementById("convertToForkForm:forkNewPathProbability").value = "";


    } else if (mode === 'mergeButton') {
        
        // If the move spot does not have an existing next object, it cannot be converted to a merge
        if (this.nextMoveSpotId === 'none') {
            alert('Error: A static object must have a next object before converting to a Merge!');
            return;
        }
        if(currentMoveSpot === null){
            alert('Error: The clicked object cannot be converted into a Merge since there is not a valid previous object!');
            return;     
        }
        
        
        var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
        var material = new THREE.MeshBasicMaterial({color: mergeColor});

        Merge.prototype = new THREE.Mesh();
        var merge = new Merge(geometry, material, this.objectId, this.x, this.y, this.nextMoveSpotId, this.prevMoveSpotId, "Merge");

        merge.prevMoveSpotAlternativeId = currentMoveSpot.objectId;
        currentMoveSpot.nextMoveSpotId = merge.objectId;


        //Update the scene position
        merge.position.x = merge.x;
        merge.position.y = merge.y;

        //Update The Move Spot Array
        for (var i = 0; i < moveSpotObjects.length; i++) {
            if (this.objectId === moveSpotObjects[i].objectId) {
                moveSpotObjects[i] = merge;
            }

        }
        // Remove the move spot
        scene.remove(this);
        
        // Add new merge
        scene.add(merge);

        // A new path must start after a point is converted into merge since
        // the merge already has a next object
        currentMoveSpot = null;

        alert('Move Spot is Converted to Merge');
    }    
}

/**
 * Display or Hide TrafficLight details in the control panel
 * @returns {undefined}
 */
function trafficLightCallback() {
   
    if (document.getElementById("trafficLightForm").style.display === 'none') {
        document.getElementById("trafficLightForm").style.display = 'inline';
        document.getElementById("trafficLightChangeButton").style.display = 'inline';
        document.getElementById("trafficLightForm:greenStartTime").value = this.greenStartTime;
        document.getElementById("trafficLightForm:greenDuration").value = this.greenDuration;
        document.getElementById("trafficLightForm:redDuration").value = this.redDuration;
        lastClickedTrafficLightId = this.objectId;

    } else {
        document.getElementById("trafficLightForm").style.display = 'none';
        document.getElementById("trafficLightChangeButton").style.display = 'none';
        document.getElementById("trafficLightForm:greenStartTime").value = "";
        document.getElementById("trafficLightForm:greenDuration").value = "";
        document.getElementById("trafficLightForm:redDuration").value = "";
        lastClickedTrafficLightId = 'none';
    }

}

/**
 * Display or Hide EnterPoint details in the control panel
 * @returns {undefined}
 */
function enterPointCallback() {
    if (document.getElementById("enterPointForm").style.display === 'none') {
        document.getElementById("enterPointForm").style.display = 'inline';
        document.getElementById("enterPointChangeButton").style.display = 'inline';
        document.getElementById("enterPointForm:minVehicleGenerationTime").value = this.minTime;
        document.getElementById("enterPointForm:maxVehicleGenerationTime").value = this.maxTime;
        lastClickedEnterPointId = this.objectId;

    } else {
        document.getElementById("enterPointForm").style.display = 'none';
        document.getElementById("enterPointChangeButton").style.display = 'none';
        document.getElementById("enterPointForm:minVehicleGenerationTime").value = "";
        document.getElementById("enterPointForm:maxVehicleGenerationTime").value = "";
        lastClickedEnterPointId = 'none';
    }

}

/**
 * Display or Hide Fork details in the control panel
 * @returns {undefined}
 */
function forkCallback(){
    if (document.getElementById("convertToForkForm").style.display === 'none') {
        document.getElementById("convertToForkForm").style.display = 'inline';
        document.getElementById("convertForkChangeButton").style.display = 'inline';
        document.getElementById("convertToForkForm:forkNewPathProbability").value = this.newPathProbability;
        lastClickedForkId = this.objectId;

    } else {
        document.getElementById("convertForkChangeButton").style.display = 'none';
        document.getElementById("convertToForkForm").style.display = 'none';
        document.getElementById("convertToForkForm:forkNewPathProbability").value = "";
        lastClickedForkId = 'none';
    }
    
}

// This Method is Called to update an existing Traffic Light
function saveTrafficLightChanges(){
    // If a traffic light is not selected return
    if(lastClickedTrafficLightId === 'none') return;
    
    var greenStartTime = document.getElementById("trafficLightForm:greenStartTime").value;
    var greenDuration = document.getElementById("trafficLightForm:greenDuration").value;
    var redDuration = document.getElementById("trafficLightForm:redDuration").value;
    
    // Check values
    if (greenStartTime === "" || greenDuration === "" || redDuration === "") {
        alert("Please Fill Traffic Light Details");
        return;
    }

    var isNumber1 = greenStartTime.match(/^\d+$/);
    var isNumber2 = greenDuration.match(/^\d+$/);
    var isNumber3 = redDuration.match(/^\d+$/);
    if (!isNumber1 || !isNumber2 || !isNumber3) {
        alert("Traffic Light Details is not valid");
        return;
    }

    if (greenStartTime < 0 || greenDuration <= 0 || redDuration <= 0) {
        alert("Time cannot be smaller than 0");
        return;
    }  
    
    if (greenDuration < 5 || redDuration < 5) {
        alert("Duration cannot be smaller than 5 seconds");
        return;
    }  
    
    // Find the traffic light object
    var trafficLight;
    for(var i = 0; i<moveSpotObjects.length; i++){
        if(lastClickedTrafficLightId === moveSpotObjects[i].objectId){
            trafficLight = moveSpotObjects[i];
            break;
        }
    }
    
    // Update traffic light values
    trafficLight.greenStartTime = greenStartTime;
    trafficLight.redDuration = redDuration;
    trafficLight.greenDuration = greenDuration;   
    if (greenStartTime === '0') {
        material = new THREE.MeshBasicMaterial({color: trafficLightGreen});
    } else {
        material = new THREE.MeshBasicMaterial({color: trafficLightRed});
    }
    trafficLight.material = material;
    
    // Hide the Settings Display
    document.getElementById("trafficLightForm").style.display = 'none';
    document.getElementById("trafficLightChangeButton").style.display = 'none';
    document.getElementById("trafficLightForm:greenStartTime").value = "";
    document.getElementById("trafficLightForm:greenDuration").value = "";
    document.getElementById("trafficLightForm:redDuration").value = "";
    lastClickedTrafficLightId = 'none';
    
    alert('Traffic Light is successfully updated!\nDo not forget to Save Model to keep changes!');

}

// This Method is Called to update an existing Enter Point
function saveEnterPointChanges(){
    // If a traffic light is not selected return
    if (lastClickedEnterPointId === 'none')
        return;

    var minTime = document.getElementById("enterPointForm:minVehicleGenerationTime").value;
    var maxTime = document.getElementById("enterPointForm:maxVehicleGenerationTime").value;

    if (minTime === "" || maxTime === "") {
        alert("Please Fill Enter Point Details");
        return;
    }

    var isNumber1 = minTime.match(/^\d+$/);
    var isNumber2 = maxTime.match(/^\d+$/);
    if (!isNumber1 || !isNumber2) {
        alert("Enter Point Details is not valid");
        return;
    }

    if (minTime <= 0 || maxTime <= 0) {
        alert("Time cannot be zero or negative");
        return;
    }

    if (parseFloat(minTime) > parseFloat(maxTime)) {
        alert("Minimum time cannot be bigger than maximum time!");
        return;
    }

    var enterPoint;
    for (var i = 0; i < moveSpotObjects.length; i++) {
        if (lastClickedEnterPointId === moveSpotObjects[i].objectId) {
            enterPoint = moveSpotObjects[i];
            break;
        }
    }

    // Update traffic light values
    enterPoint.minTime = minTime;
    enterPoint.maxTime = maxTime;


    // Hide the Settings Display
    document.getElementById("enterPointForm").style.display = 'none';
    document.getElementById("enterPointChangeButton").style.display = 'none';
    document.getElementById("enterPointForm:minVehicleGenerationTime").value = "";
    document.getElementById("enterPointForm:minVehicleGenerationTime").value = "";
    lastClickedEnterPointId = 'none';

    alert('Enter Point is successfully updated!\nDo not forget to Save Model to keep changes!');

}

// This Method is called to update an existing Fork
function saveConvertForkChanges() {
    var newPathProbability = document.getElementById("convertToForkForm:forkNewPathProbability").value;

    // Check values
    if (newPathProbability === "") {
        alert("Please Fill Fork Details");
        return;
    }

    var isNumber = newPathProbability.match(/^\d+$/);
    if (!isNumber) {
        alert("Fork Details is not valid");
        return;
    }

    if (newPathProbability <= 0 || newPathProbability >= 100) {
        alert("Fork Path Selection Probability must be a number between 0 and 100");
        return;
    }
    
    // Find the clicked fork
    var fork;
    for (var i = 0; i < moveSpotObjects.length; i++) {
        if (lastClickedForkId === moveSpotObjects[i].objectId) {
            fork = moveSpotObjects[i];
            break;
        }
    }
    
    // Update the fork's new path probability
    fork.newPathProbability = newPathProbability;
    
    // Hide fork form
    document.getElementById("convertForkChangeButton").style.display = 'none';
    document.getElementById("convertToForkForm").style.display = 'none';
    document.getElementById("convertToForkForm:forkNewPathProbability").value = "";
    lastClickedForkId = 'none';
    
    alert('Fork is successfully updated!\nDo not forget to Save Model to keep changes!');

}

/**
 *  Finds the clicked move spot and calls its callback method
 * @param {type} event
 * @returns {undefined}
 */
function moveSpotClicked(event) {

    // Determine how much user has scrolled
    // Source: https://stackoverflow.com/questions/11373741/detecting-by-how-much-user-has-scrolled
    // Scrooling must be taken into consideration to calculate the mouse click coordinate
    var scrollAmountY = (window.pageYOffset !== undefined) ? window.pageYOffset : (document.documentElement
            || document.body.parentNode || document.body).scrollTop;

    mouse.x = ((event.clientX - controlPanelWidth) / renderer.domElement.clientWidth) * 2 - 1;
    mouse.y = -(((event.clientY + scrollAmountY) - headerHeight) / renderer.domElement.clientHeight) * 2 + 1;

    raycaster.setFromCamera(mouse, camera);
    var intersects = raycaster.intersectObjects(moveSpotObjects);

    // Check if clicked point intersects with one of the move spot objects
    if (intersects.length > 0) {
        intersects[0].object.callback();
        return true;
    }
    return false;
}

// Removes The Last Added Move Spot
function removeMoveSpot() {
    
    if(simulationHasStarted){
        alert('Simulation model cannot be modified during the simulation\nPlease refresh the page to remove the last added spot');
        return;       
    }
    
    
    if (currentMoveSpot === null) {
        alert('There is no MoveSpot to Remove');
        return;
    }

    //Remove From The Scene
    scene.remove(currentMoveSpot);
    //Remove From the Array
    moveSpotObjects.splice(-1, 1);
    
    // If there are not move spot objects, make the current move spot null
    // Otherwise, the previous added object is the new current move spot
    if(moveSpotObjects.length === 0){
        currentMoveSpot = null;
    }
    else{
        //Set the new Current Move Spot
        currentMoveSpot = moveSpotObjects[moveSpotObjects.length - 1];
    } 
}

/**
 * This is called when client process traffic light state change
 * @param {type} event
 * @returns {undefined}
 */
function changeTrafficLightState(event) {
    var lightId = event.lightId;

    for (var i = 0; i < moveSpotObjects.length; i++) {

        //Find the Traffic Light from the array
        if (lightId === moveSpotObjects[i].objectId) {

            //If it is green change the state and make it Red
            if (moveSpotObjects[i].state === 'Green') {
                moveSpotObjects[i].state = 'Red';
                moveSpotObjects[i].material.color.setHex(trafficLightRed);
            } else {
                moveSpotObjects[i].state = 'Green';
                moveSpotObjects[i].material.color.setHex(trafficLightGreen);
            }

            break;
        }
    }
}


// **** FOLLOWING METHODS ARE CALLED WHEN A CANVAS MODEL IS RETRIEVED FROM DATABASE ****

function enterPointFromSavedModel(xCoord, yCoord, objectId, nextId, minTime, maxTime) {

    EnterPoint.prototype = new THREE.Mesh();

    //Create The Geometry and the material for the Mesh
    var geometry = new THREE.CircleGeometry(enterExitRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: enterPointColor});

    var enterPoint = new EnterPoint(geometry, material, objectId, xCoord, yCoord, nextId, "none", "EnterPoint", minTime, maxTime);

    enterPoint.position.x = enterPoint.x;
    enterPoint.position.y = enterPoint.y;

    //Push it To the Queue
    moveSpotObjects.push(enterPoint);

    //Add to the Three.js Scene
    scene.add(enterPoint);
    enterPoint.callback = enterPointCallback;

}

function exitPointFromSavedModel(xCoord, yCoord, objectId, prevId) {

    MoveSpot.prototype = new THREE.Mesh();

    var geometry = new THREE.CircleGeometry(enterExitRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: exitPointColor});

    var exitPoint = new MoveSpot(geometry, material, objectId, xCoord, yCoord, "none", prevId, "ExitPoint");

    exitPoint.position.x = exitPoint.x;
    exitPoint.position.y = exitPoint.y;

    moveSpotObjects.push(exitPoint);

    scene.add(exitPoint);
}

function moveSpotFromSavedModel(xCoord, yCoord, objectId, nextId, prevId) {

    var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: moveSpotColor});

    MoveSpot.prototype = new THREE.Mesh();

    var addedMoveSpot = new MoveSpot(geometry, material, objectId, xCoord, yCoord, nextId, prevId, "Standart");

    addedMoveSpot.position.x = addedMoveSpot.x;
    addedMoveSpot.position.y = addedMoveSpot.y;

    moveSpotObjects.push(addedMoveSpot);

    scene.add(addedMoveSpot);
    
    // Move Spot Can be converted into Fork or Merge
    addedMoveSpot.callback = moveSpotCallback;
}

function trafficLightFromSavedModel(xCoord, yCoord, objectId, nextId, prevId,
        greenStartTime, greenDuration, redDuration) {

    TrafficLight.prototype = new THREE.Mesh();
    var geometry = new THREE.CircleGeometry(trafficLightRadius, 32);
    var material;

    if (greenStartTime === '0') {
        material = new THREE.MeshBasicMaterial({color: trafficLightGreen});
    } else {
        material = new THREE.MeshBasicMaterial({color: trafficLightRed});
    }

    var trafficLight = new TrafficLight(geometry, material, objectId, xCoord,
            yCoord, nextId, prevId, "TrafficLight", greenStartTime, greenDuration, redDuration);

    trafficLight.position.x = trafficLight.x;
    trafficLight.position.y = trafficLight.y;

    moveSpotObjects.push(trafficLight);
    //Add to the Scene
    scene.add(trafficLight);
    trafficLight.callback = trafficLightCallback;
}

function forkFromSavedModel(objectId, xCoord, yCoord, prevMoveSpotId,
        nextMoveSpotId, nextAlternativeMoveSpotId, newPathProbability) {

    var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: forkColor});

    Fork.prototype = new THREE.Mesh();

    var fork = new Fork(geometry, material, objectId, xCoord, yCoord, nextMoveSpotId, prevMoveSpotId, "Fork", newPathProbability);

    fork.nextMoveSpotAlternativeId = nextAlternativeMoveSpotId;

    //Add Fork To The Scene
    fork.position.x = fork.x;
    fork.position.y = fork.y;

    moveSpotObjects.push(fork);

    scene.add(fork);
    
    fork.callback = forkCallback;

}

function mergeFromSavedModel(objectId, xCoord, yCoord, prevId, nextId, alternativePrevId) {

    var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: mergeColor});

    Merge.prototype = new THREE.Mesh();
    var merge = new Merge(geometry, material, objectId, xCoord, yCoord, nextId, prevId, "Merge");
    merge.prevMoveSpotAlternativeId = alternativePrevId;

    //Updaet the scene position
    merge.position.x = merge.x;
    merge.position.y = merge.y;

    moveSpotObjects.push(merge);

    scene.add(merge);

}

// **********************************************************************************