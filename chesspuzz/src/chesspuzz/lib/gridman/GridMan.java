package chesspuzz.lib.gridman;

/*
 * a library was created by Abdul Ahad. for known height, width, number of rows & columns, this
 * library can calculate the starting point for any given grid index. grids are of uniform size
 * meaning each grid of geometric rectangle shape.
 *
 * this library can be very powerful and useful when programs are dealing with grid based programming
 * where program knows above mentioned critical values.
 *
 * grid starts from 1 unlike zero-based array index for making clear assumption about grid counting.
 *
 * critical variables are those variables which define a rectangle in enough descriptive form so that
 * the library can calculate such rectangle width, height, number of columns, rows etc.
 * */

import java.util.HashMap;

public class GridMan {

    private double rectWidth;
    private double rectHeight;

    private double gridWidth;
    private double gridHeight;

    private HashMap<Integer, GridManPoint> indexPoint;
    private int numOfGrid;

    public GridMan(double rectWidth, double rectHeight, double gridWidth, double gridHeight) {
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        calculateIndexPointTable();
    }

    /*
     * for given critical grid variables calculate points. every time for changes to those variables
     * it clears out the previously calculated points and adds newly calculated points.
     * */
    private void calculateIndexPointTable() {
        numOfGrid = (int) (Math.round(rectWidth / gridWidth) * Math.round(rectHeight / gridHeight));
        indexPoint = new HashMap<>(numOfGrid);

        for (int i = 1; i <= numOfGrid; i++) {
            double x = getX(i);
            double y = getY(i);
            GridManPoint point = new GridManPoint(x, y);
            indexPoint.put(i, point);
        }
    }

    /*
     * for given x and y, this method calculates which grid number the point belongs to.
     * it returns -1, if given point can't be fitted within any grid defined by critical variables.
     * */
    public int getGridIndex(double x, double y) {
        for (int i = 1; i <= numOfGrid; i++) {
            GridManPoint point = indexPoint.get(i);
            if (point == null) continue;

            boolean xRange = x >= point.x && x <= (point.x + gridWidth);
            boolean yRange = y >= point.y && y <= (point.y + gridHeight);
            if (xRange && yRange) return i;
        }

        return -1;
    }

    /*
     * this method returns GridManPoint containing the starting point for a given grid index of a
     * grid. if the given grid index is larger than number of grid then it returns null.
     * */
    public GridManPoint getPoint(int gridIndex) {
        if (gridIndex > numOfGrid) return null;
        double x = getX(gridIndex);
        double y = getY(gridIndex);
        return new GridManPoint(x, y);
    }

    /*
     * description for these algorithms can be found at respective library website. consult the
     * official doc for more understanding.
     * */

    private double getX(int gridIndex) {
        double step1 = gridIndex * gridWidth;
        while (step1 > rectWidth) {
            step1 = step1 - rectWidth;
        }
        double step2 = step1 / gridWidth;
        double step3 = step2 * gridWidth;
        return step3 - gridWidth;
    }

    private double getY(int gridIndex) {
        double step1 = gridIndex * gridWidth;
        float step2 = (float) Math.ceil(step1 / rectHeight);
        double step3 = step2 * gridHeight;
        return step3 - gridHeight;
    }

    //  setter methods

    public void setRectWidth(float rectWidth) {
        this.rectWidth = rectWidth;
        calculateIndexPointTable();
    }

    public void setRectHeight(float rectHeight) {
        this.rectHeight = rectHeight;
        calculateIndexPointTable();
    }

    public void setGridWidth(double gridWidth) {
        this.gridWidth = gridWidth;
        calculateIndexPointTable();
    }

    public void setGridHeight(double gridHeight) {
        this.gridHeight = gridHeight;
        calculateIndexPointTable();
    }

}