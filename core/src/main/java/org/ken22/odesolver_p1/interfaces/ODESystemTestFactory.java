package org.ken22.odesolver_p1.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;


public class ODESystemTestFactory {
    ODESystem system(HashMap<String, Integer> varOrder, ArrayList<Double> initialStateVector, ArrayList<IFunc<Double, Double>> functions) {
        return new ODESystem(varOrder, initialStateVector, functions);
    }

    public ODESystem LotkaVolterra(double initialX, double initialY, double alpha, double beta,
                                   double delta, double gamma) {

        if (alpha <= 0 || beta <= 0 || gamma <= 0 || delta <= 0) {
            throw new IllegalArgumentException("alpha, beta, gamma, and delta must be positive");
        }

        ArrayList<Double> initialStateVector = new ArrayList<>();
        ArrayList<IFunc<Double, Double>> functions = new ArrayList<>();

        // initial conditions
        ArrayList<Double> initialValues = new ArrayList<>();
        initialValues.add(initialX);
        initialValues.add(initialY);

        initialStateVector.add(initialValues.get(0));
        initialStateVector.add(initialValues.get(1));


        functions.add((systemVars) -> {
            return alpha * systemVars.get(0).doubleValue() - beta * systemVars.get(0).doubleValue()
                * systemVars.get(1).doubleValue();
        });

        functions.add((systemVars) -> {
            return delta * systemVars.get(0).doubleValue() * systemVars.get(1).doubleValue() -
                gamma * systemVars.get(1).doubleValue();
        });

        HashMap<String, Integer> varOrder = new HashMap<>() {
            {
                put("X", 0);
                put("Y", 1);
            }
        };

        return new ODESystem(varOrder, initialStateVector, functions);
    }

    public ODESystem LotkaVolterra(double initialX, double initialY) {

        double alpha = 1;
        double beta = 1;
        double delta = 1;
        double gamma = 1;
        ArrayList<Double> initialStateVector = new ArrayList<>();
        ArrayList<IFunc<Double, Double>> functions = new ArrayList<>();

        // initial conditions
        ArrayList<Double> initialValues = new ArrayList<>();
        initialValues.add(initialX);
        initialValues.add(initialY);

        initialStateVector.add(initialValues.get(0));
        initialStateVector.add(initialValues.get(1));


        functions.add((systemVars) -> {
            return alpha * systemVars.get(0).doubleValue() - beta * systemVars.get(0).doubleValue()
                * systemVars.get(1).doubleValue();
        });

        functions.add((systemVars) -> {
            return delta * systemVars.get(0).doubleValue() * systemVars.get(1).doubleValue() -
                gamma * systemVars.get(1).doubleValue();
        });

        HashMap<String, Integer> varOrder = new HashMap<>() {
            {
                put("X", 0);
                put("Y", 1);
            }
        };

        return new ODESystem(varOrder, initialStateVector, functions);
    }

    public ODESystem FitzHughNagumo() {
        ArrayList<Double> initialStateVector = new ArrayList<>();
        ArrayList<IFunc<Double, Double>> functions = new ArrayList<>();
        ArrayList<Double> vars = new ArrayList<>();
        vars.add(1.0);
        vars.add(0.0);
        initialStateVector.add(vars.get(0));
        functions.add((systemVars) -> {
            return systemVars.getFirst().doubleValue() - Math.pow(systemVars.getFirst().doubleValue(), 3) / 3 - systemVars.get(1).doubleValue() + 0.1;
        });
        functions.add((systemVars) -> {
            return 0.05 * (systemVars.getFirst().doubleValue() + 0.95 - 0.91 * systemVars.get(1).doubleValue() + 0.1);
        });

        HashMap<String, Integer> varOrder = new HashMap<>() {
            {
                put("V", 0);
                put("W", 1);
            }
        };

        return new ODESystem(varOrder, initialStateVector, functions);
    }

    public ODESystem SIR(double initialS, double initialI, double initialR, double k, double mu, double gamma) {
        ArrayList<Double> initialStateVector = new ArrayList<>();
        ArrayList<IFunc<Double, Double>> functions = new ArrayList<>();

        // initial conditions
        ArrayList<Double> initialValues = new ArrayList<>();
        initialValues.add(initialS);
        initialValues.add(initialI);
        initialValues.add(initialR);

        initialStateVector.add(initialValues.get(0));
        initialStateVector.add(initialValues.get(1));
        initialStateVector.add(initialValues.get(2));


        functions.add((systemVars) -> {
            return -k * systemVars.get(0).doubleValue() * systemVars.get(1).doubleValue() +
                mu * (1 - systemVars.get(0).doubleValue());
        });

        functions.add((systemVars) -> {
            return k * systemVars.get(0).doubleValue() * systemVars.get(1).doubleValue() -
                (gamma + mu) * systemVars.get(1).doubleValue();
        });

        functions.add((systemVars) -> {
            return gamma * systemVars.get(1).doubleValue() - mu * systemVars.get(2).doubleValue();
        });

        HashMap<String, Integer> varOrder = new HashMap<>() {
            {
                put("S", 0);
                put("I", 1);
                put("R", 2);
            }

        };

        return new ODESystem(varOrder, initialStateVector, functions);
    }

    public ODESystem SIR(double initialS, double initialI, double initialR) {

        double k = 3;
        double mu = 0.001;
        double gamma = 2;

        ArrayList<Double> initialStateVector = new ArrayList<>();
        ArrayList<IFunc<Double, Double>> functions = new ArrayList<>();

        // initial conditions
        ArrayList<Double> initialValues = new ArrayList<>();
        initialValues.add(initialS);
        initialValues.add(initialI);
        initialValues.add(initialR);

        initialStateVector.add(initialValues.get(0));
        initialStateVector.add(initialValues.get(1));
        initialStateVector.add(initialValues.get(2));


        functions.add((systemVars) -> {
            return -k * systemVars.get(0).doubleValue() * systemVars.get(1).doubleValue() +
                mu * (1 - systemVars.get(0).doubleValue());
        });

        functions.add((systemVars) -> {
            return k * systemVars.get(0).doubleValue() * systemVars.get(1).doubleValue() -
                (gamma + mu) * systemVars.get(1).doubleValue();
        });

        functions.add((systemVars) -> {
            return gamma * systemVars.get(1).doubleValue() - mu * systemVars.get(2).doubleValue();
        });

        HashMap<String, Integer> varOrder = new HashMap<>() {
            {
                put("S", 0);
                put("I", 1);
                put("R", 2);
            }
        };

        return new ODESystem(varOrder, initialStateVector, functions);

    }

    public static void main(String[] args) throws IOException {
        // ODESystemTestFactory factory = new ODESystemTestFactory();
        // ODESystem syst = factory.testSyst();
        // System.out.println(syst + "\n" + syst.derivative());

        // ODESolver solverContext = new ODESolver();
        // ODESolverMethod strat = new RungeKutta2(syst, 0.00001, 0, 100);
        // solverContext.setStrategy(strat);
        // ODESolution solution = solverContext.solve();
        // System.out.println("Initial state vector");
        // System.out.println(solution.stateVectors.get(100000));
        // ex 2

    }
}
