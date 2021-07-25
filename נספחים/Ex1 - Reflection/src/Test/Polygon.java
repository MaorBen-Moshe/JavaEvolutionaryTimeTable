package Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Polygon {
    private final Set<Point> points;

    public Polygon() {
        points = new HashSet<>();
    }

    public int getTotalPoints() {
        return points.size();
    }

    protected Point addPoint(int x, int y) {
        points.add(new Point(x, y));
        return points.stream().findFirst().get();
    }
}
