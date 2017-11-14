/* 
 * Created by Sait Tuna Onder on 2017.02.03  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */

/* global THREE, queue, eventQueue, currentMoveSpot */

var camera;
var scene;
var renderer;

//Control Panel Insert Mode(Changes According to Button Clicked)
var mode = "";

//Height Of The Control Panel
var controlPanelHeight = 80;
var headerHeight = 60;

//Three.js Objects
var raycaster = new THREE.Raycaster(); // create once
var mouse = new THREE.Vector2(); // create once
var vector = new THREE.Vector3();

//Boundries of The Map
var mapStartX;
var mapStartY;
var mapFinishX;
var mapFinishY;

//Array Of Current Vehicles 
var vehicles = [];


//Earliest Event
var earliestEvent;

//Earliest Event Time
var earliestEventTime = 0;

//Visualization Time
var visualizationTime = 0;

var simulationIsRunning = false;

//Set The When Application Displays the Map
function setScene() {
    
    //Add Event Listener to the page
    document.addEventListener('mousedown', onDocumentMouseDown, false);

    scene = new THREE.Scene();
    camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 30000);

    renderer = new THREE.WebGLRenderer();

    var backImage = new Image();
    // Default Image
    backImage.src = 'resources/images/DowntownClean.jpg';

    // Get the size ratio of the default image
    var ratio = backImage.height/backImage.width;
    
    var backgroundWidth = window.innerWidth;
    var backgroundHeight = ratio * backgroundWidth;
    
    //Degree
    var degree = Math.PI / 180;

    camera.position.z = 800;

    // Load the background texture
    var texture = THREE.ImageUtils.loadTexture('resources/images/DowntownClean.jpg');
    var backgroundMesh = new THREE.Mesh(
            new THREE.PlaneBufferGeometry(backgroundWidth, backgroundHeight, 0),
            new THREE.MeshBasicMaterial({
                map: texture
            }));

    backgroundMesh.material.depthTest = false;
    backgroundMesh.material.depthWrite = false;

    //Add the Map To The Scene
    scene.add(backgroundMesh);

    //Get The Bounds Of The Map
    var bbox = new THREE.Box3().setFromObject(backgroundMesh);
    mapStartX = bbox.min.x;
    mapStartY = bbox.min.y;
    mapFinishX = bbox.max.x;
    mapFinishY = bbox.max.y;


    //Renders the app in half resolution, but display in full size.
    renderer.setSize(window.innerWidth, window.innerHeight);
    //Set Background Color to the Scene
    renderer.setClearColor(0xafcedf);
    document.body.appendChild(renderer.domElement);

    
    //This Function is Called 60 times in A Second!
    //Three.js main visualization processes are happening in this method.
    var render = function () {
        //pauses when the user navigates to another browser tab
        requestAnimationFrame(render);
        
        if (simulationIsRunning) {
            
            
            //Render Until All events are processed
            if (eventQueue.size() !== 0) {
                
                //Get The Earliest Event
                earliestEventTime = eventQueue.getFirst().time;
                
                //If there are multiple events at the same time, process all of them before incrementing
                //visualizer time
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
            
            //Move All Vehicles
            for (var i = 0; i < vehicles.length; i++) {


                vehicles[i].position.x += vehicles[i].speed * Math.cos(vehicles[i].carRotation) * -1;
                vehicles[i].position.y += vehicles[i].speed * Math.sin(vehicles[i].carRotation) * -1;
            }
            //increment Visualization Time
            visualizationTime++;

        }
        renderer.render(scene, camera);


    };

    render();
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

/*
 * 
 * This method is called when user clicks to a location in the page
 */
function onDocumentMouseDown(event) {

    event.preventDefault();

    //Map Starts Under Control Panel Height
    //Set the correct coordinates to the vector according to window width, window height, 
    //headerHeight and coordinatePanelHeight
    vector.set(
            (event.clientX / window.innerWidth) * 2 - 1,
            -((event.clientY - (controlPanelHeight + headerHeight)) / window.innerHeight) * 2 + 1,
            0.5);

    vector.unproject(camera);

    var dir = vector.sub(camera.position).normalize();

    var distance = -camera.position.z / dir.z;

    var pos = camera.position.clone().add(dir.multiplyScalar(distance));




    //If The User Clicks Withit The Map Boundries
    if (vector.x > mapStartX && vector.x < mapFinishX && vector.y > mapStartY && vector.y < mapFinishY) {
        
        //If the Mode is Convert To Fork or Convert to Merge
        if (mode === 'forkButton' || mode === 'mergeButton') {
            moveSpotClicked();
            return;
        }
        
        //Check If The New Spot is very close to Current Spot. If it is do not add the spot
        if (currentMoveSpot !== null) {
            var distance = calculateDistance(currentMoveSpot.x, currentMoveSpot.y, vector.x, vector.y);
            
            if(distance < 40){
                window.alert('Distance between Spots should be longer than the length of vehicles');
                return;
            }        
        }

        if (mode === 'enterPointButton') {

            enterPointInsert();
        } else if (mode === 'moveSpotButton') {

            moveSpotInsert();

        } else if (mode === 'exitPointButton') {
            exitPointInsert();
        }
        else if(mode === 'greenLightButton'){
            trafficLightInsert('Green');
            
        }
        else if(mode === 'redLightButton'){
            trafficLightInsert('Red');
                 
        }

    }
}

/*
 * 
 * @param {type} id
 * This method changes styling of the clicked buttons
 * It also sets mode, so that when user clicks to a location after choosing mode
 * Selected type of spot is inserted
 */
function changeInsertMode(id) {


    //If No Button Is Clicked Before Set The Mode
    if (mode === '') {
        mode = id;
        document.getElementById(id).style.background = "green";

    }
    //If There is already a clicked button
    else {
        //If Same Button is Clicked
        if (mode === id) {
            //Clear The Mode
            document.getElementById(id).style.background = "red";
            mode = '';
        } else {
            //The Other Mode is cancelled
            document.getElementById(mode).style.background = "red";
            //Set The New Mode
            mode = id;
            //Make it Visible
            document.getElementById(mode).style.background = "green";

        }

    }
}


//Gets An Object And Camere, and returns 2d X and Y coordinates
//function toScreenPosition(obj, camera)
//{
//    var vector = new THREE.Vector3();
//
//    var widthHalf = 0.5 * renderer.context.canvas.width;
//    var heightHalf = 0.5 * renderer.context.canvas.height;
//
//    obj.updateMatrixWorld();
//    vector.setFromMatrixPosition(obj.matrixWorld);
//    vector.project(camera);
//
//    vector.x = (vector.x * widthHalf) + widthHalf;
//    vector.y = -(vector.y * heightHalf) + heightHalf;
//
//    return {
//        x: vector.x,
//        y: vector.y
//    };
//
//}

/**
 * 
 * @param {type} x1
 * @param {type} y1
 * @param {type} x2
 * @param {type} y2
 * @returns {Number} Distance between two coordinates
 */
function calculateDistance(x1, y1, x2, y2) {

    var distance1 = Math.abs(x2 - x1);
    var distance2 = Math.abs(y2 - y1);

    return Math.sqrt(distance1 * distance1 + distance2 * distance2);
}