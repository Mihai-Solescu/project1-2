package org.ken22.players.bots;

import org.ken22.input.settings.BotSettings;
import org.ken22.input.courseinput.GolfCourse;
import org.ken22.input.settings.ErrorFunctionType;
import org.ken22.physics.PhysicsFactory;
import org.ken22.players.Player;
import org.ken22.players.bots.newtonraphson.BasicNewtonRaphsonBot;
import org.ken22.players.bots.newtonraphson.NewtonRaphsonBot;
import org.ken22.players.bots.simulatedannealing.GradientDescent;
import org.ken22.players.bots.simulatedannealing.SimulatedAnnealing;
import org.ken22.players.bots.hillclimbing.HillClimbingBot;
import org.ken22.players.bots.hillclimbing.LineHillClimbingBot;
import org.ken22.players.bots.hillclimbing.RandomRestartHillClimbingBot;
import org.ken22.players.bots.simplebots.InitialGuessBot;
import org.ken22.players.bots.simplebots.SimplePlanarApproximationBot;
import org.ken22.players.error.ErrorFunction;
import org.ken22.players.error.PathfindingError;

public class BotFactory {
    private BotSettings settings;
    private PhysicsFactory physicsFactory;

    public BotFactory(BotSettings settings, PhysicsFactory physicsFactory) {
        this.settings = settings;
        this.physicsFactory = physicsFactory;
    }

    public HillClimbingBot hillClimbingBot(GolfCourse course, Player initialGuessBot) {
        return new HillClimbingBot(initialGuessBot, course, errorFunction(course), settings.stepSize);
    }

    public GradientDescent gradientDescent(GolfCourse course) {
        return new GradientDescent(0.01, course.targetRadius, settings.sidewaysMoves, settings.randomRestarts,
            course, settings.odesolverType.getSolver(), settings.differentiatorType.getDifferentiator(),
            settings.stepSize, errorFunction(course), physicsFactory);
    }

    public SimulatedAnnealing simulatedAnnealing(GolfCourse course)  {
        return new SimulatedAnnealing(course, settings.odesolverType.getSolver(),
            settings.differentiatorType.getDifferentiator(), settings.stepSize, 1000,1000, pathfindingErrorFunction(course)); //TODO : temp
    }

    public NewtonRaphsonBot newtonRaphsonBot(GolfCourse course, Player initialGuessBot) {
        return new NewtonRaphsonBot(initialGuessBot, errorFunction(course), settings.stepSize);
    }

    public SimplePlanarApproximationBot planarApproximationBot(GolfCourse course) {
        return new SimplePlanarApproximationBot(course);
    }

    public InitialGuessBot initialGuessBot(GolfCourse course) {
        return new InitialGuessBot(course);
    }

    public ErrorFunction errorFunction(GolfCourse course) {
        ErrorFunction errorFunction = settings.errorFunctionType
            .getErrorFunction(course, physicsFactory, settings.gridPathfindingType.getPathfinding(), settings.weightingType.getWeighting());
        return errorFunction;
    }

    public ErrorFunction pathfindingErrorFunction(GolfCourse course) {
        ErrorFunction errorFunction = ErrorFunctionType.PATHFINDING
            .getErrorFunction(course, physicsFactory, settings.gridPathfindingType.getPathfinding(), settings.weightingType.getWeighting());
        return errorFunction;
    }

    public RandomRestartHillClimbingBot randomRestartHillClimbingBot(GolfCourse course, InitialGuessBot initialGuessBot) {
        return new RandomRestartHillClimbingBot(initialGuessBot, course, errorFunction(course), settings.stepSize);
    }

    public LineHillClimbingBot lineHillClimbingBot(GolfCourse course, InitialGuessBot initialGuessBot) {
        return new LineHillClimbingBot(initialGuessBot, course, pathfindingErrorFunction(course), settings.stepSize); //TODO temp
        //return new LineHillClimbingBot(initialGuessBot, course, errorFunction(course), settings.stepSize);
    }

    public BasicNewtonRaphsonBot basicNewtonRaphsonBot(GolfCourse course, InitialGuessBot initialGuessBot) {
        return new BasicNewtonRaphsonBot(initialGuessBot, errorFunction(course), settings.stepSize);
    }
}
