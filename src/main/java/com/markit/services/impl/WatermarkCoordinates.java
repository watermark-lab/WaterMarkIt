package com.markit.services.impl;

public class WatermarkCoordinates {
    private int imageWidth;
    private int imageHeignt;
    private int watermarkWidth;
    private int watermarkHeignt;
    private final int H_MIN_EDGE_SIZE = 30;
    private final int V_MIN_EDGE_SIZE = 10;

    public WatermarkCoordinates(int iw, int ih, int ww, int wh) {
        this.imageWidth = iw;
        this.imageHeignt = ih;
        this.watermarkWidth = ww;
        this.watermarkHeignt = wh;
    }

    public Coordinates center(){
        return new Coordinates((imageWidth - watermarkWidth) / 2, imageHeignt / 2 );
    }

    public Coordinates topLeft(){
        return new Coordinates(H_MIN_EDGE_SIZE, watermarkHeignt + V_MIN_EDGE_SIZE);
    }

    public Coordinates topRight() {
        return new Coordinates(imageWidth - watermarkWidth - H_MIN_EDGE_SIZE, watermarkHeignt + V_MIN_EDGE_SIZE);
    }

    public Coordinates bottomLeft() {
        return new Coordinates(H_MIN_EDGE_SIZE, imageHeignt - H_MIN_EDGE_SIZE);
    }

    public Coordinates bottomRight() {
        return new Coordinates(imageWidth - watermarkWidth - H_MIN_EDGE_SIZE, imageHeignt - H_MIN_EDGE_SIZE);
    }

    public final class Coordinates{
        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
