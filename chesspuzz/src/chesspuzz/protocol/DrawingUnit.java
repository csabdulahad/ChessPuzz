package chesspuzz.protocol;

import javafx.scene.canvas.GraphicsContext;

public interface DrawingUnit {

    void update(double delta);

    void paint(GraphicsContext canvas);

}
