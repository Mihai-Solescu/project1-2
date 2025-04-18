package org.ken22.physics.differentiators;

import java.util.function.Function;

public class FivePointCenteredDifference implements Differentiator {
    public double differentiate(double h, double x, Function<Double, Double> f) {
        return (f.apply(x - 2 * h) - 8 * f.apply(x - h) + 8 * f.apply(x + h) - f.apply(x + 2 * h)) / (12 * h);
    }
}
