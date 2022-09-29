package com.api.spaceprobecontrol.spaceprobe;

import com.api.spaceprobecontrol.planet.Planet;

import java.awt.Point;

public enum Orientation {
    NORTH {
        @Override
        Orientation alterOrientation(char c) {
            if (c == 'L')
                return WEST;
            return EAST;
        }

        @Override
        Point estimateCoordinate(Point currentCoordinate, Planet planet) {
            int y = currentCoordinate.y == planet.getyAxis() ? 1 : currentCoordinate.y + 1;
            return new Point(currentCoordinate.x, y);
        }
    },
    SOUTH {
        @Override
        Orientation alterOrientation(char c) {
            if (c == 'L')
                return EAST;
            return WEST;
        }

        @Override
        Point estimateCoordinate(Point currentCoordinate, Planet planet) {
            int y = currentCoordinate.y == 1 ? planet.getyAxis() : currentCoordinate.y - 1;
            return new Point(currentCoordinate.x, y);
        }
    },
    EAST {
        @Override
        Orientation alterOrientation(char c) {
            if (c == 'L')
                return NORTH;
            return SOUTH;
        }

        @Override
        Point estimateCoordinate(Point currentCoordinate, Planet planet) {
            int x = currentCoordinate.x == planet.getxAxis() ? 1 : currentCoordinate.x + 1;
            return new Point(x, currentCoordinate.y);
        }
    },
    WEST {
        @Override
        Orientation alterOrientation(char c) {
            if (c == 'L')
                return SOUTH;
            return NORTH;
        }

        @Override
        Point estimateCoordinate(Point currentCoordinate, Planet planet) {
            int x = currentCoordinate.x == 1 ? planet.getxAxis() : currentCoordinate.x - 1;
            return new Point(x, currentCoordinate.y);
        }
    };

    abstract Orientation alterOrientation(char c);
    abstract Point estimateCoordinate(Point currentCoordinate, Planet planet);
}
