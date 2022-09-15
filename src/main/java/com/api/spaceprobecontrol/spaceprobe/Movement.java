package com.api.spaceprobecontrol.spaceprobe;

import java.awt.Point;
import java.util.List;

public interface Movement {
    void moveTowardsNorth(List<Point> coordinates);
    void moveTowardsSouth(List<Point> coordinates);
    void moveTowardsEast(List<Point> coordinates);
    void moveTowardsWest(List<Point> coordinates);
}
