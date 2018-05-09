/*
 * Created by Sait Tuna Onder on 2017.03.18  * 
 * Copyright © 2017 Sait Tuna Onder. All rights reserved. * 
 */
package vt.canvas.execution.random;

import java.util.Random;

/**
 * An instance of this class is used to generate a stream of pseudorandom,
 * uniformally distributed numbers. The class uses the java.util.Random class of
 * the JDK which uses a 48-bit seed that is then modified using a linear
 * congruential formula. (See Donald Knuth. <I>The Art of Computer Programming,
 * Volume 2,</I> Section 3.2.1.)
 *
 * @author Onder
 */
public class UniformRandom implements RandomVariate {

    protected Random rng;
    protected double lowerBound;
    protected double upperBound;
    protected boolean started;

    public UniformRandom() {
        rng = new Random(); // initialize the RNG
        started = false;
        this.lowerBound = 0;
        this.upperBound = 0;
    }

    /**
     * Creates a new uniformally distributed random number generator with the
     * given lower and upper bounds.
     *
     * @param lowerBound
     * @param upperBound
     */
    public UniformRandom(double lowerBound, double upperBound) {
        this();
        this.lowerBound = lowerBound;	// set the distribution lower bound
        this.upperBound = upperBound;	// set the distribution upper bound
    }

    /**
     * Returns the next pseudorandom, uniformally distributed
     * <code>double</code> value from the random number generators sequence.
     *
     * @return
     */
    @Override
    public double nextDouble() {
        started = true;	// the rng has started

        double rand = rng.nextDouble();	//retrieve a uniformly distributed random variate - U(0,1)

        // move the variate into the distibution range and return the value.
        return ((upperBound - lowerBound) * rand + lowerBound);
    }

    public double nextDouble(double upper, double lower) {
       

        double rand = rng.nextDouble();	//retrieve a uniformly distributed random variate - U(0,1)

        // move the variate into the distibution range and return the value.
        return ((upper - lower) * rand + lower);
    }

    /**
     * @return
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * @return
     */
    public double getUpperBound() {
        return upperBound;
    }

    /**
     * @param d
     */
    public void setLowerBound(double d) {
        lowerBound = d;
    }

    /**
     * @param d
     */
    public void setUpperBound(double d) {
        upperBound = d;
    }
}
