package org.ken22.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.ken22.input.courseinput.GolfCourse;
import org.ken22.input.courseinput.GolfCourseLoader;
import org.ken22.screens.ScreenManager;

import java.util.List;



public class CourseSelectorStage extends Stage {
    private ScreenManager manager;
    private GolfCourseLoader courseLoader;
    private List<GolfCourse> courses;
    private Table table;
    private ScrollPane scrollPane;
    private TextButton addButton;
    private TextButton backButton;



    public CourseSelectorStage(ScreenManager manager) {
        super(new ScreenViewport());
        this.manager = manager;
        this.courseLoader = GolfCourseLoader.getInstance();
        this.courses = courseLoader.getCourses();

        this.table = new Table();
        Skin skin = new Skin(Gdx.files.internal("skins/test/uiskin.json"));

        scrollPane = new ScrollPane(table, skin);
        scrollPane.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);

        this.addActor(scrollPane);

        for (GolfCourse course : courses) {
            Table coursePanel = createCoursePanel(course, skin);
            table.add(coursePanel).pad(10).row();
        }

        addButton = new TextButton("+", skin);
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAddCourseDialog(skin);
            }
        });
        table.add(addButton).pad(10).row();

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                manager.toMainStage();
            }
        });

        table.add(backButton).padTop(10).row();
    }





    // creating course panel
    private Table createCoursePanel(GolfCourse course, Skin skin) {
        Table coursePanel = new Table(skin);
        coursePanel.setBackground("default-round");
        coursePanel.defaults().pad(5);

        coursePanel.add(new Label("Course: " + course.name(), skin)).row();
        coursePanel.add(new Label("Height profile: " + course.courseProfile(), skin)).row();
        coursePanel.add(new Label("Start location: (" + course.ballX() + ", " + course.ballY() + ")", skin)).row();
        coursePanel.add(new Label("Target location: (" + course.targetXcoord() + ", " + course.targetYcoord() + ")", skin)).row();

        TextButton selectButton = new TextButton("Select", skin);
        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                manager.selectedCourse = course;
                manager.toMainStage();
            }
        });
        coursePanel.add(selectButton).padTop(10).row();

        TextButton deleteButton = new TextButton("Delete", skin);
        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                courses.remove(course);
                courseLoader.removeCourse(course);
                coursePanel.remove();
            }
        });
        coursePanel.add(deleteButton).padTop(10);

        return coursePanel;
    }




    // adding the new level
    private void showAddCourseDialog(Skin skin) {
        Dialog dialog = new Dialog("Add Course", skin);
        Table contentTable = dialog.getContentTable();
        contentTable.add(new Label("Name: ", skin));
        TextField nameField = new TextField("", skin);
        contentTable.add(nameField).row();
        contentTable.add(new Label("Height Profile: ", skin));
        TextField profileField = new TextField("", skin);
        contentTable.add(profileField).row();
        contentTable.add(new Label("Start X: ", skin));
        TextField startXField = new TextField("", skin);
        contentTable.add(startXField).row();
        contentTable.add(new Label("Start Y: ", skin));
        TextField startYField = new TextField("", skin);
        contentTable.add(startYField).row();
        contentTable.add(new Label("Target X: ", skin));
        TextField targetXField = new TextField("", skin);
        contentTable.add(targetXField).row();
        contentTable.add(new Label("Target Y: ", skin));
        TextField targetYField = new TextField("", skin);
        contentTable.add(targetYField).row();

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });
        dialog.button(cancelButton);

        TextButton submitButton = new TextButton("Submit", skin);
        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    String name = nameField.getText();
                    String profile = profileField.getText();
                    double startX = Double.parseDouble(startXField.getText());
                    double startY = Double.parseDouble(startYField.getText());
                    double targetX = Double.parseDouble(targetXField.getText());
                    double targetY = Double.parseDouble(targetYField.getText());

                    GolfCourse newCourse = new GolfCourse(name, profile, 100, 1, 9.81, 0.3, 0.4, 0.5, 0.6, 30, 5, targetX, targetY, startX, startY);
                    courses.add(newCourse);
                    Table newCoursePanel = createCoursePanel(newCourse, skin);
                    table.add(newCoursePanel).pad(10).row();
                    table.getCell(addButton).setActor(null);
                    table.getCell(backButton).setActor(null);
                    table.add(addButton).pad(10).row();
                    table.add(backButton).padTop(10).row();
                    dialog.hide();
                } catch (NumberFormatException e) {
                    // if bad
                }
            }
        });
        dialog.button(submitButton);

        dialog.show(this);
    }




    @Override
    public void dispose() {
        super.dispose();
        scrollPane.remove();
    }
}
