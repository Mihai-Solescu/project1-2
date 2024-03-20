package interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import odesolver.methods.SolverMethodType;

public class UserInput {
    public final HashMap<String, Number> initialValuesMap;
    public final ArrayList<String> equations;
    public final double stepSize;
    public final double startTime;
    public final double endTime;
    public final boolean graph;
    public final boolean table;
    public final boolean phase;
    public final SolverMethodType methodType;

    public UserInput(HashMap<String, Number> initialValuesMap, ArrayList<String> equations, double stepSize, double startTime, double endTime, boolean graph, boolean table, boolean phase, SolverMethodType methodType) {
        this.initialValuesMap = initialValuesMap;
        this.equations = equations;
        this.stepSize = stepSize;
        this.startTime = startTime;
        this.endTime = endTime;
        this.graph = graph;
        this.table = table;
        this.phase = phase;
        this.methodType = methodType;
    }
}