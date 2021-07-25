package Test;

import java.io.Serializable;

public class Rectangle extends Polygon implements Comparable, Serializable {

    private final int SCALE = 2;
    private int x;
    private int y;

    public Rectangle() {
        x = -1;
        y = -1;
    }

    public Rectangle(int x, int y) {
        this.x = x;
        this.y = y;
        updateParent();
    }

    public static void PRINT_SOMETHING() {
        System.out.println("this is a static method");
    }

    private void updateParent() {
        addPoint(0, 0);
        addPoint(x, 0);
        addPoint(0, y);
        addPoint(x, y);
    }

    public int calcArea() {
        return x * y;
    }

    public int calcPerimeter() {
        return twice(x) + twice(y);
    }

    private int twice(int num) {
        return 2 * num;
    }

    @Override
    public int compareTo(Object o) {
        return this.calcArea() - ((Rectangle) o).calcArea();
    }
}

