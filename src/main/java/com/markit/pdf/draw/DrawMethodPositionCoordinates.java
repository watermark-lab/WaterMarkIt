package com.markit.pdf.draw;

import com.markit.api.PositionCoordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class DrawMethodPositionCoordinates extends PositionCoordinates {
    private final int imageWidth;
    private final int imageHeight;
    private final int watermarkWidth;
    private final int watermarkHeight;
    private final int MIN_X_EDGE_SIZE = 30;
    private final int MIN_Y_EDGE_SIZE = 100;

    public DrawMethodPositionCoordinates(int iw, int ih, int ww, int wh) {
        this.imageWidth = iw;
        this.imageHeight = ih;
        this.watermarkWidth = ww;
        this.watermarkHeight = wh;
    }

    @Override
    public Coordinates center(){
        return new Coordinates((imageWidth - watermarkWidth) / 2, (imageHeight - watermarkHeight) / 2 );
    }

    @Override
    public Coordinates topLeft(){
        return new Coordinates(MIN_X_EDGE_SIZE, MIN_Y_EDGE_SIZE);
    }

    @Override
    public Coordinates topRight() {
        return new Coordinates(imageWidth - watermarkWidth - MIN_X_EDGE_SIZE, MIN_Y_EDGE_SIZE);
    }

    @Override
    public Coordinates bottomLeft() {
        return new Coordinates(MIN_X_EDGE_SIZE, imageHeight - watermarkHeight);
    }

    @Override
    public Coordinates bottomRight() {
        return new Coordinates(imageWidth - watermarkWidth - MIN_X_EDGE_SIZE, imageHeight - watermarkHeight);
    }

    @Override
    public List<Coordinates> tiled() {
        // Calculate the number of watermarks that can fit horizontally and vertically
        int numHorizontal = (int) Math.ceil((double) imageWidth / watermarkWidth);
        int numVertical = (int) Math.ceil((double) imageHeight / watermarkHeight);
        int spacing = 50;
        List<Coordinates> list = new ArrayList<>();

        for (int i = 0; i < numHorizontal; i++) {
            for (int j = 0; j < numVertical; j++) {
                list.add(new Coordinates((i * watermarkWidth) + (i * spacing), (j * watermarkHeight) + (j * spacing)));
            }
        }
        return list;
    }
}
