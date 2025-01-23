package com.markit.pdf.overlay;

import com.markit.api.PositionCoordinates;

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
        throw new RuntimeException("a tiled position for overlay mode hasn't been implemented yet");
    }
}
