package org.ken22.players.bots.hillclimbing;

import org.ken22.input.courseinput.GolfCourse;
import org.ken22.physics.vectors.StateVector4;
import org.ken22.players.Player;
import org.ken22.players.error.ErrorFunction;
import org.ken22.utils.MathUtils;

/**
 * Basic gradient descent hill climbing bot with no sideway moves.
 */
public class LineHillClimbingBot implements Player {
    private int maxIterations = 500;
    private double errorThreshold = 0.15;
    private double convergenceThreshold = 0.001;
    private boolean converged = false;

    private GolfCourse course;
    private double stepSize;

    private Player initialGuessBot;
    private ErrorFunction errorFunction;

    private StateVector4 currentState;
    private double[] gradient = new double[2];

    public LineHillClimbingBot(Player initialGuessBot, GolfCourse course, ErrorFunction errorFunction, double stepSize) {
        this.initialGuessBot = initialGuessBot;
        this.course = course;
        this.errorFunction = errorFunction;
        this.stepSize = 0.05; //stepSize;
    }

    @Override
    public StateVector4 play(StateVector4 state) {
        if(initialGuessBot != null) {
            currentState = initialGuessBot.play(state);
        } else {
            currentState = state;
        }

        for (int i = 0; i < maxIterations; i++) {
            climb();
            if (errorFunction.calculateError(currentState) < errorThreshold) {
                break;
            }
            if (converged) {
                break;
            }
        }

        return currentState;
    }

    private void climb() {
        var e10 = errorFunction.calculateError(new StateVector4(currentState.x(), currentState.y(), currentState.vx() + stepSize, currentState.vy()));
        var e01 = errorFunction.calculateError(new StateVector4(currentState.x(), currentState.y(), currentState.vx(), currentState.vy() + stepSize));
        var e_10 = errorFunction.calculateError(new StateVector4(currentState.x(), currentState.y(), currentState.vx() - stepSize, currentState.vy()));
        var e0_1 = errorFunction.calculateError(new StateVector4(currentState.x(), currentState.y(), currentState.vx(), currentState.vy() - stepSize));

        gradient[0] = (e10 - e_10) / (2 * stepSize);
        gradient[1] = (e01 - e0_1) / (2 * stepSize);

        // gradient-descent // x = x - stepsize * gradient
//        var alpha = stepSize;
//        var currentError = errorFunction.calculateError(currentState);
//        currentState.setVx(currentState.vx() - alpha*gradient[0]);
//        currentState.setVy(currentState.vy() - alpha*gradient[1]);
//        var newError = errorFunction.calculateError(currentState);
//        while (newError < currentError) {
//            currentError = newError; alpha *= 2;
//            currentState.setVx(currentState.vx() - alpha*gradient[0]);
//            currentState.setVy(currentState.vy() - alpha*gradient[1]);
//            newError = errorFunction.calculateError(currentState);
//        }

        var alpha = stepSize;
        var currentError = errorFunction.calculateError(currentState);
        var tempState = new StateVector4(currentState.x(), currentState.y(), currentState.vx() - alpha*gradient[0], currentState.vy() - alpha*gradient[1]);
        var newError = errorFunction.calculateError(tempState);
        while (newError > currentError) {
            alpha /= 2;
            tempState = new StateVector4(currentState.x(), currentState.y(), currentState.vx() - alpha*gradient[0], currentState.vy() - alpha*gradient[1]);
            newError = errorFunction.calculateError(tempState);
        }
        currentState = tempState;

        if (MathUtils.magnitude(alpha * gradient[0], alpha * gradient[1]) < convergenceThreshold) {
            converged = true;
        }

        System.out.println("Gradient: " + gradient[0] + " " + gradient[1]);
        System.out.println("Current velocity: " + currentState.vx() + " " + currentState.vy() + "; Error: " + errorFunction.calculateError(currentState));
    }
}
