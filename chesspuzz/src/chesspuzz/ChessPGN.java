package chesspuzz;

import database.Environment;
import database.PuzzleDB;
import database.helper.Pref;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChessPGN extends Application {

    public static int currentPuzzleIndex = 0;

    public static void main(String[] args) {
        Environment.check();

        // load the current game
        currentPuzzleIndex = Pref.getInt(App.Key.CURRENT_PUZZLE_INDEX, 1);
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        App.initialize(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("layout/puzzle_board.fxml"));
        primaryStage.setTitle("Chess Puzz");
        Scene scene = new Scene(root, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        primaryStage.setScene(scene);
        // primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static String[] getPuzzleAt(int puzzleIndex) {
        try {
            PuzzleDB puzzleDB = PuzzleDB.getInstance();
            ResultSet resultSet = puzzleDB.executeAndReturn("SELECT * FROM pgn WHERE id = " + puzzleIndex);
            if (!resultSet.next()) return null;

            String puzzle = resultSet.getString("problem");
            String solution = resultSet.getString("solution");
            resultSet.close();
            return new String[]{puzzle, solution};
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
