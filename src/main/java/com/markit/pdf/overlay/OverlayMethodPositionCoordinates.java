package com.markit.pdf.overlay;

import com.markit.api.PositionCoordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class OverlayMethodPositionCoordinates extends PositionCoordinates {
    private final int pageWidth;
    private final int pageHeight;
    private final int watermarkWidth;
    private final int watermarkHeight;
    private final int EDGE_SIZE = 10;

    public OverlayMethodPositionCoordinates(int iw, int ih, int ww, int wh) {
        this.pageWidth = iw;
        this.pageHeight = ih;
        this.watermarkWidth = ww;
        this.watermarkHeight = wh;
    }

    @Override
    public Coordinates center(){
        return new Coordinates((pageWidth - watermarkWidth) / 2,(pageHeight - watermarkHeight) / 2);
    }

    @Override
    public Coordinates topLeft(){
        return new Coordinates(EDGE_SIZE, pageHeight - watermarkHeight - EDGE_SIZE);
    }

    @Override
    public Coordinates topRight() {
        return new Coordinates(pageWidth - watermarkWidth - EDGE_SIZE,pageHeight - watermarkHeight - EDGE_SIZE);
    }

    @Override
    public Coordinates bottomLeft() {
        return new Coordinates(EDGE_SIZE, watermarkHeight);
    }

    @Override
    public Coordinates bottomRight() {
        return new Coordinates(pageWidth - watermarkWidth - EDGE_SIZE, watermarkHeight);
    }

    @Override
    public List<Coordinates> tiled() {
        int numHorizontal = (int) Math.ceil((double) pageWidth / watermarkWidth);
        int numVertical = (int) Math.ceil((double) pageHeight / watermarkHeight);
        int spacing = 20;
        List<Coordinates> list = new ArrayList<>();
        for (int i = 0; i < numHorizontal; i++) {
            for (int j = 0; j < numVertical; j++) {
                list.add(new Coordinates((i * watermarkWidth) - i, (j * watermarkHeight) + (j * spacing)));
            }
        }
        return list;
    }
}
