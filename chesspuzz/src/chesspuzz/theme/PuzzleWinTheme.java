package chesspuzz.theme;

import javafx.scene.Scene;

import java.util.ArrayList;

public class PuzzleWinTheme extends Theme {

    public PuzzleWinTheme(Scene scene) {
        super(scene);
    }

    @Override
    protected ArrayList<String> getStyle() {
        ArrayList<String> styleList = new ArrayList<>();
        styleList.add("puzzle_board");
        return styleList;
    }


}
