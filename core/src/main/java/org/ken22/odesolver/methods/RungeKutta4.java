package org.ken22.odesolver.methods;

import org.ken22.interfaces.IFunc;
import org.ken22.interfaces.ODESolution;
import org.ken22.interfaces.ODESystem;

import java.util.ArrayList;

public class RungeKutta4 extends ODESolverMethod {

    /**
     * Initialize the Runge Kutta 4 solver.
     * Inherited from {@link ODESolverMethod}
     *
     * @param system   the system of equations to solve
     * @param stepSize step size used by the solver
     * @param endTime  the end point of the interval to solve, namely: [initialValue, endPoint]
     */
    public RungeKutta4(ODESystem system, double stepSize, double startTime, double endTime) {
        super(system, stepSize, startTime, endTime);
    }

    @Override
    public ODESolution solve() {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<ArrayList<Double>> stateVectors = new ArrayList<>();
        ODESolution solution = new ODESolution(time, stateVectors);
        double t = startTime;
        ArrayList<Double> stateVector = system.getInitialStateVector();
        time.add(t);
        stateVectors.add(stateVector);

        long computationStartTime = System.nanoTime();

        while (t <= endTime) {
            t += stepSize;

            ArrayList<Double> k1 = new ArrayList<>();
            ArrayList<Double> k2 = new ArrayList<>();
            ArrayList<Double> k3 = new ArrayList<>();
            ArrayList<Double> k4 = new ArrayList<>();

            int j1 = 0;
            for (Double y : stateVector) {
                IFunc<Double, Double> function2 = this.system.getFunctions().get(j1);
                double newY = y.doubleValue() + stepSize * function2.apply(stateVector).doubleValue();
                k1.add(newY);
                j1++;
            }

            int j2 = 0;
            for (Double y : stateVector) {
                IFunc<Double, Double> function2 = this.system.getFunctions().get(j2);
                double halfStepSize = stepSize / 2;
                double newY = y.doubleValue() + halfStepSize/2 * function2.apply(k1).doubleValue();
                k2.add(newY);
                j2++;
            }
            // all values of k2 have to be divided by 2 before function is applied?

            int j3 = 0;
            for (Double y : stateVector) {
                IFunc<Double, Double> function2 = this.system.getFunctions().get(j3);
                double halfStepSize = stepSize / 2;
                double newY = y.doubleValue() + halfStepSize/2 * function2.apply(k2).doubleValue();
                k3.add(newY);
                j3++;
            }

            int j4 = 0;
            for (Double y : stateVector) {
                IFunc<Double, Double> function2 = this.system.getFunctions().get(j4);
                double newY = y.doubleValue() + stepSize * function2.apply(k3).doubleValue();
                k4.add(newY);
                j4++;
            }

            ArrayList<Double> updatedStateVector = new ArrayList<>();
            int i = 0;
            for (Double x : stateVector) {
                IFunc<Double, Double> function = this.system.getFunctions().get(i);
                double newX = x.doubleValue() + stepSize/6 * (function.apply(k1).doubleValue() + 2*function.apply(k2).doubleValue() + 2*function.apply(k3).doubleValue() + function.apply(k4).doubleValue());
                updatedStateVector.add(newX);
                i++;
            }

            stateVectors.add(updatedStateVector);
            time.add(t);
            stateVector = updatedStateVector;
        }

        long elapsedComputationTime = System.nanoTime() - computationStartTime;
        solution.setTimeTaken(elapsedComputationTime);

        return solution;
    }
}
