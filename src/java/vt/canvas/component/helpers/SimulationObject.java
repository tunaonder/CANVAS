/*
 * Created by Sait Tuna Onder on 2017.03.08  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.component.helpers;

/**
 *
 * @author Onder
 */

public abstract class SimulationObject {

    private final String id;
    private double x;
    private double y;

    public SimulationObject(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return Distance between two coordinates
     */
    public double calculateDistance(double x1, double y1, double x2, double y2) {

        double xDistance = Math.abs(x1 - x2);
        double yDistance = Math.abs(y1 - y2);

        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);

    }
}
