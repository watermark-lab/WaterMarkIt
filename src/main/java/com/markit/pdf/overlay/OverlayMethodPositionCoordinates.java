package com.markit.pdf.overlay;

import com.markit.api.PositionCoordinates;

/**
 * @author Oleg Cheban
 * @since 1.0
 */
public class OverlayMethodPositionCoordinates extends PositionCoordinates {
    private final int pageWidth;
    private final int pageHeight;
    private final int textWidth;
    private final int textHeight;
    private final int EDGE_SIZE = 10;

    public OverlayMethodPositionCoordinates(int iw, int ih, int ww, int wh) {
        this.pageWidth = iw;
        this.pageHeight = ih;
        this.textWidth = ww;
        this.textHeight = wh;
    }

    @Override
    public Coordinates center(){
        return new Coordinates((pageWidth - textWidth) / 2,(pageHeight - textHeight) / 2);
    }

    @Override
    public Coordinates topLeft(){
        return new Coordinates(EDGE_SIZE, pageHeight - textHeight - EDGE_SIZE);
    }

    @Override
    public Coordinates topRight() {
        return new Coordinates(pageWidth - textWidth - EDGE_SIZE,pageHeight - textHeight - EDGE_SIZE);
    }

    @Override
    public Coordinates bottomLeft() {
        return new Coordinates(EDGE_SIZE, textHeight);
    }

    @Override
    public Coordinates bottomRight() {
        return new Coordinates(pageWidth - textWidth - EDGE_SIZE,textHeight);
    }
}
