package com.markit.api;

/**
 *
 * @author Oleg Cheban
 * @since 1.0
 */
public interface WatermarkPositionCoordinates {
    Coordinates center();
    Coordinates topLeft();
    Coordinates topRight();
    Coordinates bottomLeft();
    Coordinates bottomRight();

    final class Coordinates{
        private final int x;
        private final int y;

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
