package org.ken22.physicsx.differentiators;

import java.util.function.Function;

public interface Differentiator {
    double differentiate(double stepSize, double x, Function<Double, Double> function);
}
