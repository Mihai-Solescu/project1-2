package org.ken22.players;

import net.objecthunter.exp4j.Expression;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ken22.input.courseinput.GolfCourse;
import org.ken22.TestUtils;

class PlanarBot2Test {

    static GolfCourse course;
    static Expression expr;

    @BeforeAll
    static void setUp() {
        course = TestUtils.course("sin(x)sin(y).json");
        expr = TestUtils.expr(course);
    }

    @Test
    void play() {
//        SimplePlaneBot2 bot = new SimplePlaneBot2();
//        var result = bot.play(new StateVector4(0, 0, 1, 0), course);
//        var expected = TestUtils.prediction.apply(result, course);
//        System.out.println(expected);
    }
}
