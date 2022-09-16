package com.api.spaceprobecontrol.managespaceprobe;

public enum Directions {
    NORTH {
        @Override
        Directions nextSide(char c) {
            if (c == 'L')
                return WEST;
            return EAST;
        }
    },
    SOUTH {
        @Override
        Directions nextSide(char c) {
            if (c == 'L')
                return EAST;
            return WEST;
        }
    },
    EAST {
        @Override
        Directions nextSide(char c) {
            if (c == 'L')
                return NORTH;
            return SOUTH;
        }
    },
    WEST {
        @Override
        Directions nextSide(char c) {
            if (c == 'L')
                return SOUTH;
            return NORTH;
        }
    };

    abstract Directions nextSide(char c);
}
