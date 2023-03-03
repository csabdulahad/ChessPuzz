package chesspuzz.staff;

import chesspuzz.App;
import chesspuzz.lib.gridman.GridMan;
import chesspuzz.lib.gridman.GridManPoint;
import chesspuzz.model.PieceBitmap;
import chesspuzz.model.SDUPossibleMove;
import chesspuzz.model.SquareDrawingUnit;
import chesspuzz.pool.PieceBitmapPool;
import chesspuzz.protocol.DrawingUnit;
import tanzi.model.Piece;
import tanzi.model.Square;
import tanzi.staff.BoardRegistry;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChessBoard extends Canvas implements BoardRegistry.ChangeListener {

    private int orientation;

    private SquareSelectionListener selectionListener;

    private GridMan gridMan;
    private PieceBitmapPool pieceBitmapPool;

    private Animator animator;
    private boolean running = true;

    private volatile GraphicsContext graphicsContext;

    private final Image chessBG;

    // map that holds SquareDrawingUnit to draw piece on the board
    private volatile ConcurrentHashMap<String, DrawingUnit> drawingUnits;
    private boolean ready = false;

    private volatile ArrayList<DrawingUnit> possibleMoveToFocus;

    BoardRegistry boardRegistry;

    private int whoseTurn;

    private int selectedIndexA, selectedIndexB;
    float pointAX, pointAY, pointBX, pointBY;

    public ChessBoard(double size) {

        setCursor(Cursor.HAND);

        boardRegistry = GameObject.getBR();
        boardRegistry.setChangeListener(this);

        animator = new Animator();

        drawingUnits = new ConcurrentHashMap<>();
        possibleMoveToFocus = new ArrayList<>();

        setWidth(size);
        setHeight(size);
        graphicsContext = getGraphicsContext2D();

        chessBG = new Image(App.getAsset(App.Key.CHESS_BOARD));

        gridMan = new GridMan(size, size, getWidth() / 8, (getHeight() / 8));
        pieceBitmapPool = new PieceBitmapPool(App.Key.PIECE_SET);
        initializeDrawingTable();

        setupClicking();
    }

    private boolean mousePressed;

    private void setupClicking() {

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (possibleMoveToFocus.size() > 0) possibleMoveToFocus.clear();

                // get which grid was selected
                selectedIndexA = gridMan.getGridIndex(event.getX(), event.getY());

                String square = Square.forIndex(selectedIndexA);

                boolean hasPiece = boardRegistry.anyPieceOn(square);
                if (!hasPiece) return;

                int selectedColor = boardRegistry.getPiece(square).color;
                // if (selectedColor != whoseTurn) return;

                highlightPossibleMove(square, GameObject.getArbiter().possibleMoveFor(square, boardRegistry));

                mousePressed = true;
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (possibleMoveToFocus.size() > 0) possibleMoveToFocus.clear();

                if (!mousePressed) return;

                mousePressed = false;

                selectedIndexB = gridMan.getGridIndex(event.getX(), event.getY());
                if (selectedIndexA == selectedIndexB || selectedIndexB < 0) return;

                if (selectionListener != null)
                    selectionListener.onSelectSquare(selectedIndexA, selectedIndexB);

            }
        });

    }

    public void run(float delta) {
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());
        graphicsContext.save();


        graphicsContext.drawImage(chessBG, 0, 0, 1200, 1200, 0, 0, getWidth(), getHeight());


        // draw piece from SquareDrawingUni map
        for (Map.Entry<String, DrawingUnit> entry : drawingUnits.entrySet()) {
            DrawingUnit sdu = entry.getValue();
            sdu.update(delta);
            sdu.paint(graphicsContext);
        }

        for (DrawingUnit drawingUnit : possibleMoveToFocus) {
            drawingUnit.update(delta);
            drawingUnit.paint(graphicsContext);
        }

        graphicsContext.restore();
    }

    private void initializeDrawingTable() {
        ArrayList<Piece> pieceList = GameObject.getBR().getRegisteredPiece();
        for (Piece piece : pieceList) {


            int in1 = Square.index(piece.getCurrentSquare(), orientation);
            int in2 = Square.index(piece.getPreviousSquare(), orientation);

            GridManPoint destPoint = gridMan.getPoint(in1);
            GridManPoint previousPoint = gridMan.getPoint(in2);

            /*System.out.printf("Piece %s, nor %d, flip %d\n",
                    piece.getShortName(),
                    Square.getIndex(piece.getCurrentSquare()),
                    Square.getIndex(piece.getCurrentSquare(), 1)
            );*/

            double destX = destPoint.x;
            double destY = destPoint.y;
            double previousX = previousPoint.x;
            double previousY = previousPoint.y;

            PieceBitmap pieceBitmap = pieceBitmapPool.get(piece.type, piece.color);
            SquareDrawingUnit drawingUnit = new SquareDrawingUnit(pieceBitmap.bitmap, destX, destY, previousX, previousY);
            drawingUnits.put(piece.getCurrentSquare(), drawingUnit);
        }
        ready = true;
    }

    public void add(Piece piece) {
        // get piece bitmap
        PieceBitmap pieceBitmap = pieceBitmapPool.get(piece.type, piece.color);

        // generate points for destination & previous positions for animation
        GridManPoint destPoint = gridMan.getPoint(Square.index(piece.getCurrentSquare(), orientation));
        GridManPoint previousPoint = gridMan.getPoint(Square.index(piece.getPreviousSquare(), orientation));

        // modify the dest point
        double destX = destPoint.x;
        double destY = destPoint.y;

        // add the piece wrapping inside SquareDrawingUnit
        drawingUnits.put(piece.getCurrentSquare(), new SquareDrawingUnit(pieceBitmap.bitmap, destX, destY, previousPoint.x, previousPoint.y));
    }

    public void delete(String square) {
        if (square == null) return;

        SquareDrawingUnit squareDrawingUnit = (SquareDrawingUnit) drawingUnits.remove(square);
//
//        if (squareDrawingUnit != null)
//            pieceBitmapPool.recycle(squareDrawingUnit);
    }

    public void __clear() {
        drawingUnits.clear();
        initializeDrawingTable();
    }

    public void start() {
        animator.start();
        running = true;
    }

    public void pause() {
        running = false;
        animator.start();
    }

    public void release() {

    }

    public void whoseTurn(int color) {
        whoseTurn = color;
        orientation = 1;
    }


    private void highlightPossibleMove(String from, ArrayList<String> possibleSquareList) {
        for (String square : possibleSquareList) {
            GridManPoint point = gridMan.getPoint(Square.index(square));
            possibleMoveToFocus.add(new SDUPossibleMove(point.x, point.y));
        }
    }

    @Override
    public void onDeletePiece(String square, Piece piece) {
        delete(square);
    }

    @Override
    public void onAddPiece(Piece piece) {
        add(piece);
    }

    @Override
    public void onBRClear() {
        __clear();
    }

    private final class Animator extends AnimationTimer {
        float lastFrameTimeNanos;

        @Override
        public void handle(long now) {
            float delta = now - lastFrameTimeNanos;
            lastFrameTimeNanos = now;
            run(delta);
        }
    }

    public void clearPossibleMoveHighlighting() {
        mousePressed = false;
        possibleMoveToFocus.clear();
    }

    public void setSelectionListener(SquareSelectionListener listener) {
        selectionListener = listener;
    }

    public interface SquareSelectionListener {
        void onSelectSquare(int selectedIndexA, int selectedIndexB);
    }

}
