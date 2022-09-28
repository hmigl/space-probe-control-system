package com.api.spaceprobecontrol.spaceprobe;

public enum Directions {
    NORTH {
        @Override
        Directions alterOrientation(char c) {
            if (c == 'L')
                return WEST;
            return EAST;
        }
    },
    SOUTH {
        @Override
        Directions alterOrientation(char c) {
            if (c == 'L')
                return EAST;
            return WEST;
        }
    },
    EAST {
        @Override
        Directions alterOrientation(char c) {
            if (c == 'L')
                return NORTH;
            return SOUTH;
        }
    },
    WEST {
        @Override
        Directions alterOrientation(char c) {
            if (c == 'L')
                return SOUTH;
            return NORTH;
        }
    };

    abstract Directions alterOrientation(char c);
}
