package org.ken22.players.bots.hillclimbing;

import org.ken22.input.courseinput.GolfCourse;
import org.ken22.physics.differentiators.Differentiator;
import org.ken22.physics.differentiators.FivePointCenteredDifference;
import org.ken22.physics.odesolvers.ODESolver;
import org.ken22.physics.odesolvers.RK4;
import org.ken22.physics.vectors.StateVector4;
import org.ken22.players.error.ErrorFunction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("ClassCanBeRecord")
public final class Evaluator {

    private final ErrorFunction heuristicFunction;
    private final ODESolver<StateVector4> solver;
    private final Differentiator differentiator;
    private final double stepSize;
    private final GolfCourse course;

    public Evaluator(ErrorFunction heuristicFunction, GolfCourse course) {
        this.heuristicFunction = heuristicFunction;
        this.course = course;
        this.solver = new RK4();
        this.differentiator = new FivePointCenteredDifference();
        this.stepSize = 0.0001;
    }

    public Evaluator(ErrorFunction heuristicFunction, GolfCourse course,  ODESolver<StateVector4> solver,
                     Differentiator differentiator, double stepSize) {
        this.course = course;
        this.heuristicFunction = heuristicFunction;
        this.solver = solver;
        this.differentiator = differentiator;
        this.stepSize = stepSize;
    }

    /**
     * <p>Evaluates each neighbour by the cost function h.</p>
     *
     * @param neighbours list of neighbours
     * @return Map of {@code (neighbour, h-value)} pairs
     */
    public Map<StateVector4, Double> evaluateNeighbours(List<StateVector4> neighbours) {
        return neighbours.parallelStream()
            .collect(Collectors.toMap(
                Function.identity(),
                this::evaluateState
            ));
    }

    /**
     * <p>Evaluates a state by the cost function h.</p>
     *
     * @param state the state to evaluate
     * @return h (cost function value)
     */
    public double evaluateState(StateVector4 state) {
        return heuristicFunction.calculateError(state);
    }
}
