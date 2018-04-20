/* 
 * Created by Sait Tuna Onder on 2017.04.07  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */

/* global THREE, scene, vehicles, mapFinishY, simulationIsRunning, simulationHasStarted */
var degree = Math.PI / 180;

var vehicleLength = 16;

var lengthRatio;
var vehicleLength2 = 26;
var vehicleLength3 = 30;
var vehicleLength4 = 40;

//Vehicle Images
var vehicleTextures = ['resources/images/Hatchback_red.png', 'resources/images/Blue_Porshe.png', 'resources/images/Porsche_black.png', 'resources/images/Porsche_blue.png',
    'resources/images/Porsche_dark_green.png', 'resources/images/Porsche_light_green.png', 'resources/images/Porsche_red.png', 'resources/images/Porsche_yellow.png', 'resources/images/truck3.png',
    'resources/images/truck4.png', 'resources/images/truck1.png', 'resources/images/truck2.png'];

var exampleVehicle = null;

function Vehicle(geometry, material, vehicleId, speed, length, rotation, x, y, targetX, targetY) {

    THREE.Mesh.call(this, geometry, material);
    this.vehicleId = vehicleId;
    this.speed = speed;
    this.length = length;
    this.x = x;
    this.y = y;
    this.targetX = targetX;
    this.targetY = targetY;
    this.carRotation = rotation;
}

function Vehicle2(geometry, material) {
    THREE.Mesh.call(this, geometry, material);
}

function createNewVehicle(event) {

    var vehicleId = event.vehicleId;
    var speed = event.speed;
    var length = event.length;
    var x = event.x;
    var y = event.y;
    var targetX = event.targetX;
    var targetY = event.targetY;
    var rotation = event.rotation;
    
    //Choose a Random Index According to the length of the new vehicle
    //If it is short, it is standart vehicle
    //According to random index, set the vehicle texture
    var vehicleImagePath;
    if (length === vehicleLength) {
        var index = Math.floor((Math.random() * 8));
        vehicleImagePath = vehicleTextures[index];
    } else if (length === vehicleLength2) {
        var index = Math.floor((Math.random() * 2));
        vehicleImagePath = vehicleTextures[8 + index];

    } else if (length === vehicleLength3) {
        vehicleImagePath = vehicleTextures[10];
    } else if (length === vehicleLength4) {
        vehicleImagePath = vehicleTextures[11];
    } else {
        return;
    }

    vehicleTexture = THREE.ImageUtils.loadTexture(vehicleImagePath);
    vehicleMaterial = new THREE.MeshBasicMaterial({map: vehicleTexture, transparent: true});
    
    // This is a simple hack. Width to height ratio is accepted as 1/2
    // However it might change from vehicle to vehicle
    // The ratio is updated when vehicle image is loaded
    // This hack is required because image is loaded asychronously
    var vehicleGeometry = new THREE.PlaneBufferGeometry(length * 1 / 2, length, 0);

    var newVehicle;
    //Vehicle is also a Three.js mesh
    Vehicle.prototype = new THREE.Mesh();
    newVehicle = new Vehicle(vehicleGeometry, vehicleMaterial, vehicleId, speed, length, rotation, x, y, targetX, targetY);

    //Set the Positions in the scene
    newVehicle.position.x = newVehicle.x;
    newVehicle.position.y = newVehicle.y;
    //Set vehicle rotation
    newVehicle.rotation.z = newVehicle.carRotation + degree * 90;

    //Add to the scene and vehicle array
    scene.add(newVehicle);
    vehicles.push(newVehicle); 
    
    // Get the Width/Height ratio from the image
    // Update the vehicle geometry
    var img = new Image();
    img.onload = function () {
        var height = img.height;
        var width = img.width;
        var ratio = width / height;

        var vehicleGeometry = new THREE.PlaneBufferGeometry(length * ratio, length, 0);
        newVehicle.geometry = vehicleGeometry;
    };

    img.src = vehicleImagePath;
    




}

/**
 * 
 * @param {type} event
 * Find the Vehicle from vehicle list and set its' new attricutes
 */
function changeVehicleDirection(event) {

    var vehicleId = event.vehicleId;
    var rotation = event.rotation;
    var x = event.x;
    var y = event.y;
    var speed = event.speed;

    for (var i = 0; i < vehicles.length; i++) {

        if (vehicleId === vehicles[i].vehicleId) {
            vehicles[i].carRotation = rotation;
            vehicles[i].rotation.z = vehicles[i].carRotation + degree * 90;
            vehicles[i].speed = speed;

            vehicles[i].x = x;
            vehicles[i].y = y;

            vehicles[i].position.x = x;
            vehicles[i].position.y = y;

            break;
        }
    }
}

/**
 * 
 * @param {type} event
 * Find the vehicle from vehicle list and change its speed
 */
function changeVehicleSpeed(event) {

    var vehicleId = event.vehicleId;
    var speed = event.speed;

    for (var i = 0; i < vehicles.length; i++) {

        if (vehicleId === vehicles[i].vehicleId) {            
            vehicles[i].speed = speed;
            break;
        }

    }
}

/**
 * 
 * @param {type} event
 * Find the vehicle from the vehicle list and remove it
 */
function destroyVehicle(event) {

    var vehicleId = event.vehicleId;

    for (var i = 0; i < vehicles.length; i++) {

        if (vehicleId === vehicles[i].vehicleId) {

            vehicles[i].position.y = vehicles[i].position.y + 30;
            scene.remove(vehicles[i]);

            //Remove Vehicle at index i
            vehicles.splice(i, 1);
            //i = i - 1;

            break;
        }
    }
}

function createExampleVehicle(newVehicleMode) {

    if(simulationHasStarted){
        alert('Vehicle size cannot be updated after the simulation has started!');
        return;
    }

    // exampleVehicle is not null if page is already loaded
    if (exampleVehicle !== null) {
        // Remove the existing vehicle
        scene.remove(exampleVehicle);
        // Set the new length according to user action
        if (newVehicleMode === '+') {
            vehicleLength += 2;
        } else {
            vehicleLength -= 2;
        }
    }

    var img = new Image();
    img.onload = function () {
        var length = img.height;
        var width = img.width;
        var ratio = width / length;

        vehicleWidth = vehicleLength * ratio;

        vehicleTexture = THREE.ImageUtils.loadTexture(vehicleTextures[0]);

        //Create Mesh
        vehicleMaterial = new THREE.MeshBasicMaterial({map: vehicleTexture, transparent: true});

        var vehicleGeometry = new THREE.PlaneBufferGeometry(vehicleWidth, vehicleLength, 0);

        //Vehicle is also a Three.js mesh
        Vehicle.prototype = new THREE.Mesh();
        // Set the vehicle on top of map, so that it can be clearly visible
        exampleVehicle = new Vehicle(vehicleGeometry, vehicleMaterial, '', 0, vehicleLength, 0, 0, mapFinishY + 20, 0, 0);

        //Set the Positions in the scene
        exampleVehicle.position.x = exampleVehicle.x;
        exampleVehicle.position.y = exampleVehicle.y;
        //Set vehicle rotation
        exampleVehicle.rotation.z = exampleVehicle.carRotation + degree * 90;

        //Add to the scene and vehicle array
        scene.add(exampleVehicle);

    };

    img.src = vehicleTextures[0];


}
