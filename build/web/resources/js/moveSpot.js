/* 
 * Created by Sait Tuna Onder on 2017.02.03  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */

/* global vector, THREE, renderer, raycaster, mouse, camera, controlPanelHeight, scene, mode */

//List Of ALL STATIC OBJECTS
var moveSpotObjects = [];

//The Last Added Spot
var currentMoveSpot = null;

//Object Id will be incremented for each added objects
var objectId = 0;

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

//Fork extends Move Spot. Has an Alternative Next Spot
function Fork(geometry, material, id, x, y, nextId, prevId, type) {
    MoveSpot.call(this, geometry, material, id, x, y, nextId, prevId, type);
    this.nextMoveSpotAlternativeId = null;
}

//Merge extends Move Spot. Has an Alternative Previous Spot
function Merge(geometry, material, id, x, y, nextId, prevId, type) {
    MoveSpot.call(this, geometry, material, id, x, y, nextId, prevId, type);
    this.prevMoveSpotAlternativeId = null;
}

//Traffic Light extends Move Spot. Has a State(Green or Red)
function TrafficLight(geometry, material, id, x, y, nextId, prevId, type, state) {
    MoveSpot.call(this, geometry, material, id, x, y, nextId, prevId, type);
    this.state = state;
}

/**
 *  IMPORTANT NOTE: On the Client Side Enter and Exit Points are exactly same with MoveSpots
 *  The difference is their type as an argument. Therefore, when we insert Enter and Exit Point
 *  We create a move SPOT object
 */

//This Methold is called to add an enter Point
function enterPointInsert() {
    //
    //Create Enter Point   
    //Set MoveSpot as a child of Mesh
    MoveSpot.prototype = new THREE.Mesh();
    
    //Create The Geometry and the material for the Mesh
    var geometry = new THREE.CircleGeometry(enterExitRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: enterPointColor});

    //Set The Id Of Next Object
    objectId++;
    var startPoint = new MoveSpot(geometry, material, 's' + objectId, vector.x, vector.y, 0, 0, "EnterPoint");

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
function exitPointInsert() {
    //Create Exit Point
    //Set MoveSpot as a child of Mesh
    MoveSpot.prototype = new THREE.Mesh();
    
    //Create The Geometry and the material for the Mesh
    var geometry = new THREE.CircleGeometry(enterExitRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: exitPointColor});

    //Set The Id Of Next Object
    objectId++;
    var exitPoint = new MoveSpot(geometry, material, 's' + objectId, vector.x, vector.y, 0, 0, "ExitPoint");

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

function trafficLightInsert(state){
    
    TrafficLight.prototype = new THREE.Mesh();
    var geometry = new THREE.CircleGeometry(trafficLightRadius, 32);
    var material;
      
    if(state === 'Red'){
        material = new THREE.MeshBasicMaterial({color: trafficLightRed});
    }
    else{
        material = new THREE.MeshBasicMaterial({color: trafficLightGreen});
        
    }
    
    //Set The Id Of Next Object
    objectId++;
    
    //Create A New Fork Containing clicked MoveSpot Info
     TrafficLight.prototype = new MoveSpot();
     var trafficLight = new TrafficLight(geometry, material, 's' + objectId, vector.x, vector.y, 0, 0, "TrafficLight", state);
    

    trafficLight.position.x = trafficLight.x;
    trafficLight.position.y = trafficLight.y;

    currentMoveSpot.nextMoveSpotId = trafficLight.objectId;
    trafficLight.prevMoveSpotId = currentMoveSpot.objectId;

    //Make it null to add new enter point
    currentMoveSpot = trafficLight;


    moveSpotObjects.push(trafficLight);

    //Add to the Scene
    scene.add(trafficLight);  
}

function moveSpotInsert() {
    var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
    var material = new THREE.MeshBasicMaterial({color: moveSpotColor});

    MoveSpot.prototype = new THREE.Mesh();

    //Set the id of the next object
    objectId++;
    var addedMoveSpot = new MoveSpot(geometry, material, 's' + objectId, vector.x, vector.y, 0, 0, "Standart");

    addedMoveSpot.position.x = addedMoveSpot.x;
    addedMoveSpot.position.y = addedMoveSpot.y;

    currentMoveSpot.nextMoveSpotId = addedMoveSpot.objectId;
    addedMoveSpot.prevMoveSpotId = currentMoveSpot.objectId;
    currentMoveSpot = addedMoveSpot;

    moveSpotObjects.push(addedMoveSpot);

    scene.add(addedMoveSpot);

    //This Function Is Called When an MoveSpot Is converted to a Fork or to a Merge
    //addedMoveSpot defines the CLICKED move Spot
    addedMoveSpot.callback = function () {
       
        //If Mode is FORK
        if (mode === 'forkButton') {
            var geometry = new THREE.CircleGeometry(moveSpotRadius, 32);
            var material = new THREE.MeshBasicMaterial({color: forkColor});

            //Create A New Fork Containing clicked MoveSpot Info
            Fork.prototype = new MoveSpot();
            
            //Set the Next Object Id - 1, because alternativeId will be the Next Object of this object
            var fork = new Fork(geometry, material, this.objectId, this.x, this.y, -1, this.prevMoveSpotId, "Fork");
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
    };
}

/**
 * 
 * Finds the clicked move spot and calls its callback method
 */
function moveSpotClicked() {
    mouse.x = (event.clientX / renderer.domElement.clientWidth) * 2 - 1;
    mouse.y = -((event.clientY - (controlPanelHeight + headerHeight)) / renderer.domElement.clientHeight) * 2 + 1;


    raycaster.setFromCamera(mouse, camera);
    var intersects = raycaster.intersectObjects(moveSpotObjects);

    if (intersects.length > 0) {
        intersects[0].object.callback();
    }
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
    moveSpotObjects.splice(-1,1);
    //Set the new Current Move Spot
    currentMoveSpot = moveSpotObjects[moveSpotObjects.length-1];



}

/**
 * This is called when client process traffic light state change
 * @param {type} event
 * @returns {undefined}
 */
function changeTrafficLightState(event){  
    var lightId = event.lightId;
  
    for (var i = 0; i < moveSpotObjects.length; i++) {
        
        //Find the Traffic Light from the array
        if (lightId === moveSpotObjects[i].objectId) {
            
            //If it is green change the state and make it Red
            if(moveSpotObjects[i].state === 'Green'){
                moveSpotObjects[i].state = 'Red';
                moveSpotObjects[i].material.color.setHex(trafficLightRed);
            }
            else{
                moveSpotObjects[i].state = 'Green';
                moveSpotObjects[i].material.color.setHex(trafficLightGreen);
            }

            break;
        }
    }    
}
