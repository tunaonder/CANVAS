/* 
 * Created by Sait Tuna Onder on 2017.04.07  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */

/* global THREE, scene, vehicles */
var degree = Math.PI / 180;

var vehicleWidth = 12.5;

//Vehicle Images
var vehicleTextures = ['resources/images/Hatchback_red.png', 'resources/images/Blue_Porshe.png', 'resources/images/Porsche_black.png', 'resources/images/Porsche_blue.png',
    'resources/images/Porsche_dark_green.png', 'resources/images/Porsche_light_green.png', 'resources/images/Porsche_red.png', 'resources/images/Porsche_yellow.png', 'resources/images/truck3.png',
    'resources/images/truck4.png', 'resources/images/truck1.png', 'resources/images/truck2.png'];

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
    if(length === 25){
        var index = Math.floor((Math.random() * 8));
        vehicleTexture = THREE.ImageUtils.loadTexture(vehicleTextures[index]);
    }
    else if(length === 35){
        var index = Math.floor((Math.random() * 2));
        vehicleTexture = THREE.ImageUtils.loadTexture(vehicleTextures[8+index]);
       
    }
    else if(length === 39){
        vehicleTexture = THREE.ImageUtils.loadTexture(vehicleTextures[10]);
    }
    else{
        vehicleTexture = THREE.ImageUtils.loadTexture(vehicleTextures[11]);
    }
    
    //Create Mesh
    vehicleMaterial = new THREE.MeshBasicMaterial({map: vehicleTexture, transparent: true});

    var vehicleGeometry = new THREE.PlaneBufferGeometry(vehicleWidth, length, 0);

    //Vehicle is also a Three.js mesh
    Vehicle.prototype = new THREE.Mesh();
    var vehicle = new Vehicle(vehicleGeometry, vehicleMaterial, vehicleId, speed, length, rotation, x, y, targetX, targetY);
    
    //Set the Positions in the scene
    vehicle.position.x = vehicle.x;
    vehicle.position.y = vehicle.y;
    //Set vehicle rotation
    vehicle.rotation.z = vehicle.carRotation + degree * 90;

    //Add to the scene and vehicle array
    scene.add(vehicle);
    vehicles.push(vehicle);

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
function destroyVehicle(event){
    
    var vehicleId = event.vehicleId;

    for (var i = 0; i < vehicles.length; i++) {

        if (vehicleId === vehicles[i].vehicleId) {
            
            vehicles[i].position.y = vehicles[i].position.y+30;           
            scene.remove(vehicles[i]);
 
            //Remove Vehicle at index i
            vehicles.splice(i,1);
            //i = i - 1;
            
            break;
        }
    }
}

