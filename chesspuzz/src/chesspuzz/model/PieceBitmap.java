package chesspuzz.model;

import javafx.scene.image.Image;

// this POJO represents graphical piece on the board

public class PieceBitmap {

    // theme id for piece graphic
    public int pieceSetId;

    // piece info
    public int type;
    public int color;
    public Image bitmap;

    public PieceBitmap(int type, int color, int pieceSetId, Image bitmap) {
        this.type = type;
        this.color = color;
        this.pieceSetId = pieceSetId;
        this.bitmap = bitmap;
    }

}
