package interfaces;

import java.util.ArrayList;

public class ODESolution {
    ArrayList<Number> time;
    ArrayList<ArrayList<Number>> stateVectors;
    /**
     * The time taken (in ms) to compute the solution.
     */
    private long timeTaken;

    public ODESolution(ArrayList<Number> time, ArrayList<ArrayList<Number>> stateVectors) {
        this.time = time;
        this.stateVectors = stateVectors;
    }

    public ArrayList<ArrayList<Number>> getStateVectors() {
        return stateVectors;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public String toString() {
        String s = "Time taken: " + timeTaken + "\n";
        s += "Final state: " + stateVectors.get(stateVectors.size()-1) + "";
        return s;
    }
}
