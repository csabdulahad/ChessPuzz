package chesspuzz;

import chesspuzz.staff.ChessBoard;
import chesspuzz.staff.GameObject;
import chesspuzz.staff.GameSound;
import tanzi.model.MoveChange;
import tanzi.model.MoveMeta;
import tanzi.staff.Arbiter;
import tanzi.staff.BoardRegistry;
import tanzi.staff.MoveAnalyzer;
import tanzi.staff.MoveHistory;
import tanzi.staff.MoveMaker;
import tanzi.staff.MoveRepo;

public class Game {

    private static Game INSTANCE;

    private ChessBoard chessBoard;

    private MoveRepo moveRepo;
    private final BoardRegistry boardRegistry;
    private final Arbiter arbiter;
    private MoveHistory moveHistory;
    private final GameSound gameSound;

    private boolean sound = true;

    private Game() {
        gameSound = new GameSound();

        boardRegistry = GameObject.getBR();
        arbiter = GameObject.getArbiter();
        moveHistory = new MoveHistory();
    }

    public void toPoint(int moveIndex) {
        // first stop making sound while moving towards the end of the move repo
        sound = false;

        int count = 0;
        while(count < moveIndex) {
            nexMove();
            count ++;
        }


        // enable the sound back on!
        sound = true;
    }

    public static Game setup(ChessBoard chessBoard) {
        if (INSTANCE == null) {
            INSTANCE = new Game();
            INSTANCE.chessBoard = chessBoard;
        }
        return INSTANCE;
    }

    public void load(MoveRepo moveRepo, MoveHistory moveHistory) {
        __clear();
        this.moveRepo = moveRepo;
        this.moveHistory = moveHistory;
    }

    public MoveRepo getMoveRepo() {
        return moveRepo;
    }

    public BoardRegistry getBoardRegistry() {
        return boardRegistry;
    }

    public Arbiter getArbiter() {
        return arbiter;
    }

    private void __clear() {
        arbiter.__clearAndSetup();
        moveRepo = null;
    }

    public boolean nexMove() {
        if (!moveRepo.hasNext()) {
            // System.out.println("End of PGN reached");
            return false;
        }

        MoveMeta moveMeta = MoveAnalyzer.analyze(moveRepo);
        try {
            MoveMaker.move(moveMeta, arbiter, moveHistory);
            if (sound) gameSound.playSound(moveMeta);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean takeBack() {
        MoveChange[] moveChanges = moveHistory.getLastChange();
        if (moveChanges.length <= 0) {
            //System.out.println("Beginning of PGN reached");
            return false;
        }

        boardRegistry.takeBack(moveChanges);
        moveRepo.previousMove();
        return true;
    }

    public boolean goAt(int moveIndex) {

        if (moveIndex >= moveRepo.moveCount()) return false;

        sound = false;
        if (moveHistory.currentIndexOnStack() > moveIndex) {
            while (moveHistory.currentIndexOnStack() != moveIndex) {
                takeBack();
            }
        } else {
            while (moveHistory.currentIndexOnStack() != moveIndex) {
                nexMove();
            }
        }

        sound = true;
        return true;
    }


}
