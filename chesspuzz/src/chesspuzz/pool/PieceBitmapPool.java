package chesspuzz.pool;

import chesspuzz.App;
import chesspuzz.data.PieceSet;
import chesspuzz.model.PieceBitmap;
import tanzi.model.Piece;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

import static tanzi.model.Piece.COLOR_WHITE;

public class PieceBitmapPool {

    private int pieceSetId;

    private static final int POOL_SIZE = 40;
    private final List<PieceBitmap> piecePool;

    public PieceBitmapPool(int pieceSetId) {
        this.pieceSetId = pieceSetId;
        piecePool = new ArrayList<>(POOL_SIZE);
    }

    public PieceBitmap get(int type, int color) {
        // search through the pool to find whether we have the requested type of PieceBitmap
        // if so then return it from the pool
        if (piecePool.size() > 0) {
            for (PieceBitmap pieceBitmap : piecePool)
                if (pieceBitmap.type == type && pieceBitmap.color == color && pieceBitmap.pieceSetId == pieceSetId)
                    return piecePool.remove(piecePool.indexOf(pieceBitmap));
        }

        // requested PieceBitmap isn't in the pool or the pool is empty. so let's crate one
        return create(type, color);
    }


    private PieceBitmap create(int type, int color) {
        String colorName = color == COLOR_WHITE ? "White" : "Black";
        String fileName = String.format("piece/%s/%s%s.png", PieceSet.getSetName(pieceSetId), colorName, Piece.getFullName(type));
        Image bitmap = new Image(App.getAsset(fileName));
        return new PieceBitmap(type, color, pieceSetId, bitmap);
    }

    public void recycle(PieceBitmap object) {
        // make sure we get the PieceBitmap of right piece set to recycle
        if (object.pieceSetId != pieceSetId) return;

        if (piecePool.size() < POOL_SIZE) piecePool.add(object);
    }

    public void changePieceSet(int pieceSetId) {
        this.pieceSetId = pieceSetId;
        piecePool.clear();
    }

}
