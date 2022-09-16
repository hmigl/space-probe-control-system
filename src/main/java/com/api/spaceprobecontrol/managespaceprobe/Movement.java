package com.api.spaceprobecontrol.managespaceprobe;

import java.awt.Point;
import java.util.List;

public interface Movement {
    boolean moveTowardsNorth(List<Point> coordinates);
    boolean moveTowardsSouth(List<Point> coordinates);
    boolean moveTowardsEast(List<Point> coordinates);
    boolean moveTowardsWest(List<Point> coordinates);
}
