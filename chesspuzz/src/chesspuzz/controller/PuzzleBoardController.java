package chesspuzz.controller;

import chesspuzz.App;
import chesspuzz.ChessPGN;
import chesspuzz.Game;
import chesspuzz.staff.ChessBoard;
import chesspuzz.staff.GameObject;
import chesspuzz.staff.GameSound;
import chesspuzz.theme.PuzzleWinTheme;
import chesspuzz.theme.Theme;
import tanzi.staff.MoveHistory;
import tanzi.staff.MoveRepo;
import tanzi.staff.PGNBuilder;
import database.helper.Pref;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class PuzzleBoardController implements App.SceneObserver, ChessBoard.SquareSelectionListener {

    private Theme theme;

    @FXML
    HBox rootView;

    @FXML
    Pane board;

    private Game game;
    private MoveRepo moveRepo;

    GameSound gameSound;
    ChessBoard chessBoard;

    MoveHistory moveHistory;

    @FXML
    StackPane previous, next;

    ArrayList<String> puzzle;
    ArrayList<String> solution;
    ArrayList<String> solutionCopy;

    int selectedIndexA, selectedIndexB;

    @FXML
    public void initialize() {
        App.getApp().observeScene(this);

        moveHistory = new MoveHistory();

        puzzle = new ArrayList<>();
        solution = new ArrayList<>();
        solutionCopy = new ArrayList<>();

        gameSound = GameObject.getGameSound();

        chessBoard = new ChessBoard(720d);
        chessBoard.setSelectionListener(this);
        chessBoard.start();
        board.getChildren().add(chessBoard);


        game = Game.setup(chessBoard);


        loadNextPuzzle();
    }

    @Override
    public void onSceneAvailable(Scene scene) {
        // remove initial focus from the chord box to be able to show the hint
        Platform.runLater(() -> rootView.requestFocus());
        scene.setOnKeyPressed(event -> {
            // System.out.println(event.getCode() + " code");
            KeyCode code = event.getCode();
            if (code == KeyCode.RIGHT) {
                nextMove();
            } else if (code == KeyCode.LEFT) {
                previousMove();
            }
        });

        theme = new PuzzleWinTheme(scene);
        theme.applyTheme();
    }

    @Override
    public void reloadTheme() {
        theme.reloadTheme();
    }

    public void change() {
        String current = Pref.getString(App.Key.THEME, Theme.BLACK_MOON);
        String putting = current.equals(Theme.BLACK_MOON) ? Theme.HAVEL_LIGHT : Theme.BLACK_MOON;
        Pref.putString(App.Key.THEME, putting);
        theme.reloadTheme();
    }

    public void skip() {
        loadNextPuzzle();
    }


    public void previousMove() {
        game.takeBack();
    }

    public void nextMove() {
        game.nexMove();
    }


    private void loadNextPuzzle() {
        chessBoard.clearPossibleMoveHighlighting();


        puzzle.clear();
        solution.clear();
        solutionCopy.clear();

        ChessPGN.currentPuzzleIndex++;
        Pref.putInt(App.Key.CURRENT_PUZZLE_INDEX, ChessPGN.currentPuzzleIndex);

        String[] pgn = ChessPGN.getPuzzleAt(ChessPGN.currentPuzzleIndex);
        if (pgn == null || pgn.length < 1) return;

        puzzle.addAll(Arrays.asList(pgn[0].split(",")));
        solution.addAll(Arrays.asList(pgn[1].split(",")));
        solutionCopy.addAll(solution);

        System.out.println("Puzzle running: ");
        for (String x : puzzle) {
            System.out.print(x + ", ");
        }

        System.out.println("\nSolution : " + solutionCopy.get(0));

        moveRepo = MoveRepo.guardedRepo(pgn[0].split(","));

        moveHistory.__clear();
        game.load(moveRepo, moveHistory);
        game.toPoint(moveRepo.moveCount() - 1);

        chessBoard.whoseTurn(moveRepo.whoseTurn());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.nexMove();
            }
        }, 650);

    }

    public void showSolution() {

        if (solution.size() < 1) return;


        for (String move : solution) {
            moveRepo.add(move, true);
        }

        solution.clear();
        nextMove();
    }

    @Override
    public void onSelectSquare(int selectedIndexA, int selectedIndexB) {
        chessBoard.clearPossibleMoveHighlighting();
        this.selectedIndexA = selectedIndexA;
        this.selectedIndexB = selectedIndexB;

        String solMove = solutionCopy.get(0);
        String move = PGNBuilder.pgn(selectedIndexA, selectedIndexB, -1, GameObject.getArbiter());

        System.out.printf("Made: %s%n", move);

        if (solMove.equals(move)) {
            solutionCopy.remove(0);
            game.getMoveRepo().add(move, true);
            game.nexMove();

            if (solutionCopy.size() > 0) {
                String opponentMove = solutionCopy.remove(0);
                try {
                    game.getMoveRepo().add(opponentMove, true);
                    game.nexMove();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else {
                // loadNextPuzzle();
                System.out.println("Puzzle finished");
            }
        } else {
            System.out.println("Wrong move for this puzzle");
        }

    }

}
