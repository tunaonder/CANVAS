/* 
 * Created by Sait Tuna Onder on 2017.02.03  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */

/* global THREE, currentMoveSpot, moveSpotObjects, simulationHasStarted */

var camera;
var scene;
var renderer;

//Control Panel Insert Mode(Changes According to Button Clicked)
var mode = "";

var controlPanelWidth = 300;
var headerHeight = 60;

var canvasWidth = window.innerWidth - controlPanelWidth;
var canvasHeight = window.innerHeight - headerHeight;

//Three.js Objects
var raycaster = new THREE.Raycaster(); // create once
var mouse = new THREE.Vector2(); // create once
var vector = new THREE.Vector3();

//Boundries of The Map
var mapStartX;
var mapStartY;
var mapFinishX;
var mapFinishY;

//Object Id will be incremented for each added objects
var objectId = 0;

var staticObjectDisplay = true;

//Set The When Application Displays the Map
function setScene() {

    var currentBackgroundMap = document.getElementById("hiddenMapInput").value;

    var img = new Image();

    // Image has to be loaded to get the imageHeight/imageWidth ratio
    img.onload = function () {
        var ratio = img.height / img.width;

        //Add Event Listener to the page
        document.addEventListener('mousedown', onDocumentMouseDown, false);

        scene = new THREE.Scene();
        camera = new THREE.PerspectiveCamera(75, canvasWidth / canvasHeight, 0.1, 30000);

        renderer = new THREE.WebGLRenderer();

        var backgroundWidth = canvasWidth;
        var backgroundHeight = ratio * backgroundWidth;

        // Camera position has to be set according to size of the background image file
        // While map gets bigger, camera zooms out
        // 0.55 is selected after tests. Number could be bigger to zoom out
        camera.position.z = backgroundWidth * 0.55;

        // Load the background texture
        var texture = THREE.ImageUtils.loadTexture(currentBackgroundMap);

        
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

        retrieveModel();
        
        createExampleVehicle('+');
        

        //Renders the app in half resolution, but display in full size.
        renderer.setSize(canvasWidth, canvasHeight);
        //Set Background Color to the Scene
        renderer.setClearColor(0xafcedf);
        document.body.appendChild(renderer.domElement);
        
        render();
    };

    img.src = currentBackgroundMap;

}

/*
 * 
 * This method is called when user clicks to a location in the page
 */
function onDocumentMouseDown(event) {

    //event.preventDefault();

    // Determine how much user has scrolled
    // Source: https://stackoverflow.com/questions/11373741/detecting-by-how-much-user-has-scrolled
    var scrollAmountY = (window.pageYOffset !== undefined) ? window.pageYOffset : (document.documentElement
            || document.body.parentNode || document.body).scrollTop;

    //Set the correct coordinates to the vector according to window width, window height, 
    //headerHeight and coordinatePanelWidth
    vector.set(
            ((event.clientX - controlPanelWidth) / canvasWidth) * 2 - 1,
            -(((event.clientY + scrollAmountY) - headerHeight) / canvasHeight) * 2 + 1,
            0.5);

    vector.unproject(camera);

    var dir = vector.sub(camera.position).normalize();

    var distance = -camera.position.z / dir.z;

    var pos = camera.position.clone().add(dir.multiplyScalar(distance));

    //If The User Clicks Withit The Map Boundries
    if (vector.x > mapStartX && vector.x < mapFinishX && vector.y > mapStartY && vector.y < mapFinishY) {
        
        if (simulationHasStarted) {
            alert('Interactions with the model is not allowed after simulation has started\n\
                    Please refresh the page to update the model');
            return;
        }
        
        //If the Mode is Convert To Fork or Convert to Merge       
        if (moveSpotClicked(event)) {
            return;
        }     

        //Check If The New Spot is very close to Current Spot. If it is do not add the spot
        if (currentMoveSpot !== null && currentMoveSpot.type !== 'ExitPoint') {
            var distance = calculateDistance(currentMoveSpot.x, currentMoveSpot.y, vector.x, vector.y);

            if (distance < 30) {
                window.alert('Distance between Spots should be longer than the length of vehicles');
                return;
            }
        }

        if (mode === 'enterPointButton') {
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

            objectId++;
            enterPointInsert(vector.x, vector.y, 's' + objectId, minTime, maxTime);

            document.getElementById("enterPointForm:minVehicleGenerationTime").value = "";
            document.getElementById("enterPointForm:maxVehicleGenerationTime").value = "";
            document.getElementById("enterPointForm").style.display = 'none';
            document.getElementById("enterPointButton").style.background = "white";
            mode = ''; 
            
        } else if (mode === 'moveSpotButton') {
            objectId++;
            moveSpotInsert(vector.x, vector.y, 's' + objectId);

        } else if (mode === 'exitPointButton') {
            objectId++;
            exitPointInsert(vector.x, vector.y, 's' + objectId);
        } else if (mode === 'trafficLightButton') {
            var greenStartTime = document.getElementById("trafficLightForm:greenStartTime").value;
            var greenDuration = document.getElementById("trafficLightForm:greenDuration").value;
            var redDuration = document.getElementById("trafficLightForm:redDuration").value;

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

            objectId++;
            trafficLightInsert(vector.x, vector.y, 's' + objectId, greenStartTime, greenDuration, redDuration);

            document.getElementById("trafficLightForm:greenStartTime").value = "";
            document.getElementById("trafficLightForm:greenDuration").value = "";
            document.getElementById("trafficLightForm:redDuration").value = "";
            document.getElementById("trafficLightForm").style.display = 'none';
            document.getElementById("trafficLightButton").style.background = "white";
            mode = '';

        }
    }
}

/**
 * 
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

function buttonClicked(id) {
    
    if (simulationHasStarted) {
        alert('Model is not modifiable during the simulation visualization\nPlease refresh the page to add a new component');
        return;
    }
    
    changeInsertMode(id);

    if (id === 'trafficLightButton') {
        // Display Traffic Light Form
        if (document.getElementById("trafficLightForm").style.display === 'none') {
            document.getElementById("trafficLightForm").style.display = 'inline';
        } else {
            hideTrafficLightForm();
        }
    } else if (id === 'enterPointButton') {
        // Display Traffic Light Form
        if (document.getElementById("enterPointForm").style.display === 'none') {
            document.getElementById("enterPointForm").style.display = 'inline';
        } else {
            hideEnterPointForm();
        }
    } else if (id === 'forkButton') {
        if (document.getElementById("convertToForkForm").style.display === 'none') {
            document.getElementById("convertToForkForm").style.display = 'inline';

        } else {
            hideForkForm();
        }
    }

    if (id !== 'trafficLightButton') {
        hideTrafficLightForm();
    }
    if (id !== 'enterPointButton') {
        hideEnterPointForm();
    }

    if (id !== 'forkButton') {
        hideForkForm();
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
        document.getElementById(id).style.background = "#99C68E";

    }
    //If There is already a clicked button
    else {
        //If Same Button is Clicked
        if (mode === id) {
            //Clear The Mode
            document.getElementById(id).style.background = "white";
            mode = '';
        } else {
            //The Other Mode is cancelled
            document.getElementById(mode).style.background = "white";
            //Set The New Mode
            mode = id;
            //Make it Visible
            document.getElementById(mode).style.background = "#99C68E";
        }
    }
}

function saveModel() {

    var staticObjects = [];
    for (var i = 0; i < moveSpotObjects.length; i++) {

        var object = moveSpotObjects[i];
        var xRelativeLocation = (object.x - mapStartX) / (mapFinishX - mapStartX);
        var yRelativeLocation = (object.y - mapStartY) / (mapFinishY - mapStartY);

        if (object.type === "Standart") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": xRelativeLocation,
                "y": yRelativeLocation,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId
            });
        } else if (object.type === "Fork") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": xRelativeLocation,
                "y": yRelativeLocation,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId,
                "alternativeNextId": object.nextMoveSpotAlternativeId,
                "newPathProbability": object.newPathProbability
            });
        } else if (object.type === "Merge") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": xRelativeLocation,
                "y": yRelativeLocation,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId,
                "alternativePrevId": object.prevMoveSpotAlternativeId
            });
        } else if (object.type === "EnterPoint") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": xRelativeLocation,
                "y": yRelativeLocation,
                "nextId": object.nextMoveSpotId,
                "minTime": object.minTime,
                "maxTime": object.maxTime
            });

        } else if (object.type === "ExitPoint") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": xRelativeLocation,
                "y": yRelativeLocation,
                "prevId": object.prevMoveSpotId
            });

        } else if (object.type === "TrafficLight") {
            staticObjects.push({
                "type": object.type,
                "objectId": object.objectId,
                "x": xRelativeLocation,
                "y": yRelativeLocation,
                "nextId": object.nextMoveSpotId,
                "prevId": object.prevMoveSpotId,
                "greenStartTime": object.greenStartTime,
                "greenDuration": object.greenDuration,
                "redDuration": object.redDuration
            });
        }
    }

    document.getElementById("simModelForm:hiddenSimulationModel").value = JSON.stringify(staticObjects);
}

function retrieveModel() {

    var modelData = document.getElementById("simModelForm:hiddenSimulationModel").value;

    if (modelData === ''){
        return;
    }        
    var jsonArr = JSON.parse(modelData);
    
    if (jsonArr.length === 0){
        return;
    }
    
    for (var i = 0; i < jsonArr.length; i++) {

        var spot = jsonArr[i];
        var xRatio = spot.x;
        var yRatio = spot.y;
        var xCoord = xRatio * (mapFinishX - mapStartX) + mapStartX;
        var yCoord = yRatio * (mapFinishY - mapStartY) + mapStartY;
        if (spot.type === 'EnterPoint') {
            enterPointFromSavedModel(xCoord, yCoord, spot.objectId, spot.nextId, spot.minTime, spot.maxTime);
        } else if (spot.type === 'ExitPoint') {
            exitPointFromSavedModel(xCoord, yCoord, spot.objectId, spot.prevId);
        } else if (spot.type === 'Standart') {
            moveSpotFromSavedModel(xCoord, yCoord, spot.objectId, spot.nextId, spot.prevId);
        } else if (spot.type === 'TrafficLight') {
            trafficLightFromSavedModel(xCoord, yCoord, spot.objectId, spot.nextId, spot.prevId,
                    spot.greenStartTime, spot.greenDuration, spot.redDuration);
        } else if (spot.type === 'Fork') {
            forkFromSavedModel(spot.objectId, xCoord, yCoord, spot.prevId, spot.nextId, spot.alternativeNextId, spot.newPathProbability);
        }
        else if (spot.type === 'Merge') {
            mergeFromSavedModel(spot.objectId, xCoord, yCoord, spot.prevId, spot.nextId, spot.alternativePrevId);
        }
    }
    
    // Find the Last Added Object Id
    var lastSpot = jsonArr[jsonArr.length-1];
    var lastObjectId = lastSpot.objectId;
       
    // Set current spot the last added spot
    // Model can be modified starting from last added spot
    for (var i = 0; i < moveSpotObjects.length; i++) {
        if (lastObjectId === moveSpotObjects[i].objectId) {
            currentMoveSpot = moveSpotObjects[i];
        }
    }
    
    // Numeric Object Id To Set New Spot Ids (All Static Object Ids start with 's')
    objectId = lastObjectId.substring(1);
}

function zoomIn(){
    camera.position.z = camera.position.z * 0.95;
}

function zoomOut(){
    camera.position.z = camera.position.z * 1.05;
}

function hideShowModelDetails(button) {
    if (staticObjectDisplay) {
        for (var i = 0; i < moveSpotObjects.length; i++) {
            var obj = moveSpotObjects[i];
            if (obj.type !== "TrafficLight") {
                obj.visible = false;
            }
        }
        staticObjectDisplay = false;
        button.innerText = 'Display Spots';
    } else {
        for (var i = 0; i < moveSpotObjects.length; i++) {
            moveSpotObjects[i].visible = true;
        }
        staticObjectDisplay = true;
        button.innerText = 'Hide Spots';
    }

}

function hideTrafficLightForm() {

    document.getElementById("trafficLightForm").style.display = 'none';
    document.getElementById("trafficLightForm:greenStartTime").value = "";
    document.getElementById("trafficLightForm:greenDuration").value = "";
    document.getElementById("trafficLightForm:redDuration").value = "";
}

function hideEnterPointForm() {
    document.getElementById("enterPointForm").style.display = 'none';
    document.getElementById("enterPointForm:minVehicleGenerationTime").value = "";
    document.getElementById("enterPointForm:maxVehicleGenerationTime").value = "";
}

function hideForkForm() {
    document.getElementById("convertToForkForm").style.display = 'none';
    document.getElementById("convertToForkForm:forkNewPathProbability").value = "";
}