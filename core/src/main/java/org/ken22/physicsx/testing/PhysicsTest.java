package org.ken22.physicsx.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.ken22.input.courseinput.GolfCourse;
import org.ken22.physicsx.engine.PhysicsEngine;
import org.ken22.physicsx.vectors.StateVector4;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PhysicsTest {
    public static void main(String[] args) {
        GolfCourse course = course("golf-course.json");
        Expression expr = expr(course);

        StateVector4 initialState = new StateVector4(-3.0, 0.0, 4.491572403911195, 1.6239689874674115);
        PhysicsEngine engine = new PhysicsEngine(course, initialState);

        while(!engine.isAtRest())
            engine.nextStep();

        String filePath = "assets/enginetest2.csv";
        System.out.println("Starting write");
        writeToCSV(engine.getTrajectory(), filePath);
    }

    private static void writeToCSV(ArrayList<StateVector4> trajectory, String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println("x,y,vx,vy");

            for (StateVector4 vector : trajectory) {
                writer.println(vector.x() + "," + vector.y() + "," + vector.vx() + "," + vector.vy());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing the trajectory to a CSV file.");
            e.printStackTrace();
        }
    }

    public static GolfCourse course(String fileName) {
        GolfCourse course;

        File resourcesDirectory = new File("assets/input");
        File courseFile = new File(resourcesDirectory, fileName);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            course = objectMapper.readValue(courseFile, GolfCourse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return course;
    }
    public static Expression expr(GolfCourse course) {
        return new ExpressionBuilder(course.courseProfile())
            .variables("x", "y")
            .build();
    }
}
