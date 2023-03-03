package chesspuzz.model;

import chesspuzz.protocol.DrawingUnit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SDUPossibleMove implements DrawingUnit {

    public double size = 35;
    public double x, y;
    public Color color = Color.web("#000000", .4);

    public SDUPossibleMove(double x, double y) {
        this.x = x + 45 - (size  / 2);
        this.y = y + 45 - (size  / 2);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void paint(GraphicsContext canvas) {
        canvas.save();
        canvas.setFill(color);
        canvas.fillOval(x, y, size, size);
        canvas.restore();
    }

}
