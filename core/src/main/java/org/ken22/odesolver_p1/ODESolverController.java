package org.ken22.odesolver_p1;

import org.ken22.odesolver_p1.interfaces.ODESolution;
import org.ken22.odesolver_p1.odeinput.ODESystemFactory;
import org.ken22.odesolver_p1.interfaces.ODESystem;
import org.ken22.odesolver_p1.interfaces.UserInput;
import org.ken22.odesolver_p1.methods.ODESolver;
import org.ken22.odesolver_p1.methods.EulerMethod;
import org.ken22.odesolver_p1.methods.ODESolverMethod;
import org.ken22.odesolver_p1.methods.RungeKutta4;
import org.ken22.odesolver_p1.ui.InputPage;
import org.ken22.odesolver_p1.ui.PhaseSpace;
import org.ken22.odesolver_p1.ui.Table;


public class ODESolverController {

    private PhaseSpace phaseSpace;
    private Table table;
    //private final GraphPanel graphPanel;

    public static void main(String[] args) {
        ODESolverController app = new ODESolverController();
        app.run();
    }

    public void run() {
        InputPage InputPage = new InputPage(this);
    }

    public void onGenerate(UserInput input) {

        ODESystemFactory gen = new ODESystemFactory(input.initialValuesMap, input.equations);
        ODESystem syst = gen.getSyst();
        ODESolver solver = new ODESolver();

        System.out.println(syst);

        ODESolverMethod strategy;
        switch(input.methodType) {
            case EULER:
                strategy = new EulerMethod(syst, input.stepSize, input.startTime, input.endTime);
                break;
            case RUNGE_KUTTA_2:
                strategy = new RungeKutta4(syst, input.stepSize, input.startTime, input.endTime);
                break;
            case RUNGE_KUTTA_4:
                strategy = new RungeKutta4(syst, input.stepSize, input.startTime, input.endTime);
                break;
            default:
                //unreachable
                strategy = null;
        }

        solver.setStrategy(strategy);
        ODESolution solution = solver.solve();
        System.out.println(solution);

        if(input.graph) {
            //
        }
        if (input.table) {
            table = new Table(syst.getVariables(), solution);
            table.setVisible(true);
        }
        //System.out.println(input.phase);
        if (input.phase) {
            phaseSpace = new PhaseSpace(syst, solution);
            phaseSpace.setVisible(true);
        }
    }
}
