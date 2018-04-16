/* 
 * Created by Sait Tuna Onder on 2017.02.03  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */

/* global vector, THREE, renderer, raycaster, mouse, camera, controlPanelHeight, scene, mode, headerHeight, controlPanelWidth */

//List Of ALL STATIC OBJECTS
var moveSpotObjects = [];

//The Last Added Spot
var currentMoveSpot = null;

// STATIC OBJECTS STYLING FOR THREE.JS MESHES

//Radius of Spots
var enterExitRadius = 8;
var moveSpotRadius = 5;
var trafficLightRadius = 7;

//Color Code Of Spots
var trafficLightRed = 0xcc0000;
var trafficLightGreen = 0x00FF00;
var exitPointColor = 0x000000;
var enterPointColor = 0xFF6347;
var moveSpotColor = 0xffff00;
var forkColor = 0x0000ff;
var mergeColor = 0x9932CC;

var lastClickedTrafficLightId = 'none';

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
function Fork(geometry, material, id, x, y, nextId, prevId, type) {
    MoveSpot.call(this, geometry, material, id, x, y, nextId, prevId, type);
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

//This Methold is called to add an enter Point
// xCoord: 
// yCoord: 
function enterPointInsert(xCoord, yCoord, objectId) {

    //Create Enter Point   
    //Set MoveSpot as a child of Mesh
    MoveSpot.prototype = new THREE.Mesh();

    //Create The Geometry and the material for the Mesh
    var geometry = new THREE.CircleGeometry(enterExitRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: enterPointColor});

    //Set The Id Of Next Object
    var startPoint = new MoveSpot(geometry, material, objectId, xCoord, yCoord, "none", "none", "EnterPoint");

    //position.x and position.y defines the coordinates of the MESH. It is related to Three.js
    //x and y holds the coordinate info
    startPoint.position.x = startPoint.x;
    startPoint.position.y = startPoint.y;

    //Set the Current Spot
    currentMoveSpot = startPoint;

    //Push it To the Queue
    moveSpotObjects.push(startPoint);

    //Add to the Three.js Scene
    scene.add(startPoint);

}

//This Method inserts Exit Point
function exitPointInsert(xCoord, yCoord, objectId) {
    //Create Exit Point
    //Set MoveSpot as a child of Mesh
    MoveSpot.prototype = new THREE.Mesh();

    //Create The Geometry and the material for the Mesh
    var geometry = new THREE.CircleGeometry(enterExitRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: exitPointColor});

    //Set The Id Of Next Object
    var exitPoint = new MoveSpot(geometry, material, objectId, xCoord, yCoord, "none", "none", "ExitPoint");

    //position.x and position.y defines the coordinates of the MESH. It is related to Three.js
    //x and y holds the coordinate info
    exitPoint.position.x = exitPoint.x;
    exitPoint.position.y = exitPoint.y;

    //Set The Connections with the current spot
    currentMoveSpot.nextMoveSpotId = exitPoint.objectId;
    exitPoint.prevMoveSpotId = currentMoveSpot.objectId;

    //Make current spot null to add new enter point
    currentMoveSpot = null;

    //Add it to static object array
    moveSpotObjects.push(exitPoint);

    //Add to the Scene
    scene.add(exitPoint);

}

function trafficLightInsert(xCoord, yCoord, objectId, greenStartTime, greenDuration, redDuration) {

    TrafficLight.prototype = new THREE.Mesh();
    var geometry = new THREE.CircleGeometry(trafficLightRadius, 32);
    var material;

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
    trafficLight.callback = trafficLightCallback;
}

function moveSpotInsert(xCoord, yCoord, objectId) {
    var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: moveSpotColor});

    MoveSpot.prototype = new THREE.Mesh();

    //Set the id of the next object
    var addedMoveSpot = new MoveSpot(geometry, material, objectId, xCoord, yCoord, "none", "none", "Standart");

    addedMoveSpot.position.x = addedMoveSpot.x;
    addedMoveSpot.position.y = addedMoveSpot.y;

    currentMoveSpot.nextMoveSpotId = addedMoveSpot.objectId;
    addedMoveSpot.prevMoveSpotId = currentMoveSpot.objectId;
    currentMoveSpot = addedMoveSpot;

    moveSpotObjects.push(addedMoveSpot);

    scene.add(addedMoveSpot);

    //This Function Is Called When an MoveSpot Is converted to a Fork or to a Merge
    //addedMoveSpot defines the CLICKED move Spot
    addedMoveSpot.callback = moveSpotCallback;
}

function moveSpotCallback() {

    //If Mode is FORK
    if (mode === 'forkButton') {
        if(this.nextMoveSpotId === 'none'){
            alert('Error: A static object must have a next object before converting to a Fork!');
            return;
        }
        
        var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
        var material = new THREE.MeshBasicMaterial({color: forkColor});

        //Create A New Fork Containing clicked MoveSpot Info
        Fork.prototype = new MoveSpot();

        //Set the Next Object Id - 1, because alternativeId will be the Next Object of this object
        var fork = new Fork(geometry, material, this.objectId, this.x, this.y, "none", this.prevMoveSpotId, "Fork");
        //Set the Alternative Next Move Spot Id
        fork.nextMoveSpotAlternativeId = this.nextMoveSpotId;

        //Last Clicked MoveSpot Is The Fork
        currentMoveSpot = fork;

        //Add Fork To The Scene
        fork.position.x = fork.x;
        fork.position.y = fork.y;

        //Update The Move Spot Array
        //Delete The Current Move Spot and Add The Created FORK
        for (var i = 0; i < moveSpotObjects.length; i++) {

            if (this.objectId === moveSpotObjects[i].objectId) {
                moveSpotObjects[i] = fork;
            }

        }

        scene.add(fork);
        //Remove The MoveSpot
        scene.remove(this);


        alert('MoveSpot is Converted To Fork');


    } else if (mode === 'mergeButton') {
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

        Merge.prototype = new MoveSpot();
        var merge = new Merge(geometry, material, this.objectId, this.x, this.y, this.nextMoveSpotId, this.prevMoveSpotId, "Merge");


        merge.prevMoveSpotAlternativeId = currentMoveSpot.objectId;
        currentMoveSpot.nextMoveSpotId = merge.objectId;


        //Updaet the scene position
        merge.position.x = merge.x;
        merge.position.y = merge.y;

        //Update The Move Spot Array
        for (var i = 0; i < moveSpotObjects.length; i++) {

            if (this.objectId === moveSpotObjects[i].objectId) {
                moveSpotObjects[i] = merge;
            }

        }
        //Remove The Area
        scene.remove(this);

        scene.add(merge);

        currentMoveSpot = null;

        alert('Move Spot is Converted to Merge');
    }    
}

function trafficLightCallback(){
        // Display Traffic Light Form
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
    
    alert('Traffic Light is successfully updated!');

    
}

/**
 *  Finds the clicked move spot and calls its callback method
 * @param {type} event
 * @returns {undefined}
 */
function moveSpotClicked(event) {

    // Determine how much user has scrolled
    // Source: https://stackoverflow.com/questions/11373741/detecting-by-how-much-user-has-scrolled
    var scrollAmountY = (window.pageYOffset !== undefined) ? window.pageYOffset : (document.documentElement
            || document.body.parentNode || document.body).scrollTop;

    mouse.x = ((event.clientX - controlPanelWidth) / renderer.domElement.clientWidth) * 2 - 1;
    mouse.y = -(((event.clientY + scrollAmountY) - headerHeight) / renderer.domElement.clientHeight) * 2 + 1;

    raycaster.setFromCamera(mouse, camera);
    var intersects = raycaster.intersectObjects(moveSpotObjects);

    if (intersects.length > 0) {
        intersects[0].object.callback();
        return true;
    }
    return false;
}

//Removes The Last Added Move Spot
function removeMoveSpot() {
    if (currentMoveSpot === null) {
        alert('There is no MoveSpot to Remove');
        return;
    }

    //Remove From The Scene
    scene.remove(currentMoveSpot);
    //Remove From the Array
    moveSpotObjects.splice(-1, 1);
    //Set the new Current Move Spot
    currentMoveSpot = moveSpotObjects[moveSpotObjects.length - 1];
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

// RETRIEVE MODEL FUNCTIONS

function enterPointFromSavedModel(xCoord, yCoord, objectId, nextId) {

    MoveSpot.prototype = new THREE.Mesh();

    //Create The Geometry and the material for the Mesh
    var geometry = new THREE.CircleGeometry(enterExitRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: enterPointColor});

    var startPoint = new MoveSpot(geometry, material, objectId, xCoord, yCoord, nextId, "none", "EnterPoint");

    startPoint.position.x = startPoint.x;
    startPoint.position.y = startPoint.y;

    //Push it To the Queue
    moveSpotObjects.push(startPoint);

    //Add to the Three.js Scene
    scene.add(startPoint);

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

    // TrafficLight.prototype = new MoveSpot();
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
        nextMoveSpotId, nextAlternativeMoveSpotId) {

    var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: forkColor});

    Fork.prototype = new MoveSpot();

    var fork = new Fork(geometry, material, objectId, xCoord, yCoord, nextMoveSpotId, prevMoveSpotId, "Fork");

    fork.nextMoveSpotAlternativeId = nextAlternativeMoveSpotId;

    //Add Fork To The Scene
    fork.position.x = fork.x;
    fork.position.y = fork.y;

    moveSpotObjects.push(fork);

    scene.add(fork);

}

function mergeFromSavedModel(objectId, xCoord, yCoord, prevId, nextId, alternativePrevId) {

    var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: mergeColor});

    Merge.prototype = new MoveSpot();
    var merge = new Merge(geometry, material, objectId, xCoord, yCoord, nextId, prevId, "Merge");
    merge.prevMoveSpotAlternativeId = alternativePrevId;

    //Updaet the scene position
    merge.position.x = merge.x;
    merge.position.y = merge.y;

    moveSpotObjects.push(merge);

    scene.add(merge);

}