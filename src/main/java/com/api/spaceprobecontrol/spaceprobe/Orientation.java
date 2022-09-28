package com.api.spaceprobecontrol.spaceprobe;

public enum Orientation {
    NORTH {
        @Override
        Orientation alterOrientation(char c) {
            if (c == 'L')
                return WEST;
            return EAST;
        }
    },
    SOUTH {
        @Override
        Orientation alterOrientation(char c) {
            if (c == 'L')
                return EAST;
            return WEST;
        }
    },
    EAST {
        @Override
        Orientation alterOrientation(char c) {
            if (c == 'L')
                return NORTH;
            return SOUTH;
        }
    },
    WEST {
        @Override
        Orientation alterOrientation(char c) {
            if (c == 'L')
                return SOUTH;
            return NORTH;
        }
    };

    abstract Orientation alterOrientation(char c);
}
