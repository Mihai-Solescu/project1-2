package org.ken22.physicsx.utils;

import net.objecthunter.exp4j.Expression;
import org.ken22.physicsx.differentiation.Differentiator;

import java.util.function.Function;

public class PhysicsUtils {
    public static double magnitude(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    public static double xSlope(int xCoord, int yCoord, double h, Expression expr, Differentiator differentiator) {
        // Define the univariate functions at the x coordinate
        Function<Double, Double> fx = (x) -> expr.setVariable("y", yCoord).setVariable("x", x).evaluate();
        // Aproximate the derivatives of the functions at the x coordinate
        return differentiator.differentiate(h, xCoord, fx);
    }

    public static double ySlope(int xCoord, int yCoord, double h, Expression expr, Differentiator differentiator) {
        // Define the univariate functions at the y coordinate
        Function<Double, Double> fy = (y) -> expr.setVariable("x", xCoord).setVariable("y", y).evaluate();
        // Aproximate the derivatives of the functions at the y coordinate
        return differentiator.differentiate(h, yCoord, fy);
    }
}
