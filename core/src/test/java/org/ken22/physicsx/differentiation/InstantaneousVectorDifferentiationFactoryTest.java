package org.ken22.physicsx.differentiation;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.junit.jupiter.api.Test;
import org.ken22.input.courseinput.GolfCourse;
import org.ken22.physicsx.TestUtils;
import org.ken22.physicsx.differentiators.FivePointCenteredDifference;
import org.ken22.physicsx.vectors.StateVector4;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InstantaneousVectorDifferentiationFactoryTest {

    @Test
    void instantaneousVectorDifferentiation4() {
        GolfCourse course = TestUtils.course("golf-course.json");
        Expression expr = TestUtils.expr(course);

        InstantaneousVectorDifferentiationFactory factory =
            new InstantaneousVectorDifferentiationFactory(1, expr, course, new FivePointCenteredDifference());
        InstantaneousVectorDifferentiation4 ivd = factory.instantaneousVectorDifferentiation4();

        StateVector4 initial = new StateVector4(4.0, 4.0, 1.0, 1.0);
        StateVector4 result = ivd.apply(initial);

        System.out.println("Initial: " + initial);
        System.out.println("Result: " + result);

        double d = 0.00000000000000000000000001;
        assertEquals(1.0, result.x(), d);
        assertEquals(1.0, result.y(), d);
        assertEquals(-10.0*2-10*2*1/Math.sqrt(2), result.vx(), d);
        assertEquals(-10.0-10*2*1/Math.sqrt(2), result.vy(), d);
    }

    @Test
    void altInstantaneousVectorDifferentiation4() {
        GolfCourse course = TestUtils.course("golf-course.json");
        Expression expr = TestUtils.expr(course);

        InstantaneousVectorDifferentiationFactory factory =
            new InstantaneousVectorDifferentiationFactory(0.00001, expr, course, new FivePointCenteredDifference());
        InstantaneousVectorDifferentiation4 ivd = factory.altInstantaneousVectorDifferentiation4();

        StateVector4 initial = new StateVector4(4.0, 4.0, 1.0, 1.0);
        StateVector4 result = ivd.apply(initial);

        double d = 0.000001;
        assertEquals(1.0, result.x(), d);
        assertEquals(1.0, result.y(), d);
        assertEquals(-10.0*2-10*2*2/Math.sqrt(5), result.vx(), d);
        assertEquals(-10.0-10*2*1/Math.sqrt(5), result.vy(), d);
    }

    @Test
    void xSlope() {
    }

    @Test
    void ySlope() {
    }
}
