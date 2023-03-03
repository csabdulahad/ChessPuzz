package chesspuzz.staff;

/*
 * this class holds all the objects that need to be singleton. it guarantees that all critical
 * objects like Board Registry, Geometry Engineer etc. are always representing the single version of
 * the current running game through out the system.
 *
 * with cached/buffered objects, it saves unnecessary objects creation which supposed to improve
 * program performance and reduce confusion of the code.
 * */

import tanzi.staff.Arbiter;
import tanzi.staff.BoardRegistry;

public class GameObject {

    private static boolean initialized;

    private static BoardRegistry boardRegistry;
    private static Arbiter arbiter;
    private static GameSound gameSound;

    private GameObject() {
    }

    private static void initialize() {
        if (initialized) return;

        boardRegistry = new BoardRegistry();
        arbiter = new Arbiter(boardRegistry);
        gameSound = new GameSound();
        initialized = true;
    }

    public static BoardRegistry getBR() {
        initialize();
        return boardRegistry;
    }

    public static Arbiter getArbiter() {
        initialize();
        return arbiter;
    }

    public static GameSound getGameSound() {
        initialize();
        return gameSound;
    }

}
