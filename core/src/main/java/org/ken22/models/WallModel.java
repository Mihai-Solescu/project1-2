package org.ken22.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import org.ken22.obstacles.Wall;



public class WallModel {
    private ModelInstance wallInstance;




    public WallModel(Wall wall) {
        ModelBuilder modelBuilder = new ModelBuilder();

        float startX = (float) wall.startPoint()[0];
        float startY = (float) wall.startPoint()[1];
        float endX = (float) wall.endPoint()[0];
        float endY = (float) wall.endPoint()[1];
        float thickness = (float) wall.thickness();

        //length of the wall
        float length = (float) Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));

        //the wall HEIGHT IS HERE
        Model wallModel = modelBuilder.createBox(length, 0.5f, thickness,
            new Material(ColorAttribute.createDiffuse(Color.GRAY)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);





        wallInstance = new ModelInstance(wallModel);

        // rotation angle and position of the wall
        float angle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        float centerX = (startX + endX) / 2;
        float centerY = (startY + endY) / 2;

        //setting the position and rotation
        wallInstance.transform.setToTranslation(centerX, 0.5f, centerY);
        wallInstance.transform.rotate(0, 1, 0, -angle);
    }

    public ModelInstance getWallInstance() {
        return wallInstance;
    }
}
