package org.ken22.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ken22.input.settings.DifferentiatorType;
import org.ken22.input.settings.GeneralSettings;
import org.ken22.input.settings.ODESolverType;
import org.ken22.screens.ScreenManager;

import java.io.File;
import java.io.IOException;

public class SettingsStage extends Stage {

    private ScreenManager manager;

    private static Viewport viewport = new ScreenViewport();
    private Table table;
    private ScrollPane scrollPane;
    private SelectBox<ODESolverType> odeSolverBox;
    private SelectBox<DifferentiatorType> differentiatorBox;

    private Skin skin;

    private TextField stepSizeField;
    private CheckBox simplifiedPhysicsCheckBox;
    private CheckBox allowPlayingCheckBox;

    // data holder for the settings
    private GeneralSettings settings;

    public SettingsStage(ScreenManager manager, GeneralSettings settings) {
        super(viewport);
        this.settings = settings;
        this.manager = manager;
        this.table = new Table();
        this.table.setFillParent(true);

        skin = new Skin(Gdx.files.internal("skins/test/uiskin.json"));

        scrollPane = new ScrollPane(table, skin);
        scrollPane.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);

        this.addActor(scrollPane);

        // ODE solver
        table.add(new Label("ODE Solver", skin)).pad(10);
        odeSolverBox = new SelectBox<>(skin);
        odeSolverBox.setItems(ODESolverType.values());
        table.add(odeSolverBox).pad(10).row();

        // step size
        table.add(new Label("Step Size", skin)).pad(10);
        stepSizeField = new TextField("", skin);
        table.add(stepSizeField).pad(10).row();


        // differentiator type
        table.add(new Label("Differentiator", skin)).pad(10);
        differentiatorBox = new SelectBox<>(skin);
        differentiatorBox.setItems(DifferentiatorType.values());
        table.add(differentiatorBox).pad(10).row();

        // physics
        simplifiedPhysicsCheckBox = new CheckBox(" Simplified Physics", skin);
        table.add(simplifiedPhysicsCheckBox).pad(10).colspan(2).left().row();

        // playing
        allowPlayingCheckBox = new CheckBox(" Allow Playing", skin);
        table.add(allowPlayingCheckBox).pad(10).colspan(2).left().row();

        // save button
        TextButton saveButton = new TextButton("Save", skin);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveSettings();
            }
        });
        table.add(saveButton).pad(10).colspan(2).center().row();

        // back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                manager.toMainStage();
            }
        });
        table.add(backButton).pad(10).colspan(2).center().row();

        loadButtons();
    }

    // save settings
    private void saveSettings() {

        // copy the settings into the settings object
        settings.solverType = odeSolverBox.getSelected();
        settings.stepSize = Double.parseDouble(stepSizeField.getText());
        settings.differentiatorType = differentiatorBox.getSelected();
        settings.useSimplifiedPhysics = simplifiedPhysicsCheckBox.isChecked();
        settings.allowPlaying = allowPlayingCheckBox.isChecked();

        // save the new settings in the .json

        // Note: everything is written into the default settings, meaning there currently aren't multiple settings
        // presets to choose from (unlike the courses, for instance).
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(new File("input/settings/default-settings.json"), settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the settings values from the default file.
     */
    private void loadButtons() {
            // make the UI reflect the loaded settings
            odeSolverBox.setSelected(settings.solverType);
            stepSizeField.setText(String.valueOf(settings.stepSize));
            differentiatorBox.setSelected(settings.differentiatorType);
            simplifiedPhysicsCheckBox.setChecked(settings.useSimplifiedPhysics);
            allowPlayingCheckBox.setChecked(settings.allowPlaying);
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
    }

    public Viewport getViewport() {
        return viewport;
    }
}
