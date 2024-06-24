package org.ken22.players.bots;

import org.ken22.input.settings.BotSettings;
import org.ken22.input.courseinput.GolfCourse;
import org.ken22.physics.PhysicsFactory;
import org.ken22.players.Player;
import org.ken22.players.bots.hillclimbing.GradientDescent;
import org.ken22.players.bots.hillclimbing.SimulatedAnnealing;
import org.ken22.players.error.ErrorFunction;

public class BotFactory {
    private BotSettings settings;
    private PhysicsFactory physicsFactory;

    public BotFactory(BotSettings settings, PhysicsFactory physicsFactory) {
        this.settings = settings;
        this.physicsFactory = physicsFactory;
    }

    public BasicHillClimbingBot hillClimbingBot(GolfCourse course, Player initialGuessBot) {
        return new BasicHillClimbingBot(initialGuessBot, course, errorFunction(course), settings.stepSize);
    }

    public GradientDescent gradientDescent(GolfCourse course) {
        return new GradientDescent(0.01, course.targetRadius, settings.sidewaysMoves, settings.randomRestarts,
            course, settings.odesolverType.getSolver(), settings.differentiatorType.getDifferentiator(),
            settings.stepSize, errorFunction(course), physicsFactory);
    }

    public SimulatedAnnealing simulatedAnnealing(GolfCourse course)  {
        return new SimulatedAnnealing(course, settings.odesolverType.getSolver(),
            settings.differentiatorType.getDifferentiator(), settings.stepSize, 100,10000, errorFunction(course));
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

    private ErrorFunction errorFunction(GolfCourse course) {
        ErrorFunction errorFunction = settings.errorFunctionType
            .getErrorFunction(course, physicsFactory, settings.gridPathfindingType.getPathfinding(), settings.weightingType.getWeighting());
        errorFunction.init(course, physicsFactory);
        return errorFunction;
    }
}
