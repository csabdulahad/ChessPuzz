package chesspuzz.lib.gridman;
/*
 * data class for holding point to be used by GridMan library. each point object represent starting
 * x & y of a grid in the rectangle.
 *
 * member variables are kept public for easier access for library usages.
 * */

public class GridManPoint {

    public double x, y;

    public GridManPoint() {

    }

    public GridManPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

}