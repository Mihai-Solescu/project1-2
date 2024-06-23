package org.ken22.players.bots.hillclimbing;

import org.ken22.input.courseinput.GolfCourse;
import org.ken22.physics.differentiators.Differentiator;
import org.ken22.physics.differentiators.FivePointCenteredDifference;
import org.ken22.physics.odesolvers.ODESolver;
import org.ken22.physics.odesolvers.RK4;
import org.ken22.physics.vectors.StateVector4;
import org.ken22.players.bots.LinearSchedule;
import org.ken22.players.bots.Schedule;
import org.ken22.players.error.ErrorFunction;
import org.ken22.players.error.EuclideanError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>This class contains an implementation of simulated annealing.</p>
 *
 * <p>Various schedule functions are supported, via the {@link Schedule} interface.</p>
 *
 *
 * <p><b>References: </b></p>
 * <ul>
 *     <i>Artificial Intelligence: A Modern Approach 3rd ed. (Chapter 4)</i>
 *     <i>Nourani, Y., & Andresen, B. (1998). A comparison of simulated annealing cooling strategies.
 *     Journal of Physics. A, Mathematical and General/Journal of Physics. A, Mathematical and General, 31(41),
 *     8373–8385. <a href="https://doi.org/10.1088/0305-4470/31/41/011">https://doi.org/10.1088/0305-4470/31/41/011</a></i>
 *
 * </ul>
 */
public final class SimulatedAnnealing {
    private static final Logger LOGGER = Logger.getLogger(GradientDescent.class.getName());

    static {

        // the default level is INFO
        // if you want to change logging, just change the enum type at (1) and (2)
        // https://docs.oracle.com/javase/8/docs/api/java/util/logging/Level.html
        LOGGER.setLevel(Level.FINER); // (1)


        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINE); // (2)
        LOGGER.addHandler(consoleHandler);
    }

    private final double DELTA;
    private final double THRESHOLD;
    private final double initialTemperature;
    private final Schedule schedule;
    private final GolfCourse course;
    private final ODESolver<StateVector4> solver;
    private final Differentiator differentiator;
    private final double stepSize;

    private final StateVector4 initialState;
    private final ErrorFunction heuristicFunction;
    private final Evaluator evaluator;

    public SimulatedAnnealing(GolfCourse course) {
        this.course = course;
        this.solver = new RK4();
        this.differentiator = new FivePointCenteredDifference();
        this.stepSize = 0.0001;
        this.DELTA = 0.01;
        this.THRESHOLD = course.targetRadius;
        this.heuristicFunction = new EuclideanError();
        this.heuristicFunction.init(this.course);
        this.evaluator = new Evaluator(this.heuristicFunction, this.course);

        this.initialTemperature = 100;
        this.schedule = new LinearSchedule(initialTemperature, 0.8);

        var initialX = course.ballX();
        var initialY = course.ballY();

        // choose random speed vector to start with
        var speedVector = getRandomSpeedVector();
        initialState = new StateVector4(initialX, initialY, speedVector[0], speedVector[1]);
    }

    public SimulatedAnnealing(GolfCourse course,
                              ODESolver<StateVector4> solver,
                              Differentiator differentiator,
                              double stepSize,
                              ErrorFunction errorFunction) {
        this.course = course;
        this.solver = solver;
        this.differentiator = differentiator;
        this.stepSize = stepSize;
        this.DELTA = 0.01;
        this.THRESHOLD = course.targetRadius;
        this.heuristicFunction = errorFunction;
        this.heuristicFunction.init(this.course);
        this.evaluator = new Evaluator(this.heuristicFunction, this.course, this.solver, this.differentiator,
            this.stepSize);

        this.initialTemperature = 100;
        this.schedule = new LinearSchedule(initialTemperature, 0.8);

        var initialX = course.ballX();
        var initialY = course.ballY();

        // choose random speed vector to start with
        var speedVector = getRandomSpeedVector();
        initialState = new StateVector4(initialX, initialY, speedVector[0], speedVector[1]);
    }

    private StateVector4 search(StateVector4 state) {
        // flag that stores whether a solution was found
        // if the search stops before a solution is found, a logging message is displayed
        boolean foundSolution = false;
        double temperature = initialTemperature;
        var current = state;
        // simulated annealing
        for (long t = 1; t < Integer.MAX_VALUE; t++) {
            temperature = schedule.getNewTemperature(t);
            if (temperature == 0) {
                LOGGER.log(Level.INFO, "Temperature reached 0, returning current state");
                return current;
            }

            var next = getRandomNeighbour(current);
            var nextValue = evaluator.evaluateState(next);

            // check if a solution is reached
            if (nextValue < THRESHOLD) {
                current = next;
                foundSolution = true;
                break;
            }

            var deltaE = nextValue - evaluator.evaluateState(current);


            if (deltaE < 0) {
                current = next; // if the evaluation is improved, accept immediately
            } else {
                double probability = Math.exp(deltaE / temperature);
                if (Math.random() < probability) {
                    current = next;
                }
            }
        }

        String message;
        if (foundSolution) {
            message = "Found solution! " + "vx: " + current.vx() + ", vy: " + current.vy();
        } else {
            message = "Solution not found but temperature reached 0 ";
        }
        LOGGER.log(Level.INFO, message);

        return current;
    }


    private StateVector4 getRandomNeighbour(StateVector4 currentState) {

        final double initialX = currentState.x();
        final double initialY = currentState.y();

        // we are in a 2-d space, so we have 4 neighbours
        var neighbour1 = new StateVector4(initialX, initialY,
            currentState.vx() + DELTA, currentState.vy());
        var neighbour2 = new StateVector4(initialX, initialY,
            currentState.vx() - DELTA, currentState.vy());


        var neighbour3 = new StateVector4(initialX, initialY,
            currentState.vx(), currentState.vy() + DELTA);
        var neighbour4 = new StateVector4(initialX, initialY,
            currentState.vx(), currentState.vy() - DELTA);

        ArrayList<StateVector4> neighbours = new ArrayList<>();
        neighbours.add(neighbour1);
        neighbours.add(neighbour2);
        neighbours.add(neighbour3);
        neighbours.add(neighbour4);

        // ensures a random neighbour is always returned
        Collections.shuffle(neighbours);
        return neighbours.getFirst();
    }

    /**
     * <p>Generate a random speed vector.</p>
     * Ensures that the magnitude of the vector does not exceed 5m/s
     *
     * @return randomly generated speed vector (vx, vy)
     */
    private double[] getRandomSpeedVector() {
        double[] vector = new double[2];
        Random random = new Random();
        vector[0] = random.nextDouble() * 5 * 2 - 5; // random number between -5 and 5
        double x = Math.sqrt(25-vector[0]*vector[0]);
        vector[1] = random.nextDouble()*2*x-x; // random number between -5 and 5
        return vector;

    }
}
