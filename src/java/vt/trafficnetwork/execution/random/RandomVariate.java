/*
 * Created by Sait Tuna Onder on 2017.03.18  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.trafficnetwork.execution.random;

/**
 *
 * @author Onder
 */
public interface RandomVariate {
    
    public double nextDouble();
    
    public double nextDouble(double lower, double upper);
    
}
