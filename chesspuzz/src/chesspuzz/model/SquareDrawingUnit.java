package chesspuzz.model;

import chesspuzz.protocol.DrawingUnit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SquareDrawingUnit implements DrawingUnit {

    private int animSpeed = 6;

    // variable that controls how often we should update the animation properties
    private float counter = 0.0f;

    // position variables
    public double x;
    public double y;
    private double previousX;
    private double previousY;
    public double destX;
    public double destY;

    // animation controlling variables
    private double stepX;
    private double stepY;
    private boolean forwardMotionX;
    private boolean forwardMotionY;

    private boolean isAtDest = false;
    public Image pieceBitmap;

    public SquareDrawingUnit(Image pieceBitmap, double destX, double destY, double previousX, double previousY) {

        // set variables
        this.pieceBitmap = pieceBitmap;
        this.x = previousX;
        this.y = previousY;
        this.destX = destX;
        this.destY = destY;
        this.previousX = previousX;
        this.previousY = previousY;

        // calculate the forward motion along X-axis
        forwardMotionX = destX > previousX;
        stepX = (Math.max(destX, previousX) - Math.min(destX, previousX)) / (float) animSpeed;

        // calculate the forward motion along Y-axis
        forwardMotionY = destY > previousY;
        stepY = (Math.max(destY, previousY) - Math.min(destY, previousY)) / (float) animSpeed;

    }

    @Override
    public void update(double delta) {

        // if we have done with animation already then return
        if (isAtDest) return;

        // increase the counter to count the delta
        counter += delta;

        // we are updating animation after each nano-second is passed
        if (counter < 1.0f) return;

        // update the x and y based on motion forward or backward
        x += forwardMotionX ? stepX : -stepX;
        y += forwardMotionY ? stepY : -stepY;

        // adjust x based on motion after the increment by stepX above
        if (forwardMotionX) {
            if (Double.compare(x, destX) > 0) x = destX;
        } else if (Double.compare(x, destX) < 0) {
            // it is backward motion & above decrement made the piece at destSquare already
            x = destX;
        }

        // adjust y based on motion after the increment by stepY above
        if (forwardMotionY) {
            if (Double.compare(y, destY) > 0) y = destY;
        } else if (Double.compare(y, destY) < 0) {
            // it is backward motion & above decrement made the piece at destSquare already
            y = destY;
        }

        // check whether the piece is at dest square from both x and y axis
        if (Double.compare(x, destX) == 0 && Double.compare(y, destY) == 0) isAtDest = true;

        // reset the counter
        this.counter = 0.0f;
    }

    @Override
    public void paint(GraphicsContext canvas) {
        canvas.drawImage(pieceBitmap, 0, 0, pieceBitmap.getWidth(), pieceBitmap.getHeight(), x, y, 90, 90);
    }

}
